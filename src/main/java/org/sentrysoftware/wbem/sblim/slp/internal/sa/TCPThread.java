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
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 */

package org.sentrysoftware.wbem.sblim.slp.internal.sa;

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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.sentrysoftware.wbem.sblim.slp.internal.SLPConfig;
import org.sentrysoftware.wbem.sblim.slp.internal.TRC;

/**
 * TCPThread
 * 
 */
public class TCPThread extends RecieverThread {

	private ServerSocket iListenerSocket;

	/**
	 * Ctor.
	 * 
	 * @param pSrvAgent
	 */
	public TCPThread(ServiceAgent pSrvAgent) {
		super("TCP receiver", pSrvAgent);
	}

	@Override
	protected void init() throws IOException {
		this.iListenerSocket = new ServerSocket(SLPConfig.getGlobalCfg().getPort());
		this.iListenerSocket.setReuseAddress(true);
		this.iListenerSocket.setSoTimeout(100);
	}

	@Override
	protected void mainLoop() throws IOException {
		try {
			new ConnectionThread(this.iListenerSocket.accept());
		} catch (SocketTimeoutException e) {
			// superclass will execute the mainLoop again
		}
	}

	private class ConnectionThread implements Runnable {

		private Socket iSock;

		/**
		 * Ctor.
		 * 
		 * @param pSock
		 */
		public ConnectionThread(Socket pSock) {
			this.iSock = pSock;
			new Thread(this).start();
		}

		public void run() {
			TCPThread.this.iSrvAgent.processMessage(this.iSock);
		}

	}

	@Override
	protected void close() {
		if (this.iListenerSocket == null) return;
		try {
			this.iListenerSocket.close();
		} catch (IOException e) {
			TRC.error(e);
		}
	}

}
