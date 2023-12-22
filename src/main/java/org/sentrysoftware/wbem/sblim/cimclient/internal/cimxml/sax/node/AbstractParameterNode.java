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
 * 1820763    2007-10-29  ebak         Supporting the EmbeddedInstance qualifier
 * 1848607    2007-12-11  ebak         Strict EmbeddedObject types
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2) 
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
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

import org.sentrysoftware.wbem.javax.cim.CIMParameter;

import org.sentrysoftware.wbem.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Class AbstractParameterNode is the superclass of ParameterArrayNode,
 * ParameterNode, ParameterRefArrayNode and ParameterReferenceNode classes.
 */
public abstract class AbstractParameterNode extends Node implements TypedIf {

	private String iName;

	protected QualifiedNodeHandler iQualiHandler;

	protected SAXSession iSession;

	/**
	 * Ctor.
	 * 
	 * @param pNameEnum
	 */
	public AbstractParameterNode(String pNameEnum) {
		super(pNameEnum);
	}

	@Override
	public void init(Attributes pAttribs, SAXSession pSession) throws SAXException {
		this.iSession = pSession;
		this.iQualiHandler = QualifiedNodeHandler.init(this.iQualiHandler);
		this.iName = getCIMName(pAttribs);
		specificInit(pAttribs);
	}

	protected abstract void specificInit(Attributes pAttribs) throws SAXException;

	@Override
	public void testChild(String pNodeNameEnum) throws SAXException {
		if (pNodeNameEnum != QUALIFIER) throw new SAXException(getNodeName() + " cannot have "
				+ pNodeNameEnum + " child node!");
	}

	@Override
	public void childParsed(Node pChild) {
		this.iQualiHandler.addQualifierNode(pChild);
	}

	/**
	 * @param pData
	 */
	@Override
	public void parseData(String pData) {
	// no data
	}

	/**
	 * getCIMParameter
	 * 
	 * @return CIMParameter
	 */
	public CIMParameter<Object> getCIMParameter() {
		/*
		 * CIMParameter(String name, CIMDataType type, CIMQualifier[]
		 * qualifiers)
		 * 
		 * Not dealing with embedded object is faster. Here we don't have to do
		 * anything with embedded object qualifier, since parameter doesn't have
		 * value.
		 * 
		 * Strict embObj type parsing requires different behavior.
		 */
		return new CIMParameter<Object>(this.iName, getType(), this.iQualiHandler
				.getQualis(!this.iSession.strictEmbObjParsing()));
	}

}
