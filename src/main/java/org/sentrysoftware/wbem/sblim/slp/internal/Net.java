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
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2) 
 */

package org.sentrysoftware.wbem.sblim.slp.internal;

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

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Net
 * 
 */
public class Net {

	private static boolean cHasV6, cHasV4;

	/**
	 * hasIPv6
	 * 
	 * @return boolean
	 */
	public static boolean hasIPv6() {
		scan();
		return cHasV6;
	}

	/**
	 * hasIPv4
	 * 
	 * @return boolean
	 */
	public static boolean hasIPv4() {
		scan();
		return cHasV4;
	}

	private static boolean cScanned;

	private static void scan() {
		if (cScanned) return;
		try {
			cScanned = true;
			Enumeration<NetworkInterface> ifaceEnum = NetworkInterface.getNetworkInterfaces();
			ifLoop: while (ifaceEnum.hasMoreElements()) {
				NetworkInterface iface = ifaceEnum.nextElement();
				Enumeration<InetAddress> addrEnum = iface.getInetAddresses();
				while (addrEnum.hasMoreElements()) {
					InetAddress addr = addrEnum.nextElement();
					if (addr instanceof Inet4Address) {
						cHasV4 = true;
						if (cHasV6) break ifLoop;
					} else if (addr instanceof Inet6Address) {
						cHasV6 = true;
						if (cHasV4) break ifLoop;
					}
				}
			}
			TRC.info("available IP versions : IPv4:" + cHasV4 + ", IPv6:" + cHasV6);
		} catch (SocketException e) {
			TRC.error(e);
		}
	}

}
