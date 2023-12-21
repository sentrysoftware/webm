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
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 3511454    2012-03-27  blaschke-oss SAX nodes not reinitialized properly
 * 3466280    2012-04-23  blaschke-oss get instance failure for CIM_IndicationSubscription
 *    2604    2013-07-01  blaschke-oss SAXException messages should contain node name
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
import org.sentrysoftware.wbem.javax.cim.CIMObjectPath;

import org.sentrysoftware.wbem.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * ELEMENT VALUE.REFERENCE (CLASSPATH | LOCALCLASSPATH | CLASSNAME |
 * INSTANCEPATH | LOCALINSTANCEPATH | INSTANCENAME)
 */
public class ValueReferenceNode extends AbstractScalarValueNode implements ObjectPathIf {

	private CIMObjectPath iCIMObjPath;

	private String iChildNodeName = null;

	/**
	 * Ctor.
	 */
	public ValueReferenceNode() {
		super(VALUE_REFERENCE);
	}

	@Override
	public void childParsed(Node pChild) {
		this.iCIMObjPath = ((AbstractPathNode) pChild).getCIMObjectPath();
		if ((CLASSNAME.equalsIgnoreCase(this.iChildNodeName) || INSTANCENAME
				.equalsIgnoreCase(this.iChildNodeName))
				&& this.iCIMObjPath.getNamespace() != null) {
			// LocalPathBuilder includes default namespace in CLASSNAME and
			// INSTANCENAME elements, needs to be stripped
			this.iCIMObjPath = new CIMObjectPath(this.iCIMObjPath.getScheme(), this.iCIMObjPath
					.getHost(), this.iCIMObjPath.getPort(), null, this.iCIMObjPath.getObjectName(),
					this.iCIMObjPath.getKeys(), this.iCIMObjPath.getXmlSchemaName());
		}
	}

	/**
	 * @param pAttribs
	 * @param pSession
	 */
	@Override
	public void init(Attributes pAttribs, SAXSession pSession) {
		this.iCIMObjPath = null;
		this.iChildNodeName = null;
		// no attributes
	}

	/**
	 * @param pData
	 */
	@Override
	public void parseData(String pData) {
	// there is no data
	}

	private static final String[] ALLOWED_CHILDREN = { CLASSPATH, LOCALCLASSPATH, CLASSNAME,
			INSTANCEPATH, LOCALINSTANCEPATH, INSTANCENAME };

	@Override
	public void testChild(String pNodeNameEnum) throws SAXException {
		if (this.iCIMObjPath != null) throw new SAXException("Child node " + pNodeNameEnum
				+ " is illegal, since VALUE.REFERENCE already has a child!");
		for (int i = 0; i < ALLOWED_CHILDREN.length; i++)
			if (ALLOWED_CHILDREN[i] == pNodeNameEnum) {
				this.iChildNodeName = pNodeNameEnum;
				return;
			}
		throw new SAXException("Invalid child node in " + getNodeName() + " node: " + pNodeNameEnum
				+ "! Valid nodes are CLASSPATH, LOCALCLASSPATH, CLASSNAME, "
				+ "INSTANCEPATH, LOCALINSTANCEPATH, INSTANCENAME");
	}

	@Override
	public void testCompletness() throws SAXException {
		if (this.iCIMObjPath == null) throw new SAXException(
				"VALUE.REFERENCE node must have a child node!");
	}

	/**
	 * @see ValueIf#getValue()
	 * @return CIMObjectPath
	 */
	public Object getValue() {
		return this.iCIMObjPath;
	}

	public CIMDataType getType() {
		return this.iCIMObjPath == null ? null : new CIMDataType(this.iCIMObjPath.getObjectName());
	}

	public CIMObjectPath getCIMObjectPath() {
		return this.iCIMObjPath;
	}

}
