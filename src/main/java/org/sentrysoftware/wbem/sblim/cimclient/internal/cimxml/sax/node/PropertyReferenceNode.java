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
 * 1689085    2007-04-10  ebak         Embedded object enhancements for Pegasus
 * 1714878    2007-05-08  ebak         Empty string property values are parsed as nulls
 * 1720707    2007-05-17  ebak         Conventional Node factory for CIM-XML SAX parser
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 *    2636    2013-05-08  blaschke-oss Nested embedded instances cause CIMXMLParseException
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

/**
 * <pre>
 * 
 * ELEMENT PROPERTY.REFERENCE (QUALIFIER*, (VALUE.REFERENCE)?)
 * ATTLIST PROPERTY.REFERENCE
 *   %CIMName; 
 *   %ReferenceClass; 
 *   %ClassOrigin;
 *   %Propagated;
 * </pre>
 */
public class PropertyReferenceNode extends AbstractPropertyNode {

	private String iRefClassName;

	// VALUE.REFERENCE
	private boolean iHasValueRef;

	private Object iValue;

	/**
	 * Ctor.
	 */
	public PropertyReferenceNode() {
		super(PROPERTY_REFERENCE);
	}

	@Override
	protected void childValueNodeParsed(Node pChild) {
		this.iValue = ((ValueReferenceNode) pChild).getValue();
		this.iHasValueRef = true;
	}

	@Override
	protected String getChildValueNodeNameEnum() {
		return VALUE_REFERENCE;
	}

	public Object getValue() {
		return this.iHasValueRef ? this.iValue : null;
	}

	@Override
	protected boolean hasValueNode() {
		return this.iHasValueRef;
	}

	/**
	 * @param pSession
	 */
	@Override
	protected void specificInit(Attributes pAttribs, SAXSession pSession) {
		this.iHasValueRef = false;
		this.iRefClassName = getReferenceClass(pAttribs);
	}

	@Override
	public void testCompletness() {
	//
	}

	public CIMDataType getType() {
		return new CIMDataType(this.iRefClassName != null ? this.iRefClassName : "");
	}

}
