/*
  (C) Copyright IBM Corp. 2009, 2013

  THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE
  ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE
  CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.

  You can obtain a current copy of the Eclipse Public License from
  http://www.opensource.org/licenses/eclipse-1.0.php

  @author : Ramandeep S Arora, IBM,  arorar@us.ibm.com
 * 
 * Flag       Date        Prog         Description
 * -------------------------------------------------------------------------------
 * 2845211    2009-08-27  raman_arora  Pull Enumeration Feature (SAX Parser)
 * 3598613    2013-01-11  blaschke-oss different data type in cim instance and cim object path
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
import org.sentrysoftware.wbem.javax.cim.CIMInstance;
import org.sentrysoftware.wbem.javax.cim.CIMObjectPath;

import org.sentrysoftware.wbem.sblim.cimclient.internal.cim.CIMHelper;
import org.sentrysoftware.wbem.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.sentrysoftware.wbem.sblim.cimclient.internal.util.WBEMConfiguration;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * <pre>
 * ELEMENT VALUE.INSTANCEWITHPATH (INSTANCEPATH, INSTANCE)
 * 
 * ELEMENT INSTANCEPATH (NAMESPACEPATH,INSTANCENAME)
 *    
 * ELEMENT INSTANCE (QUALIFIER*, (PROPERTY | PROPERTY.ARRAY | PROPERTY.REFERENCE)*)
 * ATTLIST INSTANCE
 *   %ClassName;
 *   xml:lang   NMTOKEN      #IMPLIED
 * </pre>
 */
public class ValueInstanceWithPathNode extends AbstractScalarValueNode {

	// INSTANCEPATH
	private CIMObjectPath iCIMInstPath;

	// INSTANCE
	private CIMInstance iCIMInstance;

	/**
	 * Ctor.
	 */
	public ValueInstanceWithPathNode() {
		super(VALUE_INSTANCEWITHPATH);
	}

	/**
	 * @param pAttribs
	 * @param pSession
	 */
	@Override
	public void init(Attributes pAttribs, SAXSession pSession) {
		// no attribute
		this.iCIMInstPath = null;
		this.iCIMInstance = null;
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
		if (pNodeNameEnum == INSTANCEPATH) {
			if (this.iCIMInstPath != null) throw new SAXException(
					"VALUE.INSTANCEWITHPATH node can have only one INSTANCEPATH node, but another one was found!");
		} else if (pNodeNameEnum == INSTANCE) {
			if (this.iCIMInstance != null) throw new SAXException(
					"VALUE.INSTANCEWITHPATH node can have only one INSTANCE node, but another one was found!");
		} else {
			throw new SAXException("VALUE.INSTANCEWITHPATH node cannot have " + pNodeNameEnum
					+ " child node!");
		}
	}

	@Override
	public void childParsed(Node pChild) {
		if (pChild instanceof InstancePathNode) {
			this.iCIMInstPath = ((InstancePathNode) pChild).getCIMObjectPath();
		} else {
			this.iCIMInstance = ((InstanceNode) pChild).getCIMInstance();
		}
	}

	@Override
	public void testCompletness() throws SAXException {
		if (this.iCIMInstPath == null) throw new SAXException(
				"VALUE.INSTANCEWITHPATH node must have an INSTANCEPATH child node!");
		if (this.iCIMInstance == null) throw new SAXException(
				"VALUE.INSTANCEWITHPATH node must have an INSTANCE child node!");
	}

	/**
	 * @see ValueIf#getValue()
	 * @return CIMInstance
	 */
	public Object getValue() {
		/*
		 * INSTANCENAME contains the key properties only, INSTANCE contains
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
