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
 * 1712656    2007-05-04  ebak         Correct type identification for SVC CIMOM
 * 1714878    2007-05-08  ebak         Empty string property values are parsed as nulls
 * 1720707    2007-05-17  ebak         Conventional Node factory for CIM-XML SAX parser
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2) 
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

import org.sentrysoftware.wbem.javax.cim.CIMDataType;

import org.sentrysoftware.wbem.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * ELEMENT VALUE (#PCDATA)<br>
 * For non-standard CIMOMs the TYPE and PARAMTYPE attributes are supported.
 */
public class ValueNode extends AbstractScalarValueNode {

	private CIMDataType iType;

	private String iData;

	/**
	 * Ctor.
	 */
	public ValueNode() {
		super(VALUE);
	}

	/**
	 * @param pSession
	 */
	@Override
	public void init(Attributes pAttribs, SAXSession pSession) throws SAXException {
		this.iData = null;
		/*
		 * For supporting non-standard CIMOMs TYPE and PARAMTYPE attributes are
		 * handled
		 */
		this.iType = getCIMType(pAttribs, true);
		if (this.iType == null) this.iType = getParamType(pAttribs);
	}

	@Override
	public void parseData(String pData) {
		this.iData = pData;
	}

	@Override
	public void testChild(String pNodeNameEnum) throws SAXException {
		throw new SAXException("VALUE node cannot have any child node! (" + pNodeNameEnum + ")");
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
	// no child nodes
	}

	/**
	 * @see ValueIf#getValue() If
	 *      the getType() returns non-null value, the container Node have to
	 *      convert the String into the corresponding Java object.
	 * @return String
	 */
	public Object getValue() {
		/*
		 * If iData is null we return empty String. If null values have to be
		 * represented by avoiding the VALUE element, or placing VALUE.NULLs
		 * into VALUE.ARRAYs.
		 */
		return this.iData == null ? "" : this.iData;
	}

	/**
	 * @see TypedIf#getType()
	 * @return usually null, because the type is unknown, but can return
	 *         non-null in case of non-standard CIMOM.
	 */
	public CIMDataType getType() {
		return this.iType;
	}

}
