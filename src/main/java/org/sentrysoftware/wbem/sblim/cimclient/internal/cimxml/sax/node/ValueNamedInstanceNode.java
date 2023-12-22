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
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 * 3598613    2013-01-11  blaschke-oss different data type in cim instance and cim object path
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
import org.sentrysoftware.wbem.javax.cim.CIMInstance;
import org.sentrysoftware.wbem.javax.cim.CIMObjectPath;

import org.sentrysoftware.wbem.sblim.cimclient.internal.cim.CIMHelper;
import org.sentrysoftware.wbem.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.sentrysoftware.wbem.sblim.cimclient.internal.util.WBEMConfiguration;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * <pre>
 * ELEMENT VALUE.NAMEDINSTANCE (INSTANCENAME, INSTANCE)
 * 
 * ELEMENT INSTANCENAME (KEYBINDING* | KEYVALUE? | VALUE.REFERENCE?)
 * ATTLIST INSTANCENAME
 *   %ClassName;
 *   
 * ELEMENT INSTANCE (QUALIFIER*, (PROPERTY | PROPERTY.ARRAY | PROPERTY.REFERENCE)*)
 * ATTLIST INSTANCE
 *   %ClassName;
 *   xml:lang   NMTOKEN      #IMPLIED 
 * FIXME: Why INSTANCE has qualifiers? CIMInstance doesn't have!
 * FIXME: InstanceName and instance provides redundant information. Why?
 * </pre>
 */
public class ValueNamedInstanceNode extends AbstractScalarValueNode {

	// INSTANCENAME
	private CIMObjectPath iCIMInstPath;

	// INSTANCE
	private CIMInstance iCIMInstance;

	/**
	 * Ctor.
	 */
	public ValueNamedInstanceNode() {
		super(VALUE_NAMEDINSTANCE);
	}

	/**
	 * @param pAttribs
	 * @param pSession
	 */
	@Override
	public void init(Attributes pAttribs, SAXSession pSession) {
		this.iCIMInstPath = null;
		this.iCIMInstance = null;
		// no attribute
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
		if (pNodeNameEnum == INSTANCENAME) {
			if (this.iCIMInstPath != null) throw new SAXException(
					"VALUE.NAMEDINSTANCE node can have only one INSTANCENAME node, but another one was found!");
		} else if (pNodeNameEnum == INSTANCE) {
			if (this.iCIMInstance != null) throw new SAXException(
					"VALUE.NAMEDINSTANCE node can have only one INSTANCE node, but another one was found!");
		} else {
			throw new SAXException("VALUE.NAMEDINSTANCE node cannot have " + pNodeNameEnum
					+ " child node!");
		}
	}

	@Override
	public void childParsed(Node pChild) {
		if (pChild instanceof InstanceNameNode) {
			this.iCIMInstPath = ((InstanceNameNode) pChild).getCIMObjectPath();
		} else {
			this.iCIMInstance = ((InstanceNode) pChild).getCIMInstance();
		}
	}

	@Override
	public void testCompletness() throws SAXException {
		if (this.iCIMInstPath == null) throw new SAXException(
				"VALUE.NAMEDINSTANCE node must have an INSTANCENAME child node!");
		if (this.iCIMInstance == null) throw new SAXException(
				"VALUE.NAMEDINSTANCE node must have an INSTANCE child node!");
	}

	/**
	 * @see ValueIf#getValue()
	 * @return CIMInstance
	 */
	public Object getValue() {
		// CIMObjectPath op=iInstanceNameNode.getCIMObjectPath();
		/*
		 * INSTANCENAME contains the key properties only, INSTANCE contains the
		 * non-key properties too.
		 */
		if (WBEMConfiguration.getGlobalConfiguration().synchronizeNumericKeyDataTypes()) return CIMHelper
				.CIMInstanceWithSynchonizedNumericKeyDataTypes(this.iCIMInstPath, this.iCIMInstance
						.getProperties());
		return new CIMInstance(this.iCIMInstPath, this.iCIMInstance.getProperties());
	}

	public CIMDataType getType() {
		return CIMDataType.OBJECT_T;
	}

}
