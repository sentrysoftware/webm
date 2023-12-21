/*
  (C) Copyright IBM Corp. 2007, 2010

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
 * 2795671    2009-05-22  raman_arora  Add Type to Comparable <T>
 * 3023135    2010-07-01  blaschke-oss DADescriptor equals/compareTo issue
 */

package org.sentrysoftware.wbem.sblim.slp.internal.msg;

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

import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.sentrysoftware.wbem.sblim.slp.ServiceLocationAttribute;

/**
 * <pre>
 * This class contains the DA related information from a DAAdvert message.
 * URL
 * Scope list
 * Attribute list
 * </pre>
 */
public class DADescriptor implements Comparable<DADescriptor> {

	private String iURL;

	private TreeSet<String> iScopeSet;

	private List<ServiceLocationAttribute> iAttributes;

	/**
	 * Ctor.
	 * 
	 * @param pURL
	 * @param pScopeSet
	 *            - set of scope Strings
	 * @param pAttributes
	 *            - set of ServiceLocationAttributes
	 */
	public DADescriptor(String pURL, TreeSet<String> pScopeSet,
			List<ServiceLocationAttribute> pAttributes) {
		this.iURL = pURL;
		this.iScopeSet = pScopeSet;
		this.iAttributes = pAttributes;
	}

	/**
	 * getURL
	 * 
	 * @return String
	 */
	public String getURL() {
		return this.iURL;
	}

	/**
	 * hasScope
	 * 
	 * @param pScope
	 * @return boolean
	 */
	public boolean hasScope(String pScope) {
		if (this.iScopeSet == null) return false;
		return this.iScopeSet.contains(pScope);
	}

	public int compareTo(DADescriptor o) {
		DADescriptor that = o;
		return this.iURL.compareTo(that.iURL);
	}

	@Override
	public boolean equals(Object pObj) {
		if (!(pObj instanceof DADescriptor)) return false;
		DADescriptor that = (DADescriptor) pObj;
		return this.iURL.equals(that.iURL);
	}

	private int iHashCode = 0;

	private void incHashCode(int pHashCode) {
		this.iHashCode *= 31;
		this.iHashCode += pHashCode;
	}

	/*
	 * hashCode has to be independent of the order of scopes and attributes
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		if (this.iHashCode == 0) {
			this.iHashCode = this.iURL.hashCode();
			Iterator<?> itr;
			if (this.iScopeSet != null) {
				itr = this.iScopeSet.iterator();
				while (itr.hasNext())
					incHashCode(itr.next().hashCode());
			}
			if (this.iAttributes != null) {
				itr = this.iAttributes.iterator();
				/*
				 * iHasCode is simply incremented, because attribute order
				 * mustn't be considered.
				 */
				while (itr.hasNext())
					this.iHashCode += itr.next().hashCode();
			}
		}
		return this.iHashCode;
	}

	@Override
	public String toString() {
		StringBuffer strBuf = new StringBuffer("URL : " + this.iURL + "\nScopes : ");
		if (this.iScopeSet != null) {
			Iterator<String> itr = this.iScopeSet.iterator();
			boolean more = false;
			while (itr.hasNext()) {
				if (more) strBuf.append(", ");
				else more = true;
				strBuf.append(itr.next());
			}
		}

		return strBuf.toString();
	}

}
