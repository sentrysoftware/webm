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
 * 1689085    2007-04-10  ebak         Embedded object enhancements for Pegasus
 * 1714878    2007-05-08  ebak         Empty string property values are parsed as null
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2) 
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
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

import org.sentrysoftware.wbem.javax.cim.CIMClassProperty;
import org.sentrysoftware.wbem.javax.cim.CIMProperty;
import org.sentrysoftware.wbem.javax.cim.CIMQualifier;

import org.sentrysoftware.wbem.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * AbstractPropertyNode is superclass of PropertyArrayNode, PropertyNode and
 * PropertyReferenceNode classes.
 */
public abstract class AbstractPropertyNode extends Node implements TypedIf, ValueIf {

	// common attributes
	private String iName;

	private String iClassOrigin;

	private boolean iPropagated;

	protected QualifiedNodeHandler iQualiHandler;

	/**
	 * Ctor.
	 * 
	 * @param pNameEnum
	 */
	public AbstractPropertyNode(String pNameEnum) {
		super(pNameEnum);
	}

	/**
	 * hasValue
	 * 
	 * @return true if it has a value child node
	 */
	protected abstract boolean hasValueNode();

	protected abstract void childValueNodeParsed(Node pChild) throws SAXException;

	protected abstract void specificInit(Attributes pAttribs, SAXSession pSession)
			throws SAXException;

	protected abstract String getChildValueNodeNameEnum();

	@Override
	public void init(Attributes pAttribs, SAXSession pSession) throws SAXException {
		this.iQualiHandler = QualifiedNodeHandler.init(this.iQualiHandler);
		this.iName = getCIMName(pAttribs);
		this.iClassOrigin = getClassOrigin(pAttribs);
		this.iPropagated = getPropagated(pAttribs);
		specificInit(pAttribs, pSession);
	}

	@Override
	public void testChild(String pNodeNameEnum) throws SAXException {
		String valueNodeNameEnum = getChildValueNodeNameEnum();
		if (pNodeNameEnum == valueNodeNameEnum) {
			if (hasValueNode()) throw new SAXException(getNodeName() + " node can have only one "
					+ valueNodeNameEnum + " child node!");
		} else if (pNodeNameEnum != QUALIFIER) throw new SAXException(getNodeName()
				+ " node cannot have " + pNodeNameEnum + " child node!");
	}

	/**
	 * @param pData
	 */
	@Override
	public void parseData(String pData) {
	// no data
	}

	@Override
	public void childParsed(Node pChild) throws SAXException {
		if (!this.iQualiHandler.addQualifierNode(pChild)) {
			childValueNodeParsed(pChild);
		}
	}

	protected CIMQualifier<?>[] getQualis() {
		// not dealing with embedded object qualifier is faster
		return this.iQualiHandler.getQualis(true);
	}

	/**
	 * getCIMProperty
	 * 
	 * @return CIMProperty
	 */
	public CIMProperty<Object> getCIMProperty() {
		/*
		 * CIMProperty( String name, CIMDataType type, Object value, boolean
		 * key, boolean propagated, String originClass )
		 */
		return new CIMProperty<Object>(this.iName, getType(), getValue(), this.iQualiHandler
				.isKeyed(), this.iPropagated, this.iClassOrigin);
	}

	/**
	 * getCIMClassProperty
	 * 
	 * @return CIMClassProperty
	 */
	public CIMClassProperty<Object> getCIMClassProperty() {
		/*
		 * CIMClassProperty( String pName, CIMDataType pType, Object pValue,
		 * CIMQualifier[] pQualifiers, boolean pKey, boolean propagated, String
		 * originClass) );
		 */
		return new CIMClassProperty<Object>(this.iName, getType(), getValue(), getQualis(),
				this.iQualiHandler.isKeyed(), this.iPropagated, this.iClassOrigin);
	}

}
