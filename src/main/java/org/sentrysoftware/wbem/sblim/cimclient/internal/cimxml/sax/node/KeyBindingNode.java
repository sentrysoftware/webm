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
 * 1720707    2007-05-17  ebak         Conventional Node factory for CIM-XML SAX parser
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 * 3602604    2013-01-29  blaschke-oss Clean up SAXException messages
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
import org.sentrysoftware.wbem.javax.cim.CIMProperty;

import org.sentrysoftware.wbem.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * ELEMENT KEYBINDING (KEYVALUE | VALUE.REFERENCE) ATTLIST KEYBINDING %CIMName;
 */
public class KeyBindingNode extends Node {

	private String iName;

	// child element
	private boolean iHasChild;

	private CIMDataType iType;

	private Object iValue;

	/**
	 * Ctor.
	 */
	public KeyBindingNode() {
		super(KEYBINDING);
	}

	/**
	 * @param pSession
	 */
	@Override
	public void init(Attributes pAttribs, SAXSession pSession) throws SAXException {
		this.iHasChild = false;
		this.iValue = null;
		this.iName = getCIMName(pAttribs);
	}

	/**
	 * @param pData
	 */
	@Override
	public void parseData(String pData) {
	// no data
	}

	@Override
	public void testChild(String pNodeNameEnum) throws SAXException {
		if (this.iHasChild) throw new SAXException("KEYBINDING node can only have one child node!");
		if (pNodeNameEnum != KEYVALUE && pNodeNameEnum != VALUE_REFERENCE) throw new SAXException(
				"KEYBINDING node's child node can be KEYVALUE or VALUE_REFERENCE but not "
						+ pNodeNameEnum);
	}

	@Override
	public void childParsed(Node pChild) {
		AbstractScalarValueNode abstScalarValNode = (AbstractScalarValueNode) pChild;
		this.iType = abstScalarValNode.getType();
		this.iValue = abstScalarValNode.getValue();
		this.iHasChild = true;
	}

	@Override
	public void testCompletness() throws SAXException {
		if (!this.iHasChild) throw new SAXException(
				"KEYBINDING node must have a KEYVALUE or VALUE_REFERENCE child node!");
	}

	/**
	 * getCIMProperty
	 * 
	 * @return CIMProperty
	 */
	public CIMProperty<Object> getCIMProperty() {
		// CIMProperty(
		// String name, CIMDataType type, Object value, boolean key, boolean
		// propagated, String originClass
		// )
		return new CIMProperty<Object>(this.iName, this.iType, this.iValue, true, false, null);
	}

}
