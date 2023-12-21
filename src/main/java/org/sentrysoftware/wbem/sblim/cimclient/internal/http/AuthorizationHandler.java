/*
  (C) Copyright IBM Corp. 2005, 2009

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
 * 1535756    2006-08-07  lupusalex    Make code warning free
 * 1516242    2006-11-27  lupusalex    Support of OpenPegasus local authentication
 * 1565892    2006-11-28  lupusalex    Make SBLIM client JSR48 compliant
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2) 
 */

package org.sentrysoftware.wbem.sblim.cimclient.internal.http;

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

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class AuthorizationHandler manages AdAuthorizationInfo instances
 * 
 */
public class AuthorizationHandler {

	private ArrayList<AuthorizationInfo> iAuthList = new ArrayList<AuthorizationInfo>();

	/**
	 * Adds an AuthorizationInfo to the handler
	 * 
	 * @param pAuthorizationInfo
	 *            The AuthorizationInfo to add
	 */
	public synchronized void addAuthorizationInfo(AuthorizationInfo pAuthorizationInfo) {
		this.iAuthList.add(pAuthorizationInfo);
	}

	/**
	 * Returns the corresponding AuthorizationInfo for a given set of parameters
	 * 
	 * @param pAuthorizationModule
	 *            The authorization module
	 * @param pProxy
	 *            Proxy authentication ?
	 * @param pAddr
	 *            Host address
	 * @param pPort
	 *            Host port
	 * @param pProtocol
	 *            Protocol
	 * @param pRealm
	 *            Realm
	 * @param pScheme
	 *            Scheme
	 * @return The AuthorizationInfo or <code>null</code> if none fits
	 */
	public synchronized AuthorizationInfo getAuthorizationInfo(String pAuthorizationModule,
			Boolean pProxy, String pAddr, int pPort, String pProtocol, String pRealm, String pScheme) {

		AuthorizationInfo request = AuthorizationInfo.createAuthorizationInfo(pAuthorizationModule,
				pProxy, pAddr, pPort, pProtocol, pRealm, pScheme);

		Iterator<AuthorizationInfo> iter = this.iAuthList.iterator();
		while (iter.hasNext()) {
			AuthorizationInfo authInfo = iter.next();

			if (authInfo.match(request)) return authInfo;

		}
		return null;
	}

	/**
	 * Returns the AuthorizationInfo at a given index
	 * 
	 * @param pIndex
	 *            The index
	 * @return The AuthorizationInfo
	 */
	public synchronized AuthorizationInfo getAuthorizationInfo(int pIndex) {
		return this.iAuthList.get(pIndex);
	}

	@Override
	public String toString() {
		return "AuthorizationHandler=[AuthInfoList=" + this.iAuthList + "]";
	}
}
