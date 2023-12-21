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

package org.sentrysoftware.wbem.sblim.slp.internal.ua;

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

import java.net.InetAddress;
import java.util.Locale;
import java.util.SortedSet;
import java.util.Vector;

import org.sentrysoftware.wbem.sblim.slp.Locator;
import org.sentrysoftware.wbem.sblim.slp.ServiceLocationEnumeration;
import org.sentrysoftware.wbem.sblim.slp.ServiceType;
import org.sentrysoftware.wbem.sblim.slp.ServiceURL;
import org.sentrysoftware.wbem.sblim.slp.internal.SLPDefaults;
import org.sentrysoftware.wbem.sblim.slp.internal.TRC;
import org.sentrysoftware.wbem.sblim.slp.internal.msg.AttributeRequest;
import org.sentrysoftware.wbem.sblim.slp.internal.msg.ServiceRequest;
import org.sentrysoftware.wbem.sblim.slp.internal.msg.ServiceTypeRequest;
import org.sentrysoftware.wbem.sblim.slp.internal.msg.Util;

/**
 * LocatorImpl
 * 
 */
public class LocatorImpl implements Locator {

	private Locale iLocale;

	private String iLangTag;

	/**
	 * Ctor.
	 * 
	 * @param pLocale
	 */
	public LocatorImpl(Locale pLocale) {
		this.iLocale = pLocale;
		this.iLangTag = Util.getLangTag(this.iLocale);
		TRC.debug("created, langTag=" + this.iLangTag);
	}

	public ServiceLocationEnumeration findAttributes(ServiceURL pURL, Vector<String> pScopes,
			Vector<String> pAttributeIds) {
		return findAttributes(pURL, pScopes, pAttributeIds, null);
	}

	public ServiceLocationEnumeration findAttributes(ServiceURL pURL, Vector<String> pScopes,
			Vector<String> pAttributeIds, Vector<InetAddress> pDirectoryAgents) {
		return new SLEnumerationImpl(new AttributeRequest(this.iLangTag, (SortedSet<String>) null,
				pURL.toString(), getScopes(pScopes), pAttributeIds, null), pDirectoryAgents);
	}

	public ServiceLocationEnumeration findAttributes(ServiceType pType, Vector<String> pScopes,
			Vector<String> pAttributeIds) {
		return findAttributes(pType, pScopes, pAttributeIds, null);
	}

	public ServiceLocationEnumeration findAttributes(ServiceType pType, Vector<String> pScopes,
			Vector<String> pAttributeIds, Vector<InetAddress> pDirectoryAgents) {
		return new SLEnumerationImpl(new AttributeRequest(this.iLangTag, (SortedSet<String>) null,
				pType.toString(), getScopes(pScopes), pAttributeIds, null), pDirectoryAgents);
	}

	public ServiceLocationEnumeration findServiceTypes(String pNamingAuthority,
			Vector<String> pScopes) {
		return findServiceTypes(pNamingAuthority, pScopes, null);
	}

	public ServiceLocationEnumeration findServiceTypes(String pNamingAuthority,
			Vector<String> pScopes, Vector<InetAddress> pDirectoryAgent) {
		return new SLEnumerationImpl(new ServiceTypeRequest(this.iLangTag, null, pNamingAuthority,
				getScopes(pScopes)), pDirectoryAgent);
	}

	public ServiceLocationEnumeration findServices(ServiceType pType, Vector<String> pScopes,
			String pSearchFilter) {
		return findServices(pType, pScopes, pSearchFilter, null);
	}

	public ServiceLocationEnumeration findServices(ServiceType pType, Vector<String> pScopes,
			String pSearchFilter, Vector<InetAddress> pDirectoryAgents) {
		return new SLEnumerationImpl(new ServiceRequest(this.iLangTag, null, pType,
				getScopes(pScopes), pSearchFilter, null), pDirectoryAgents);
	}

	public Locale getLocale() {
		return this.iLocale;
	}

	/**
	 * @param pScopes
	 * @return pScopes if that is not empty or a Vector with "default" entry if
	 *         the pScopes is null or empty
	 */
	private static Vector<String> getScopes(Vector<String> pScopes) {
		if (pScopes == null) pScopes = new Vector<String>();
		if (pScopes.isEmpty()) pScopes.add(SLPDefaults.DEFAULT_SCOPE);
		return pScopes;
	}

}
