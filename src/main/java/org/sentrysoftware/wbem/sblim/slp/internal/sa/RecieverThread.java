/*
  (C) Copyright IBM Corp. 2007, 2009
 
  THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE 
  ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE 
  CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.
 
  You can obtain a current copy of the Eclipse Public License from
  http://www.opensource.org/licenses/eclipse-1.0.php
 
  @author : Endre Bak, IBM, ebak@de.ibm.com
 * 
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 1804402    2007-09-28  ebak         IPv6 ready SLP
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 */

package org.sentrysoftware.wbem.sblim.slp.internal.sa;

/*-
 * ╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲
 * WbeM Java Client
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

import java.io.IOException;

import org.sentrysoftware.wbem.sblim.slp.ServiceLocationException;
import org.sentrysoftware.wbem.sblim.slp.internal.TRC;

/**
 * RecieverThread
 * 
 */
public abstract class RecieverThread implements Runnable {

	private Thread iThread;

	private volatile boolean iStop;

	private boolean iInited;

	private Object iInitLock = new Object();

	protected ServiceAgent iSrvAgent;

	/**
	 * Ctor.
	 * 
	 * @param pName
	 * @param pSrvAgent
	 */
	public RecieverThread(String pName, ServiceAgent pSrvAgent) {
		this.iThread = new Thread(this, pName);
		this.iSrvAgent = pSrvAgent;
	}

	/**
	 * start
	 */
	public void start() {
		this.iThread.start();
	}

	/**
	 * wait4init
	 */
	public void wait4init() {
		synchronized (this.iInitLock) {
			try {
				if (this.iInited) return;
				this.iInitLock.wait();
				return;
			} catch (InterruptedException e) {
				TRC.error(e);
			}
		}
	}

	/**
	 * stop
	 */
	public void stop() {
		stop(true);
	}

	/**
	 * stop
	 * 
	 * @param pWait
	 */
	public void stop(boolean pWait) {
		this.iStop = true;
		if (pWait) join();
	}

	public void run() {
		// 1st init
		synchronized (this.iInitLock) {
			TRC.debug("initing");
			initialize();
			this.iInited = true;
			TRC.debug("inited");
			try {
				this.iInitLock.notifyAll();
			} catch (IllegalMonitorStateException e) {
				TRC.error(e);
			}
		}
		while (!this.iStop) {
			try {
				mainLoop();
			} catch (Exception e) {
				TRC.error(e);
				sleep(100);
				initialize();
			}
		}
		close();
		this.iStop = false;
		TRC.debug("STOPPED");
	}

	// exception of init shoud be reachable for ServiceTable

	protected abstract void init() throws ServiceLocationException, IOException;

	protected abstract void mainLoop() throws IOException;

	protected abstract void close();

	private void join() {
		try {
			this.iThread.join();
		} catch (InterruptedException e) {
			TRC.error(e);
		}
	}

	private void initialize() {
		try {
			init();
		} catch (ServiceLocationException e) {
			TRC.error(e);
		} catch (IOException e) {
			TRC.error(e);
		}
	}

	private static void sleep(int pMillis) {
		try {
			Thread.sleep(pMillis);
		} catch (InterruptedException e) {
			TRC.error(e);
		}
	}

}
