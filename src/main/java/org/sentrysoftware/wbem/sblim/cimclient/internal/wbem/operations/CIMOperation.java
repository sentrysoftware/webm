/*
  CIMOperation.java

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
 */

package org.sentrysoftware.wbem.sblim.cimclient.internal.wbem.operations;

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

import org.sentrysoftware.wbem.javax.cim.CIMObjectPath;
import org.sentrysoftware.wbem.javax.wbem.WBEMException;

/**
 * CIMOperation
 * 
 */
public abstract class CIMOperation {

	protected CIMObjectPath iObjectName;

	protected String iNameSpace;

	protected String iMethodCall;

	protected Object iResult;

	/**
	 * Returns the object name
	 * 
	 * @return The object name
	 */
	public CIMObjectPath getObjectName() {
		return this.iObjectName;
	}

	/**
	 * Returns the namespace
	 * 
	 * @return The namespace
	 */
	public String getNameSpace() {
		return this.iNameSpace;
	}

	/**
	 * Sets the namespace
	 * 
	 * @param pNamespace
	 *            The namespace
	 */
	public void setNameSpace(String pNamespace) {
		this.iNameSpace = pNamespace;
	}

	/**
	 * Returns the method call
	 * 
	 * @return The method call
	 */
	public String getMethodCall() {
		return this.iMethodCall;
	}

	/**
	 * Returns if an (uncaught) exception occurred
	 * 
	 * @return <code>true</code> if an (uncaught) exception occurred,
	 *         <code>false</code> otherwise
	 */
	public boolean isException() {
		return (this.iResult instanceof Exception);
	}

	/**
	 * Returns the result of the operation
	 * 
	 * @return The result
	 * @throws WBEMException
	 */
	public Object getResult() throws WBEMException {
		if (this.iResult instanceof WBEMException) throw (WBEMException) this.iResult;
		return this.iResult;
	}

	/**
	 * Sets the operation result
	 * 
	 * @param pResult
	 *            The result
	 */
	public void setResult(Object pResult) {
		this.iResult = pResult;
	}
}
