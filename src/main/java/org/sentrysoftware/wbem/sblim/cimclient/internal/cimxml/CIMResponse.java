/*
  CIMResponse.java

  (C) Copyright IBM Corp. 2005, 2012

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
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 * 3525135    2012-05-09  blaschke-oss Remove CIMResponse.isSuccessul
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

import java.util.List;
import java.util.Vector;

import org.sentrysoftware.wbem.javax.wbem.WBEMException;

/**
 * Represent a CIMReponse message.
 */
public class CIMResponse extends CIMMessage {

	protected Vector<CIMResponse> iResponses = new Vector<CIMResponse>(0);

	protected WBEMException iError = null;

	protected Vector<Object> iReturnValue = new Vector<Object>();

	protected Vector<Object> iParamValue = new Vector<Object>(0);

	/**
	 * Constructs a CIMResponse object.
	 * 
	 */
	public CIMResponse() { /**/}

	/**
	 * Constructs a CIMResponse object with the specified CIMVersion, DTDVersion
	 * and method.
	 * 
	 * @param pCimVersion
	 * @param pDtdVersion
	 * @param pId
	 * @param pMethod
	 */
	public CIMResponse(String pCimVersion, String pDtdVersion, String pId, String pMethod) {
		super(pCimVersion, pDtdVersion, pId, pMethod);
	}

	/**
	 * Constructs a CIM Response message from a given CIM Request.
	 * 
	 * @param request
	 */
	public CIMResponse(CIMRequest request) {
		super();
		this.iCimVersion = request.getCIMVersion();
		this.iDtdVersion = request.getDTDVersion();
	}

	/**
	 * addParamValue
	 * 
	 * @param o
	 */
	public void addParamValue(Object o) {
		this.iParamValue.add(o);
	}

	/**
	 * addParamValue
	 * 
	 * @param v
	 */
	public void addParamValue(Vector<Object> v) {
		this.iParamValue.addAll(v);
	}

	/**
	 * addResponse
	 * 
	 * @param response
	 */
	public void addResponse(CIMResponse response) {
		this.iResponses.add(response);
	}

	/**
	 * addReturnValue
	 * 
	 * @param o
	 */
	public void addReturnValue(Object o) {
		this.iReturnValue.add(o);
	}

	/**
	 * Verify the status code for this CIMResponse.
	 * 
	 * @throws WBEMException
	 *             if the status code is other than success.
	 */
	public void checkError() throws WBEMException {
		if (this.iError != null) throw this.iError;
	}

	/**
	 * getAllResponses
	 * 
	 * @return List
	 */
	public List<CIMResponse> getAllResponses() {
		return this.iResponses;
	}

	/**
	 * getException
	 * 
	 * @return WBEMException
	 */
	public WBEMException getException() {
		return this.iError;
	}

	/**
	 * isSuccessful
	 * 
	 * @return boolean
	 */
	public boolean isSuccessful() {
		return this.iError == null;
	}

	/**
	 * getFirstResponse
	 * 
	 * @return CIMResponse
	 */
	public CIMResponse getFirstResponse() {
		if (this.iResponses != null && this.iResponses.size() > 0) return this.iResponses
				.elementAt(0);
		return null;
	}

	/**
	 * getParamValues
	 * 
	 * @return List
	 */
	public List<Object> getParamValues() {
		return this.iParamValue;
	}

	/**
	 * getFirstReturnValue
	 * 
	 * @return List
	 */
	public List<Object> getFirstReturnValue() {
		return this.iReturnValue;
	}

	/**
	 * setError
	 * 
	 * @param error
	 */
	public void setError(WBEMException error) {
		this.iError = error;
	}

	/**
	 * setParamValue
	 * 
	 * @param paramValue
	 */
	public void setParamValue(Vector<Object> paramValue) {
		this.iParamValue = paramValue;
	}

	/**
	 * setReturnValue
	 * 
	 * @param returnValue
	 */
	public void setReturnValue(Vector<Object> returnValue) {
		this.iReturnValue = returnValue;
	}

}
