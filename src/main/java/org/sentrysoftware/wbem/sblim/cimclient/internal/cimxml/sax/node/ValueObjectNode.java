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
 * 1679534    2007-03-13  ebak         wrong ValueObjectNode.testChild()
 * 1720707    2007-05-17  ebak         Conventional Node factory for CIM-XML SAX parser
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 3602604    2013-01-29  blaschke-oss Clean up SAXException messages
 *    2604    2013-07-01  blaschke-oss SAXException messages should contain node name
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

import org.sentrysoftware.wbem.javax.cim.CIMClass;
import org.sentrysoftware.wbem.javax.cim.CIMDataType;
import org.sentrysoftware.wbem.javax.cim.CIMNamedElementInterface;

import org.sentrysoftware.wbem.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * ELEMENT VALUE.OBJECT (CLASS | INSTANCE)
 */
public class ValueObjectNode extends AbstractScalarValueNode {

	private CIMNamedElementInterface iCIMObject;

	/**
	 * Ctor.
	 */
	public ValueObjectNode() {
		super(VALUE_OBJECT);
	}

	/**
	 * @param pAttribs
	 * @param pSession
	 */
	@Override
	public void init(Attributes pAttribs, SAXSession pSession) {
		this.iCIMObject = null;
		// no attributes
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
		if (this.iCIMObject != null) throw new SAXException("This " + getNodeName()
				+ " node can have only one child but an additional " + pNodeNameEnum
				+ " node found!");
		if (pNodeNameEnum != CLASS && pNodeNameEnum != INSTANCE) throw new SAXException(
				getNodeName() + " node child node can be CLASS or INSTANCE but a " + pNodeNameEnum
						+ " node was found!");
	}

	@Override
	public void childParsed(Node pChild) {
		AbstractObjectNode objNode = (AbstractObjectNode) pChild;
		if (objNode instanceof ClassNode) this.iCIMObject = ((ClassNode) objNode).getCIMClass();
		else this.iCIMObject = ((InstanceNode) objNode).getCIMInstance();
	}

	@Override
	public void testCompletness() throws SAXException {
		if (this.iCIMObject == null) throw new SAXException(
				"VALUE.OBJECT node must have a CLASS or INSTANCE child node!");
	}

	/**
	 * @seeValueIf#getValue()
	 * @return CIMClass or CIMInstance
	 */
	public Object getValue() {
		return this.iCIMObject;
	}

	public CIMDataType getType() {
		if (this.iCIMObject instanceof CIMClass) return CIMDataType.CLASS_T;
		return CIMDataType.OBJECT_T;
	}

}
