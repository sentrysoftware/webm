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
 * 3511454    2012-03-27  blaschke-oss SAX nodes not reinitialized properly
 *    2604    2013-07-01  blaschke-oss SAXException messages should contain node name
 *    2708    2013-11-12  blaschke-oss CIMNode quietly ignores DECLARATION child
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

import org.sentrysoftware.wbem.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * <pre>
 * ELEMENT CIM (MESSAGE | DECLARATION)
 * ATTLIST CIM
 * 	CIMVERSION CDATA #REQUIRED
 * DTDVERSION CDATA #REQUIRED
 * </pre>
 */
public class CIMNode extends Node implements NonVolatileIf {

	/**
	 * Ctor.
	 */
	public CIMNode() {
		super(CIM);
	}

	private Node iContent;

	private String iCimVersion, iDtdVersion;

	public void addChild(Node pChild) {
		this.iContent = pChild;
	}

	/**
	 * @param pSession
	 */
	@Override
	public void init(Attributes pAttribs, SAXSession pSession) throws SAXException {
		this.iCimVersion = pAttribs.getValue("CIMVERSION");
		if (this.iCimVersion == null) { throw new SAXException(
				"CIMVERSION attribute is mandatory for " + getNodeName() + " node!"); }
		this.iDtdVersion = pAttribs.getValue("DTDVERSION");
		if (this.iDtdVersion == null) { throw new SAXException(
				"DTDVERSION attribute is mandatory for " + getNodeName() + " node!"); }
		this.iContent = null;
	}

	/**
	 * @param pData
	 */
	@Override
	public void parseData(String pData) {
		return;
	}

	@Override
	public void testChild(String pNodeNameEnum) throws SAXException {
		if (this.iContent != null) {
			String msg = "CIM node also has a " + this.iContent.getNodeName() + " child node!";
			throw new SAXException(msg);
		}
		if (pNodeNameEnum == MESSAGE) return;
		String msg = (pNodeNameEnum == DECLARATION) ? "DECLARATION child node not supported by CIM node!"
				: pNodeNameEnum + " cannot be a child node of CIM node!";
		throw new SAXException(msg);
	}

	@Override
	public void testCompletness() throws SAXException {
		if (this.iContent == null) throw new SAXException(
				"CIM node must have a MESSAGE or a DECLARATION child!");
	}

	/**
	 * @param pChild
	 */
	@Override
	public void childParsed(Node pChild) {
	// nothing to do here
	}

	/**
	 * getCimVersion
	 * 
	 * @return String
	 */
	public String getCimVersion() {
		return this.iCimVersion;
	}

	/**
	 * getDtdVersion
	 * 
	 * @return String
	 */
	public String getDtdVersion() {
		return this.iDtdVersion;
	}

	/**
	 * getMessageNode
	 * 
	 * @return MessageNode or null
	 */
	public MessageNode getMessageNode() {
		return (this.iContent instanceof MessageNode) ? (MessageNode) this.iContent : null;
	}

	// not implemented yet
	// public DeclarationNode getDeclarationNode() {}

}
