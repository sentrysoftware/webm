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
 * 1804402    2007-11-10  ebak         IPv6 ready SLP - revision 4
 * 1892103    2008-02-15  ebak         SLP improvements
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2) 
 */

package org.sentrysoftware.wbem.sblim.slp.internal;

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

import org.sentrysoftware.wbem.sblim.slp.Advertiser;
import org.sentrysoftware.wbem.sblim.slp.ServiceLocationAttribute;
import org.sentrysoftware.wbem.sblim.slp.ServiceLocationException;
import org.sentrysoftware.wbem.sblim.slp.ServiceURL;
import org.sentrysoftware.wbem.sblim.slp.internal.msg.MsgFactory;
import org.sentrysoftware.wbem.sblim.slp.internal.msg.SLPMessage;
import org.sentrysoftware.wbem.sblim.slp.internal.msg.ServiceAcknowledgment;
import org.sentrysoftware.wbem.sblim.slp.internal.msg.ServiceDeregistration;
import org.sentrysoftware.wbem.sblim.slp.internal.msg.ServiceRegistration;
import org.sentrysoftware.wbem.sblim.slp.internal.msg.Util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;



/**
 * AdvertiserImpl
 * 
 */
public class AdvertiserImpl implements Advertiser {

	private Locale iLocale;

	private String iLangTag;

	private List<String> iDefScopeList;

	private final int iMaxDatagramSize = SLPConfig.getGlobalCfg().getMTU();

	private final byte[] iInBuf = new byte[this.iMaxDatagramSize];

	private final int[] iTimeOuts = SLPConfig.getGlobalCfg().getDatagramTimeouts();

	private boolean iUseV6 = Net.hasIPv6() && SLPConfig.getGlobalCfg().useIPv6();

	/**
	 * Ctor.
	 * 
	 * @param pLocale
	 */
	public AdvertiserImpl(Locale pLocale) {
		this.iLocale = pLocale;
		this.iLangTag = Util.getLangTag(this.iLocale);
		this.iDefScopeList = new ArrayList<String>();
		this.iDefScopeList.add(SLPDefaults.DEFAULT_SCOPE);
	}

	/**
	 * @param pURL
	 * @param pAttributes
	 * @throws ServiceLocationException
	 */
	public void addAttributes(ServiceURL pURL, Vector<ServiceLocationAttribute> pAttributes)
	throws ServiceLocationException {
		throw new ServiceLocationException(ServiceLocationException.NOT_IMPLEMENTED);
	}

	/**
	 * @param pURL
	 * @param pAttributeIds
	 * @throws ServiceLocationException
	 */
	public void deleteAttributes(ServiceURL pURL, Vector<String> pAttributeIds)
			throws ServiceLocationException {
		throw new ServiceLocationException(ServiceLocationException.NOT_IMPLEMENTED);
	}

	public void deregister(ServiceURL pURL) throws ServiceLocationException {
		sendMessage(new ServiceDeregistration(this.iLangTag, this.iDefScopeList, pURL, null));
	}

	public Locale getLocale() {
		return this.iLocale;
	}

	public void register(ServiceURL pURL, Vector<ServiceLocationAttribute> pAttributes)
			throws ServiceLocationException {
		sendMessage(new ServiceRegistration(this.iLangTag, pURL, this.iDefScopeList, pAttributes,
		                                    null));

	}

	private void sendMessage(SLPMessage pMsg) throws ServiceLocationException {
		try {
			int res = unicast(pMsg);
			if (res != ServiceLocationException.OK) throw new ServiceLocationException((short) res);
			TRC.debug("service registered");
		} catch (IOException e) {
			throw new ServiceLocationException(ServiceLocationException.NETWORK_ERROR, e);
		}
	}

	private int unicast(SLPMessage pMsg) throws ServiceLocationException, IOException {
		DatagramSocket dgSocket = new DatagramSocket();
		try {
			byte[] reqBytes = pMsg.serialize(false, true, false);
			InetAddress loopback = this.iUseV6 ? SLPConfig.getLoopbackV6() : SLPConfig
					.getLoopbackV4();
			TRC.debug("loopback:" + loopback);

			DatagramPacket outPacket = new DatagramPacket(reqBytes, reqBytes.length, loopback,
					SLPConfig.getGlobalCfg().getPort());
			DatagramPacket inPacket = new DatagramPacket(this.iInBuf, this.iInBuf.length);

			int timeOutIdx = 0;
			while (timeOutIdx < this.iTimeOuts.length) {
				TRC.debug("sending : " + pMsg);
				dgSocket.send(outPacket);
				dgSocket.setSoTimeout(this.iTimeOuts[timeOutIdx++]);
				try {
					dgSocket.receive(inPacket);
				} catch (SocketTimeoutException e) {
					TRC.debug("receive time out");
					continue;
				}
				SLPMessage msg = MsgFactory.parse(inPacket);
				TRC.debug("expected: " + pMsg.getXID() + ", received : " + msg);
				if (msg instanceof ServiceAcknowledgment) {
					ServiceAcknowledgment ack = (ServiceAcknowledgment) msg;
					if (ack.getXID() == pMsg.getXID()) return ack.getErrorCode();
				}
				TRC.debug("ignoring :" + msg);
			}
		} finally {
			dgSocket.close();
		}
		TRC.warning("registration failed");
		return ServiceLocationException.INVALID_REGISTRATION;
	}

}
