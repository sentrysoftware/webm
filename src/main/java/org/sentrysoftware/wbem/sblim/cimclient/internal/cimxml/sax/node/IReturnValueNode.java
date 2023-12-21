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
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2845211    2009-08-27  raman_arora  Pull Enumeration Feature (SAX Parser)
 * 3511454    2012-03-27  blaschke-oss SAX nodes not reinitialized properly
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

import java.util.LinkedList;

import org.sentrysoftware.wbem.sblim.cimclient.internal.cimxml.sax.SAXSession;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * <pre>
 * 
 * ELEMENT IRETURNVALUE (
 *   CLASSNAME* | INSTANCENAME* | VALUE* | VALUE.OBJECTWITHPATH* | VALUE.OBJECTWITHLOCALPATH* |
 *   VALUE.OBJECT* | OBJECTPATH* | QUALIFIER.DECLARATION* | VALUE.ARRAY? | VALUE.REFERENCE? |
 *   CLASS* | INSTANCE* | VALUE.NAMEDINSTANCE*
 * )
 * </pre>
 */
public class IReturnValueNode extends Node implements RetValPipeIf, NonVolatileIf {

	private String iChildNameEnum;

	private LinkedList<Object> iChildValueLL;

	/**
	 * Ctor.
	 */
	public IReturnValueNode() {
		super(IRETURNVALUE);
	}

	/**
	 * @param pChild
	 */
	public void addChild(Node pChild) { /* we don't need it here */}

	/**
	 * @param pAttribs
	 * @param pSession
	 */
	@Override
	public void init(Attributes pAttribs, SAXSession pSession) {
		// currently it's not really necessary here, since the Node is
		// NonVolatile
		this.iChildNameEnum = null;
		this.iChildValueLL = null;
		// no attribs
	}

	/**
	 * @param pData
	 */
	@Override
	public void parseData(String pData) {
	// no data
	}

	private static final String[] ALLOWED_CHILDREN = { CLASSNAME, INSTANCENAME, INSTANCEPATH,
			VALUE, VALUE_OBJECTWITHPATH, VALUE_OBJECTWITHLOCALPATH, VALUE_OBJECT, OBJECTPATH,
			QUALIFIER_DECLARATION, VALUE_ARRAY, VALUE_REFERENCE, CLASS, INSTANCE,
			VALUE_NAMEDINSTANCE, VALUE_INSTANCEWITHPATH };

	// private static final String[] SINGLE_CHILDREN = { VALUE_ARRAY,
	// VALUE_REFERENCE };

	/**
	 * @see Node#testChild(java.lang.String)
	 */
	@Override
	public void testChild(String pNodeNameEnum) throws SAXException {
		if (this.iChildNameEnum != null) {
			if (pNodeNameEnum != this.iChildNameEnum) throw new SAXException(getNodeName()
					+ " node cannot have " + pNodeNameEnum + " child node, since it already has "
					+ this.iChildNameEnum + " child node(s)!");
			if (this.iChildNameEnum == VALUE_ARRAY || this.iChildNameEnum == VALUE_REFERENCE) throw new SAXException(
					getNodeName() + " node can have only one " + this.iChildNameEnum
							+ " child node!");
		} else {
			boolean found = false;
			for (int i = 0; i < ALLOWED_CHILDREN.length; i++) {
				if ((found = (pNodeNameEnum == ALLOWED_CHILDREN[i])) == true) break;
			}
			if (!found) throw new SAXException(getNodeName() + " node cannot have " + pNodeNameEnum
					+ " child node!");
		}
	}

	@Override
	public void childParsed(Node pChild) {
		if (this.iChildValueLL == null) {
			this.iChildNameEnum = pChild.getNodeName();
			this.iChildValueLL = new LinkedList<Object>();
		}
		this.iChildValueLL.add(((ValueIf) pChild).getValue());
	}

	@Override
	public void testCompletness() {
	// child nodes are optional
	}

	public int getReturnValueCount() {
		return this.iChildValueLL == null ? 0 : this.iChildValueLL.size();
	}

	public Object readReturnValue() {
		return this.iChildValueLL == null ? null : this.iChildValueLL.removeFirst();
	}

}
