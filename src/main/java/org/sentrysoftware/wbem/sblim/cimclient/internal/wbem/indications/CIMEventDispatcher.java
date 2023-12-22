/*
  CIMEventDispatcher.java

  (C) Copyright IBM Corp. 2005, 2013

  THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE
  ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE
  CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.

  You can obtain a current copy of the Eclipse Public License from
  http://www.opensource.org/licenses/eclipse-1.0.php

  @author : Roberto Pineiro, IBM, roberto.pineiro@us.ibm.com
 * @author : Chung-hao Tan, IBM, chungtan@us.ibm.com
 * 
 * 
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 1498927    2006-06-01  lupusalex    Fill gaps in logging coverage
 * 1535756    2006-08-07  lupusalex    Make code warning free
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2204488 	  2008-10-28  raman_arora  Fix code to remove compiler warnings
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 3477087    2012-01-23  blaschke-oss Need Access to an Indication Sender's IP Address
 *    2628    2013-03-26  blaschke-oss Limit size of LinkedList of CIMEvents to be dispatched
 */
package org.sentrysoftware.wbem.sblim.cimclient.internal.wbem.indications;

/*-
 * ╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲
 * WBEM Java Client
 * ჻჻჻჻჻჻
 * Copyright (C) 2023 Sentry Software
 * ჻჻჻჻჻჻
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * ╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱
 */

import java.util.EventListener;
import java.util.LinkedList;
import java.util.logging.Level;

import org.sentrysoftware.wbem.javax.wbem.listener.IndicationListener;

import org.sentrysoftware.wbem.sblim.cimclient.IndicationListenerSBLIM;
import org.sentrysoftware.wbem.sblim.cimclient.internal.logging.LogAndTraceBroker;

/**
 * Class CIMEventDispatcher is required for indication handling.
 * 
 */
public class CIMEventDispatcher extends Thread {

	protected LinkedList<CIMEvent> iEventQueue = new LinkedList<CIMEvent>();

	protected EventListener iListener = null;

	private boolean iAlive = true;

	private int iMaxEvents = 0;

	private LogAndTraceBroker iLogger = LogAndTraceBroker.getBroker();

	/**
	 * Construct a CIMEventDispatcher object which distributes CIMEvents to the
	 * appropriate CIMListener. The EventListener must be an instance of
	 * IndicationListener or IndicationListenerSBLIM.
	 * 
	 * @param pListener
	 *            The CIMListener (IndicationListener or
	 *            IndicationListenerSBLIM) which receives the CIMEvents to be
	 *            dispatched.
	 */
	public CIMEventDispatcher(EventListener pListener) {
		this(pListener, 0);
	}

	/**
	 * Construct a CIMEventDispatcher object which distributes CIMEvents to the
	 * appropriate CIMListener. The EventListener must be an instance of
	 * IndicationListener or IndicationListenerSBLIM.
	 * 
	 * @param pListener
	 *            The CIMListener (IndicationListener or
	 *            IndicationListenerSBLIM) which receives the CIMEvents to be
	 *            dispatched.
	 * @param pMaxEvents
	 *            The maximum number of CIMEvents waiting to be dispatched.
	 */
	public CIMEventDispatcher(EventListener pListener, int pMaxEvents) {
		if (!(pListener instanceof IndicationListener)
				&& !(pListener instanceof IndicationListenerSBLIM)) throw new IllegalArgumentException(
				"Listener must be instance of IndicationListener or IndicationListenerSBLIM");
		this.iListener = pListener;
		this.iMaxEvents = pMaxEvents;
		setDaemon(true);
		setName("CIMEventDispatcher");
		start();
	}

	/**
	 * Propagates the CIMEvent to the event consumers.
	 * 
	 * @param pEvent
	 *            The CIMEvent to be dispatched.
	 */
	public synchronized void dispatchEvent(CIMEvent pEvent) {

		if (pEvent != null) {
			if (this.iMaxEvents > 0) {
				int size = this.iEventQueue.size();
				if (size >= this.iMaxEvents) {
					for (int i = size - this.iMaxEvents + 1; i > 0; i--) {
						CIMEvent event = this.iEventQueue.remove(0);
						this.iLogger.trace(Level.FINE, "Deleted CIMEvent (id=" + event.getID()
								+ ") from the queue (maximum size of " + this.iMaxEvents
								+ " reached)");
					}
				}
			}
			this.iEventQueue.add(pEvent);
			this.iLogger.trace(Level.FINE, "Added CIMEvent (id=" + pEvent.getID()
					+ ") to the queue (" + this.iEventQueue.size() + " elements total)");
			notify();
		} else {
			this.iLogger.trace(Level.WARNING, "CIMEvent to dispatch was null");
		}
	}

	/**
	 * Starts the dispatching engine of the CIMEventDispatcher.
	 * 
	 */
	public synchronized void startup() {
		this.iAlive = true;
		start();
	}

	/**
	 * Stops the dispatching of events.
	 */
	public synchronized void kill() {
		this.iAlive = false;
		notify();
	}

	/**
	 * close
	 */
	public synchronized void close() {
		kill();
	}

	private synchronized CIMEvent getEvent() {
		CIMEvent event = null;
		while (event == null) {
			try {
				if (this.iEventQueue.size() == 0) wait();
			} catch (InterruptedException e) { /**/}
			if (!this.iAlive) break;
			if (this.iEventQueue.size() > 0) {
				event = this.iEventQueue.remove(0);
				this.iLogger.trace(Level.FINER, "Removed CIMEvent (id=" + event.getID()
						+ ") from the queue (" + this.iEventQueue.size() + " elements left)");
			}
		}
		return event;
	}

	@Override
	public void run() {
		while (this.iAlive) {
			try {
				CIMEvent event = getEvent();
				if (event != null) {
					this.iLogger.trace(Level.FINER, "Processing CIMEvent (id=" + event.getID()
							+ ")");
					try {
						if (this.iListener instanceof IndicationListener) ((IndicationListener) this.iListener)
								.indicationOccured(event.getID(), event.getIndication());
						else // if instanceof IndicationListenerSBLIM)
						((IndicationListenerSBLIM) this.iListener).indicationOccured(event.getID(),
								event.getIndication(), event.getInetAddress());
					} catch (Throwable t) {
						this.iLogger.trace(Level.FINE, "Exception caught in listener ("
								+ this.iListener.getClass().getName()
								+ ") while processing CIMEvent", t);
					}
				}
			} catch (Throwable t) {
				this.iLogger.trace(Level.FINE, "Exception in event dispatcher loop", t);
			}
		}
	}
}
