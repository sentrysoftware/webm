/*
  (C) Copyright IBM Corp. 2006, 2010

  THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE
  ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE
  CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.

  You can obtain a current copy of the Eclipse Public License from
  http://www.opensource.org/licenses/eclipse-1.0.php

  @author : Endre Bak, ebak@de.ibm.com
 * 
 * Flag       Date        Prog         Description
 * -------------------------------------------------------------------------------
 * 1565892    2006-10-06  ebak         Make SBLIM client JSR48 compliant
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 * 2935258    2010-01-22  blaschke-oss Sync up javax.cim.* javadoc with JSR48 1.0.0
 */

package org.sentrysoftware.wbem.javax.cim;

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

//Sync'd against JSR48 1.0.0 javadoc (build 1.5.0_10) on Wed Jan 20 02:20:59 EST 2010
/**
 * The <code>CIMQualifiedElementInterface</code> is used by CIM Elements that
 * have qualifiers. For example, CIM Classes, CIM Properties, CIM Methods and
 * CIM Parameters are all CIM Elements that have qualifiers.
 */
public interface CIMQualifiedElementInterface {

	/**
	 * Get a qualifier by index.
	 * 
	 * @param pIndex
	 *            The index of the qualifier.
	 * @return The Qualifier at index <code>pIndex</code>.
	 */
	public CIMQualifier<?> getQualifier(int pIndex);

	/**
	 * Gets a qualifier by name.
	 * 
	 * @param pName
	 *            The name of the qualifier to get.
	 * @return <code>null</code> if the qualifier does not exist, otherwise
	 *         returns the reference to the qualifier.
	 */
	public CIMQualifier<?> getQualifier(String pName);

	/**
	 * Get the number of qualifiers defined for this CIM Element.
	 * 
	 * @return The number of qualifiers.
	 */
	public int getQualifierCount();

	/**
	 * Returns the list of qualifiers for this class.
	 * 
	 * @return Qualifiers for this class.
	 */
	public CIMQualifier<?>[] getQualifiers();

	/**
	 * Gets a qualifier value by name.
	 * 
	 * @param pName
	 *            The name of the qualifier to get.
	 * @return <code>null</code> if the qualifier does not exist or value is
	 *         <code>null</code>, otherwise returns the reference to the
	 *         qualifier.
	 */
	public Object getQualifierValue(String pName);

	/**
	 * Checks whether the specified qualifier is one of the qualifiers in this
	 * CIM element.
	 * 
	 * @param pName
	 *            The name of the qualifier.
	 * @return <code>true</code> if the qualifier exists in this CIM element,
	 *         otherwise <code>false</code>.
	 */
	public boolean hasQualifier(String pName);

	/**
	 * Checks whether the specified qualifier is one of the qualifiers defined
	 * for this property with the specified value. This method will return
	 * <code>false</code> if the qualifier is not applied or if the value does
	 * not match.
	 * 
	 * @param pName
	 *            The name of the qualifier.
	 * @param pValue
	 *            The value to be tested.
	 * @return <code>true</code> if the qualifier exists and has the value,
	 *         otherwise <code>false</code>.
	 */
	public boolean hasQualifierValue(String pName, Object pValue);

}
