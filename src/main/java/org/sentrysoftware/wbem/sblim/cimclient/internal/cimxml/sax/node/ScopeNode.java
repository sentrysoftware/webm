/*
  (C) Copyright IBM Corp. 2006, 2009

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
 */

package org.sentrysoftware.wbem.sblim.cimclient.internal.cimxml.sax.node;

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

import org.sentrysoftware.wbem.javax.cim.CIMScope;

import org.sentrysoftware.wbem.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * <pre>
 * ELEMENT SCOPE EMPTY
 * ATTLIST SCOPE
 *     CLASS (true | false) &quot;false&quot;
 *     ASSOCIATION (true | false) &quot;false&quot;
 *     REFERENCE (true | false) &quot;false&quot;
 *     PROPERTY (true | false) &quot;false&quot;
 *     METHOD (true | false) &quot;false&quot;
 *     PARAMETER (true | false) &quot;false&quot;
 *     INDICATION (true | false) &quot;false&quot;
 * </pre>
 */
public class ScopeNode extends Node {

	private int iScope;

	/**
	 * Ctor.
	 */
	public ScopeNode() {
		super(SCOPE);
	}

	/**
	 * getScope
	 * 
	 * @return int - set of CIMScope bits
	 */
	public int getScope() {
		return this.iScope;
	}

	/**
	 * @param pSession
	 */
	@Override
	public void init(Attributes pAttribs, SAXSession pSession) {
		this.iScope = 0;
		if (hasTrueAttribute(pAttribs, "CLASS")) this.iScope |= CIMScope.CLASS;
		if (hasTrueAttribute(pAttribs, "ASSOCIATION")) this.iScope |= CIMScope.ASSOCIATION;
		if (hasTrueAttribute(pAttribs, "REFERENCE")) this.iScope |= CIMScope.REFERENCE;
		if (hasTrueAttribute(pAttribs, "PROPERTY")) this.iScope |= CIMScope.PROPERTY;
		if (hasTrueAttribute(pAttribs, "METHOD")) this.iScope |= CIMScope.METHOD;
		if (hasTrueAttribute(pAttribs, "PARAMETER")) this.iScope |= CIMScope.PARAMETER;
		if (hasTrueAttribute(pAttribs, "INDICATION")) this.iScope |= CIMScope.INDICATION;
	}

	/**
	 * @param pData
	 */
	@Override
	public void parseData(String pData) {
	// no data
	}

	/**
	 * @param pNodeNameEnum
	 */
	@Override
	public void testChild(String pNodeNameEnum) throws SAXException {
		throw new SAXException("SCOPE node cannot have any child node!");
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
	// Nothing to test, since it doesn't have any child node.
	}

}
