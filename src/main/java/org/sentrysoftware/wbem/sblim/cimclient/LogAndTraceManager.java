/*
  (C) Copyright IBM Corp. 2006, 2012

  THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE
  ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE
  CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.

  You can obtain a current copy of the Eclipse Public License from
  http://www.opensource.org/licenses/eclipse-1.0.php

  @author : Alexander Wolf-Reber, IBM, a.wolf-reber@de.ibm.com
 * 
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 1565892    2006-11-13  lupusalex    Make SBLIM client JSR48 compliant
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 3554738    2012-08-16  blaschke-oss dump CIM xml by LogAndTraceBroker.trace()
 */

package org.sentrysoftware.wbem.sblim.cimclient;

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

import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

import org.sentrysoftware.wbem.sblim.cimclient.internal.logging.LogAndTraceBroker;

/**
 * Class LogAndTraceManager provides the means to register/unregister log and
 * trace listeners. It is the entry point for application that want to redirect
 * the CIM Client's log and trace messages into their own logging framework.
 * 
 */
public class LogAndTraceManager {

	private static LogAndTraceManager cManager = new LogAndTraceManager();

	/**
	 * Returns the singleton instance of the manager.
	 * 
	 * @return The manager
	 */
	public static LogAndTraceManager getManager() {
		return cManager;
	}

	private LogAndTraceManager() {
		super();
	}

	/**
	 * Adds a listener for log messages. The listener will be notified of any
	 * log event.
	 * 
	 * @param pListener
	 *            The listener
	 */
	public void addLogListener(LogListener pListener) {
		LogAndTraceBroker.getBroker().addLogListener(pListener);
	}

	/**
	 * Remove a listener. This listener will not be notified of log events
	 * anymore.
	 * 
	 * @param pListener
	 *            The listener
	 */
	public void removeLogListener(LogListener pListener) {
		LogAndTraceBroker.getBroker().removeLogListener(pListener);
	}

	/**
	 * Removes all listeners. Caution: This will also remove the internal
	 * console and file loggers.
	 */
	public void clearLogListeners() {
		LogAndTraceBroker.getBroker().clearLogListeners();
	}

	/**
	 * Gets the registered log listeners including the internal console and file
	 * loggers.
	 * 
	 * @return An unmodifiable list of listeners
	 */
	public List<LogListener> getLogListeners() {
		return Collections.unmodifiableList(LogAndTraceBroker.getBroker().getLogListeners());
	}

	/**
	 * Adds a listener for log messages. The listener will be notified of any
	 * trace event.
	 * 
	 * @param pListener
	 *            The listener
	 */
	public void addTraceListener(TraceListener pListener) {
		LogAndTraceBroker.getBroker().addTraceListener(pListener);
	}

	/**
	 * Removes a listener. This listener will not be notified of trace events
	 * anymore.
	 * 
	 * @param pListener
	 *            The listener
	 */
	public void removeTraceListener(TraceListener pListener) {
		LogAndTraceBroker.getBroker().removeTraceListener(pListener);
	}

	/**
	 * Removes all listeners. Caution this will also remove the internal trace
	 * file listener.
	 */
	public void clearTraceListeners() {
		LogAndTraceBroker.getBroker().clearTraceListeners();
	}

	/**
	 * Gets the registered trace listeners including the internal console and
	 * file loggers.
	 * 
	 * @return A unmodifiable list of listeners
	 */
	public List<TraceListener> getTraceListeners() {
		return Collections.unmodifiableList(LogAndTraceBroker.getBroker().getTraceListeners());
	}

	/**
	 * Adds a listener for CIM-XML trace messages. The listener will be notified
	 * of any CIM-XML trace event.
	 * 
	 * @param pListener
	 *            The listener
	 */
	public void addCIMXMLTraceListener(CIMXMLTraceListener pListener) {
		LogAndTraceBroker.getBroker().addCIMXMLTraceListener(pListener);
	}

	/**
	 * Removes a CIM-XML trace listener. This listener will not be notified of
	 * CIM-XML trace events anymore.
	 * 
	 * @param pListener
	 *            The listener
	 */
	public void removeCIMXMLTraceListener(CIMXMLTraceListener pListener) {
		LogAndTraceBroker.getBroker().removeCIMXMLTraceListener(pListener);
	}

	/**
	 * Removes all CIM-XML trace listeners.
	 */
	public void clearCIMXMLTraceListeners() {
		LogAndTraceBroker.getBroker().clearCIMXMLTraceListeners();
	}

	/**
	 * Gets the registered CIM-XML trace listeners.
	 * 
	 * @return A unmodifiable list of listeners
	 */
	public List<CIMXMLTraceListener> getCIMXMLTraceListeners() {
		return Collections
				.unmodifiableList(LogAndTraceBroker.getBroker().getCIMXMLTraceListeners());
	}

	/**
	 * Returns the stream to which the CIM-XML traces are sent. A value of
	 * <code>null</code> means that tracing is effectively disabled. Otherwise
	 * the CIM-XML tracing can be activated either globally or per-connection
	 * via the &quot;sblim.wbem.cimxmlTracing&quot; configuration property.
	 * 
	 * @return The CIM-XML trace stream
	 */
	public OutputStream getXmlTraceStream() {
		return LogAndTraceBroker.getBroker().getXmlTraceStream();
	}

	/**
	 * Sets the stream to which the CIM-XML traces are sent. A value of
	 * <code>null</code> means that tracing is effectively disabled. Otherwise
	 * the CIM-XML tracing can be activated either globally or per-connection
	 * via the &quot;sblim.wbem.cimxmlTracing&quot; configuration property.
	 * 
	 * @param pStream
	 *            The CIM-XML trace stream
	 */
	public void setXmlTraceStream(OutputStream pStream) {
		LogAndTraceBroker.getBroker().setXmlTraceStream(pStream);
	}

}
