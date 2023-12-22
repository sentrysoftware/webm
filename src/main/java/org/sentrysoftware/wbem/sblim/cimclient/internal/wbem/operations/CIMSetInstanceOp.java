/*
  CIMSetInstanceOp.java

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

import org.sentrysoftware.wbem.javax.cim.CIMInstance;
import org.sentrysoftware.wbem.javax.cim.CIMObjectPath;

/**
 * CIMSetInstanceOp
 * 
 */
public class CIMSetInstanceOp extends CIMOperation {

	protected CIMInstance iInstance;

	protected boolean iIncludeQualifiers;

	protected String[] iPropertyList;

	/**
	 * Ctor.
	 * 
	 * @param objectName
	 * @param instance
	 * @param includeQualifiers
	 * @param propertyList
	 */
	public CIMSetInstanceOp(CIMObjectPath objectName, CIMInstance instance,
			boolean includeQualifiers, String[] propertyList) {
		this.iMethodCall = "SetInstance";
		this.iObjectName = objectName;
		this.iInstance = instance;
		this.iIncludeQualifiers = includeQualifiers;
		this.iPropertyList = propertyList;
	}

	/**
	 * Returns includeQualifiers
	 * 
	 * @return The value of includeQualifiers.
	 */
	public boolean isIncludeQualifiers() {
		return this.iIncludeQualifiers;
	}

	/**
	 * Returns instance
	 * 
	 * @return The value of instance.
	 */
	public CIMInstance getInstance() {
		return this.iInstance;
	}

	/**
	 * Returns propertyList
	 * 
	 * @return The value of propertyList.
	 */
	public String[] getPropertyList() {
		return this.iPropertyList;
	}

}
