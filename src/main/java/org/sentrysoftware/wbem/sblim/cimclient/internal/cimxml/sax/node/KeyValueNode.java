/*
  (C) Copyright IBM Corp. 2006, 2013

  THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE
  ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE
  CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.

  You can obtain a current copy of the Eclipse Public License from
  http://www.opensource.org/licenses/eclipse-1.0.php

  @author : Endre Bak, ebak@de.ibm.com
 * 
 * Flag       Date        Prog         Description
 * -------------------------------------------------------------------------------
 * 1565892    2006-12-04  ebak         Make SBLIM client JSR48 compliant
 * 1663270    2007-02-19  ebak         Minor performance problems
 * 1660756    2007-02-22  ebak         Embedded object support
 * 1714878    2007-05-08  ebak         Empty string property values are parsed as nulls
 * 1720707    2007-05-17  ebak         Conventional Node factory for CIM-XML SAX parser
 * 1769504	  2007-08-08  ebak         Type identification for VALUETYPE="numeric"
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 * 2823494    2009-08-03  rgummada     Change Boolean constructor to static
 * 3572993    2012-10-01  blaschke-oss parseDouble("2.2250738585072012e-308") DoS vulnerability
 * 3602604    2013-01-29  blaschke-oss Clean up SAXException messages
 *    2604    2013-07-01  blaschke-oss SAXException messages should contain node name
 *    2683    2013-10-07  blaschke-oss KEYVALUE VALUETYPE optional, "string" default
 */

package org.sentrysoftware.wbem.sblim.cimclient.internal.cimxml.sax.node;

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

import org.sentrysoftware.wbem.javax.cim.CIMDataType;
import org.sentrysoftware.wbem.javax.cim.CIMDateTimeAbsolute;
import org.sentrysoftware.wbem.javax.cim.CIMDateTimeInterval;
import org.sentrysoftware.wbem.javax.cim.UnsignedInteger64;

import org.sentrysoftware.wbem.sblim.cimclient.internal.cimxml.sax.CIMObjectFactory;
import org.sentrysoftware.wbem.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.sentrysoftware.wbem.sblim.cimclient.internal.util.MOF;
import org.sentrysoftware.wbem.sblim.cimclient.internal.util.Util;
import org.sentrysoftware.wbem.sblim.cimclient.internal.util.WBEMConfiguration;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * ELEMENT KEYVALUE (#PCDATA) ATTLIST KEYVALUE VALUETYPE (string | boolean |
 * numeric) "string" %CIMType; #IMPLIED
 */
public class KeyValueNode extends AbstractScalarValueNode {

	private CIMDataType iType;

	private String iValueTypeStr;

	private Object iValue;

	/**
	 * Ctor.
	 */
	public KeyValueNode() {
		super(KEYVALUE);
	}

	/**
	 * @param pSession
	 */
	@Override
	public void init(Attributes pAttribs, SAXSession pSession) throws SAXException {
		this.iType = getCIMType(pAttribs, true);
		this.iValue = null;
		if (this.iType != null) {
			if (this.iType.isArray()) throw new SAXException("KEYVALUE node cannot be array typed!");
			return;
		}
		// let's see the VALUETYPE attribute
		this.iValueTypeStr = pAttribs.getValue("VALUETYPE");
		if (this.iValueTypeStr == null) this.iValueTypeStr = "string";
	}

	@Override
	public void parseData(String pData) throws SAXException {
		if (this.iType == null) {
			setTypeAndValue(pData);
		} else {
			this.iValue = CIMObjectFactory.getObject(this.iType, pData);
		}
	}

	/**
	 * @param pNodeNameEnum
	 */
	@Override
	public void testChild(String pNodeNameEnum) throws SAXException {
		throw new SAXException("KEYVALUE node cannot have any child nodes!");
	}

	/**
	 * @param pChild
	 */
	@Override
	public void childParsed(Node pChild) {
	// no child
	}

	@Override
	public void testCompletness() {
	// nothing to do
	}

	public Object getValue() {
		/*
		 * null key values are not allowed, empty #PCDATA means empty String
		 */
		return this.iValue == null ? "" : this.iValue;
	}

	public CIMDataType getType() {
		return this.iType;
	}

	private void setTypeAndValue(String pValue) throws SAXException {
		// determine iType from iValueTypeStr if required
		if (this.iType != null) return;

		if (this.iValueTypeStr.equals("numeric")) {
			if (!setUInt64(pValue) && !setSInt64(pValue) && !setReal64(pValue)) throw new SAXException(
					"Unparseable \"number\" value in " + getNodeName() + " node: " + pValue + "!");
		} else if (this.iValueTypeStr.equals(MOF.DT_STR)) {
			if (!setDTAbsolute(pValue) && !setDTInterval(pValue)) {
				this.iValue = pValue;
				this.iType = CIMDataType.STRING_T;
			}
		} else if (this.iValueTypeStr.equals(MOF.DT_BOOL)) {
			if (!setBoolean(pValue)) throw new SAXException("Unparseable \"boolean\" value in "
					+ getNodeName() + " node: " + pValue + "!");
		} else {
			throw new SAXException("KEYVALUE node's VALUETYPE attribute must be " + MOF.DT_STR
					+ ", " + MOF.DT_BOOL + " or numeric! " + pValue + " is not allowed!");
		}
	}

	private boolean setUInt64(String pValue) {
		try {
			this.iValue = new UnsignedInteger64(pValue);
		} catch (NumberFormatException e) {
			return false;
		}
		this.iType = CIMDataType.UINT64_T;
		return true;
	}

	private boolean setSInt64(String pValue) {
		try {
			this.iValue = new Long(pValue);
		} catch (NumberFormatException e) {
			return false;
		}
		this.iType = CIMDataType.SINT64_T;
		return true;
	}

	private boolean setReal64(String pValue) {
		try {
			if (WBEMConfiguration.getGlobalConfiguration().verifyJavaLangDoubleStrings()) {
				if (Util.isBadDoubleString(pValue)) return false;
			}
			this.iValue = new Double(pValue);
		} catch (NumberFormatException e) {
			return false;
		}
		this.iType = CIMDataType.REAL64_T;
		return true;
	}

	private boolean setBoolean(String pValue) {
		this.iValue = Boolean.valueOf(pValue);
		this.iType = CIMDataType.BOOLEAN_T;
		return true;
	}

	private boolean setDTAbsolute(String pValue) {
		try {
			this.iValue = new CIMDateTimeAbsolute(pValue);
		} catch (IllegalArgumentException e) {
			return false;
		}
		this.iType = CIMDataType.DATETIME_T;
		return true;
	}

	private boolean setDTInterval(String pValue) {
		try {
			this.iValue = new CIMDateTimeInterval(pValue);
		} catch (IllegalArgumentException e) {
			return false;
		}
		this.iType = CIMDataType.DATETIME_T;
		return true;
	}

}
