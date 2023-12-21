/*
  CIMInvokeMethodOp.java

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
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 */

package org.sentrysoftware.wbem.sblim.cimclient.internal.wbem.operations;

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

import org.sentrysoftware.wbem.javax.cim.CIMArgument;
import org.sentrysoftware.wbem.javax.cim.CIMObjectPath;

/**
 * CIMInvokeMethodOp
 * 
 */
public class CIMInvokeMethodOp extends CIMOperation {

	protected String iMethodName;

	protected CIMArgument<?>[] iInParams;

	protected CIMArgument<?>[] iOutParams;

	/**
	 * Ctor.
	 * 
	 * @param pObjectName
	 * @param pMethodName
	 * @param pInParams
	 * @param pOutParams
	 */
	public CIMInvokeMethodOp(CIMObjectPath pObjectName, String pMethodName,
			CIMArgument<?>[] pInParams, CIMArgument<?>[] pOutParams) {
		this.iMethodCall = "InvokeMethod";
		this.iObjectName = pObjectName;
		this.iMethodName = pMethodName;
		this.iInParams = pInParams;
		this.iOutParams = pOutParams;
	}

	/**
	 * Returns inParameters
	 * 
	 * @return The value of inParameters.
	 */
	public CIMArgument<?>[] getInParams() {
		return this.iInParams;
	}

	/**
	 * Returns methodName
	 * 
	 * @return The value of methodName.
	 */
	public String getMethodName() {
		return this.iMethodName;
	}

	/**
	 * Returns outParameters
	 * 
	 * @return The value of outParameters.
	 */
	public CIMArgument<?>[] getOutParams() {
		return this.iOutParams;
	}

}
