/*
  (C) Copyright IBM Corp. 2006, 2012

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
 * 1783288    2007-09-10  ebak         CIMClass.isAssociation() not working for retrieved classes.
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 * 3500619    2012-03-16  blaschke-oss JSR48 1.0.0: CIMClass association/key clean up
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

import java.util.ArrayList;

import org.sentrysoftware.wbem.javax.cim.CIMClass;
import org.sentrysoftware.wbem.javax.cim.CIMClassProperty;
import org.sentrysoftware.wbem.javax.cim.CIMMethod;
import org.sentrysoftware.wbem.javax.cim.CIMObjectPath;

import org.sentrysoftware.wbem.sblim.cimclient.GenericExts;
import org.sentrysoftware.wbem.sblim.cimclient.internal.cimxml.LocalPathBuilder;
import org.sentrysoftware.wbem.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * <pre>
 * ELEMENT CLASS (QUALIFIER*, (PROPERTY | PROPERTY.ARRAY | PROPERTY.REFERENCE)*, METHOD*)
 * ATTLIST CLASS
 * %CIMName; 
 * %SuperClass;
 * </pre>
 */
public class ClassNode extends AbstractObjectNode {

	private String iName;

	private String iSuperClass;

	private QualifiedNodeHandler iQualiHandler;

	private ArrayList<CIMClassProperty<?>> iCIMClassPropAL;

	private boolean iKeyed;

	private ArrayList<CIMMethod<?>> iCIMMethodAL;

	/**
	 * Ctor.
	 */
	public ClassNode() {
		super(CLASS);
	}

	@Override
	public void init(Attributes pAttribs, SAXSession pSession) throws SAXException {
		this.iLocalPath = pSession.getDefLocalPath();
		this.iQualiHandler = QualifiedNodeHandler.init(this.iQualiHandler);
		this.iCIMClassPropAL = GenericExts.initClearArrayList(this.iCIMClassPropAL);
		this.iKeyed = false;
		this.iCIMMethodAL = GenericExts.initClearArrayList(this.iCIMMethodAL);
		this.iName = getCIMName(pAttribs);
		this.iSuperClass = pAttribs.getValue("SUPERCLASS"); // not mandatory
	}

	/**
	 * @param pData
	 */
	@Override
	public void parseData(String pData) {
	// no data
	}

	private static final String[] ALLOWED_CHILDREN = { QUALIFIER, PROPERTY, PROPERTY_ARRAY,
			PROPERTY_REFERENCE, METHOD };

	@Override
	public void testChild(String pNodeNameEnum) throws SAXException {
		for (int i = 0; i < ALLOWED_CHILDREN.length; i++)
			if (ALLOWED_CHILDREN[i] == pNodeNameEnum) return;
		throw new SAXException(getNodeName() + " node cannot have " + pNodeNameEnum
				+ " child node!");
	}

	@Override
	public void childParsed(Node pChild) {
		if (this.iQualiHandler.addQualifierNode(pChild)) return;
		if (pChild instanceof AbstractPropertyNode) {
			if (this.iCIMClassPropAL == null) this.iCIMClassPropAL = new ArrayList<CIMClassProperty<?>>();
			CIMClassProperty<Object> prop = ((AbstractPropertyNode) pChild).getCIMClassProperty();
			if (prop.isKey()) this.iKeyed = true;
			this.iCIMClassPropAL.add(prop);
		} else {
			if (this.iCIMMethodAL == null) this.iCIMMethodAL = new ArrayList<CIMMethod<?>>();
			this.iCIMMethodAL.add(((MethodNode) pChild).getCIMMethod());
		}
	}

	@Override
	public void testCompletness() {
	// all child nodes are optional
	}

	/**
	 * getCIMClass
	 * 
	 * @return CIMClass
	 */
	public CIMClass getCIMClass() {
		/*
		 * ebak: this constructor can add localPath info to the class CIMClass(
		 * CIMObjectPath path, String superclass, CIMQualifier[] qualifiers,
		 * CIMClassProperty[] props, CIMMethod[] pMethods, boolean
		 * pIsAssociation, boolean pIsKeyed )
		 */
		return new CIMClass(LocalPathBuilder.build(this.iLocalPath, this.iName, null),
				this.iSuperClass, this.iQualiHandler.getQualis(), this.iCIMClassPropAL
						.toArray(EMPTY_PA), this.iCIMMethodAL.toArray(EMPTY_MA), this.iQualiHandler
						.isAssociation(), this.iKeyed);
	}

	private static final CIMMethod<?>[] EMPTY_MA = new CIMMethod[0];

	private static final CIMClassProperty<?>[] EMPTY_PA = new CIMClassProperty[0];

	/**
	 * getCIMClass
	 * 
	 * @param pObjPath
	 * @return CIMClass with the provided object path
	 */
	public CIMClass getCIMClass(CIMObjectPath pObjPath) {
		return new CIMClass(pObjPath, this.iSuperClass, this.iQualiHandler.getQualis(),
				this.iCIMClassPropAL.toArray(EMPTY_PA), this.iCIMMethodAL.toArray(EMPTY_MA),
				this.iQualiHandler.isAssociation(), this.iKeyed);
	}

	/**
	 * @see ValueIf#getValue()
	 * @return CIMClass
	 */
	public Object getValue() {
		return getCIMClass();
	}

}
