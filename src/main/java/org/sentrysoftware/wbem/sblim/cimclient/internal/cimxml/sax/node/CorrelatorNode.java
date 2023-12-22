/*
  (C) Copyright IBM Corp. 2013

  THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE
  ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE
  CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.

  You can obtain a current copy of the Eclipse Public License from
  http://www.opensource.org/licenses/eclipse-1.0.php

  @author : Dave Blaschke, blaschke@us.ibm.com
 * 
 * Flag       Date        Prog         Description
 * -------------------------------------------------------------------------------
 *    2538    2013-11-28  blaschke-oss CR14: Support new CORRELATOR element
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

import org.sentrysoftware.wbem.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * <pre>
 * ELEMENT CORRELATOR (VALUE)
 * ATTLIST CORRELATOR
 *   %CIMName;
 *   %CIMType; #REQUIRED
 * </pre>
 */
public class CorrelatorNode extends Node implements TypedIf, ValueIf {

	// private String iName;

	private CIMDataType iType;

	private Object iValue;

	private boolean iHasValue;

	/**
	 * Ctor.
	 */
	public CorrelatorNode() {
		super(CORRELATOR);
	}

	/**
	 * @param pSession
	 */
	@Override
	public void init(Attributes pAttribs, SAXSession pSession) throws SAXException {
		/* this.iName = */getCIMName(pAttribs);
		this.iType = getCIMType(pAttribs, false);
		this.iValue = null;
		this.iHasValue = false;
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
		if (pNodeNameEnum != VALUE) throw new SAXException(pNodeNameEnum
				+ " cannot be the child node of " + getNodeName() + " node!");
		if (this.iHasValue) throw new SAXException(getNodeName()
				+ " node can have only one child node!");
	}

	@Override
	public void childParsed(Node pChild) {
		if (pChild instanceof ValueNode) this.iHasValue = true;
		this.iValue = ((ValueNode) pChild).getValue();
	}

	@Override
	public void testCompletness() throws SAXException {
		if (!this.iHasValue) throw new SAXException(getNodeName()
				+ " node must have one VALUE child node!");
	}

	public CIMDataType getType() {
		return this.iType;
	}

	public Object getValue() {
		return this.iValue;
	}

}
