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
 * 1892103    2008-02-13  ebak         SLP improvements
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
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
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;

import org.sentrysoftware.wbem.sblim.slp.internal.SLPConfig;
import org.sentrysoftware.wbem.sblim.slp.internal.SLPDefaults;
import org.sentrysoftware.wbem.sblim.slp.internal.TRC;

/**
 * DatagramThread
 * 
 */
public class DatagramThread extends RecieverThread {

	private static MulticastSocket cMCastSocket;

	private DatagramPacket iPacket = new DatagramPacket(new byte[SLPDefaults.MTU], SLPDefaults.MTU);

	/**
	 * Ctor.
	 * 
	 * @param pSrvAgent
	 */
	public DatagramThread(ServiceAgent pSrvAgent) {
		super("DatagramThread", pSrvAgent);
	}

	/**
	 * joinGroup
	 * 
	 * @param pGroup
	 * @throws IOException
	 */
	public synchronized void joinGroup(InetAddress pGroup) throws IOException {
		TRC.debug("join:" + pGroup);
		cMCastSocket.joinGroup(pGroup);
	}

	/**
	 * leaveGroup
	 * 
	 * @param pGroup
	 * @throws IOException
	 */
	public synchronized void leaveGroup(InetAddress pGroup) throws IOException {
		TRC.debug("leave:" + pGroup);
		cMCastSocket.leaveGroup(pGroup);
	}

	@Override
	protected void init() throws IOException {
		if (cMCastSocket == null) {
			cMCastSocket = new MulticastSocket(SLPConfig.getGlobalCfg().getPort());
			cMCastSocket.setReuseAddress(true);
			cMCastSocket.setSoTimeout(100);
		}
	}

	@Override
	protected void mainLoop() throws IOException {
		try {
			cMCastSocket.receive(this.iPacket);
			TRC.debug("Packet received");
			this.iSrvAgent.processMessage(cMCastSocket, this.iPacket);
		} catch (SocketTimeoutException e) {
			// superclass will restart this function
		}
	}

	@Override
	protected void close() {
		if (cMCastSocket == null) return;
		cMCastSocket.close();
		cMCastSocket = null;
	}

}
