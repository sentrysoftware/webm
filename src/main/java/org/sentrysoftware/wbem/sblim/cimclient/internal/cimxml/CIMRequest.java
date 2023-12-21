/*
  CIMRequest.java

  (C) Copyright IBM Corp. 2005, 2009

  THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE
  ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE
  CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.

  You can obtain a current copy of the Eclipse Public License from
  http://www.opensource.org/licenses/eclipse-1.0.php

  @author : Roberto Pineiro, IBM, roberto.pineiro@us.ibm.com
 * @author : Chung-hao Tan, IBM, chungtan@us.ibm.com
 * 
 * 
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 1535756    2006-08-07  lupusalex    Make code warning free
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2) 
 */

package org.sentrysoftware.wbem.sblim.cimclient.internal.cimxml;

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

import java.util.Vector;

import org.sentrysoftware.wbem.javax.cim.CIMObjectPath;

/**
 * Class CIMRequest is used by the CIM-XML DOM parser.
 * 
 */
public class CIMRequest extends CIMMessage {

	protected Vector<CIMRequest> iRequests = new Vector<CIMRequest>(0);

	protected Vector<Object> iParamValue = new Vector<Object>(0);

	protected String iMethodName;

	protected CIMObjectPath iPath;

	protected String iNamespace;

	/**
	 * Ctor.
	 */
	public CIMRequest() { /**/}

	/**
	 * Ctor.
	 * 
	 * @param pCimVersion
	 * @param pDtdVersion
	 * @param pId
	 * @param pMethod
	 */
	public CIMRequest(String pCimVersion, String pDtdVersion, String pId, String pMethod) {
		super(pCimVersion, pDtdVersion, pId, pMethod);
	}

	/**
	 * addParamValue
	 * 
	 * @param v
	 */
	public void addParamValue(Object v) {
		if (v instanceof Vector) this.iParamValue.addAll((Vector<?>) v);
		else this.iParamValue.add(v);
	}

	/**
	 * addRequest
	 * 
	 * @param request
	 */
	public void addRequest(CIMRequest request) {
		this.iRequests.add(request);
	}

	/**
	 * getMethodName
	 * 
	 * @return String
	 */
	public String getMethodName() {
		return this.iMethodName;
	}

	/**
	 * getNameSpace
	 * 
	 * @return String
	 */
	public String getNameSpace() {
		return this.iNamespace;
	}

	/**
	 * getObjectPath
	 * 
	 * @return String
	 */
	public CIMObjectPath getObjectPath() {
		return this.iPath;
	}

	/**
	 * getParamValue
	 * 
	 * @return String
	 */
	public Vector<Object> getParamValue() {
		return this.iParamValue;
	}

	/**
	 * setMethodName
	 * 
	 * @param methodName
	 */
	public void setMethodName(String methodName) {
		this.iMethodName = methodName;
	}

	/**
	 * setNameSpace
	 * 
	 * @param namespace
	 */
	public void setNameSpace(String namespace) {
		this.iNamespace = namespace;
	}

	/**
	 * setObjectPath
	 * 
	 * @param path
	 */
	public void setObjectPath(CIMObjectPath path) {
		this.iPath = path;
	}
}
