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
 * 1565892    2006-10-10  ebak         Make SBLIM client JSR48 compliant
 * 1660756    2007-02-22  ebak         Embedded object support
 * 1737141    2007-06-18  ebak         Sync up with JSR48 evolution
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2) 
 */

package org.sentrysoftware.wbem.sblim.cimclient.internal.cim;

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

import java.util.Arrays;
import java.util.Comparator;

import org.sentrysoftware.wbem.javax.cim.CIMElement;

/**
 * Class CIMElementSorter can sort CIMElement arrays and can do binary search by
 * name in them.
 */
public class CIMElementSorter implements Comparator<Object> {

	private static final Comparator<Object> COMPARATOR = new CIMElementSorter();

	/**
	 * Sorts the passed CIMElement array, the passed array is not copied.
	 * 
	 * @param pArray
	 *            the array which will be sorted if it's not null
	 * @return pArray
	 */
	public static CIMElement[] sort(CIMElement[] pArray) {
		if (pArray == null || pArray.length == 0) return null;
		synchronized (pArray) {
			Arrays.sort(pArray /* , COMPARATOR */);
		}
		return pArray;
	}

	/**
	 * Finds CIMElement, named pName, in pArray which must be a sorted array of
	 * CIMElements.
	 * 
	 * @param pArray
	 * @param pName
	 * @return the CIMElement if found, otherwise null
	 */
	public static CIMElement find(CIMElement[] pArray, String pName) {
		if (pArray == null) return null;
		int idx;
		synchronized (pArray) {
			idx = Arrays.binarySearch(pArray, pName, COMPARATOR);
		}
		if (idx < 0) return null;
		return pArray[idx];
	}

	/**
	 * Finds the index of CIMElement, named pName, in pArray which must be a
	 * sorted array of CIMElements.
	 * 
	 * @param pArray
	 * @param pName
	 * @return the index, just like in case of Arrays.binarySearch()
	 * @see java.util.Arrays#binarySearch(Object[], Object, Comparator)
	 */
	public static int findIdx(CIMElement[] pArray, String pName) {
		if (pArray == null) return -1;
		synchronized (pArray) {
			return Arrays.binarySearch(pArray, pName, COMPARATOR);
		}
	}

	/**
	 * @see java.util.Comparator#compare(Object, Object)
	 */
	public int compare(Object pObj0, Object pObj1) {
		// ebak: handling null objects
		if (pObj0 == null) return pObj1 == null ? 0 : 1;
		if (pObj1 == null) return -1;
		String name0 = pObj0 instanceof CIMElement ? ((CIMElement) pObj0).getName()
				: (String) pObj0;
		String name1 = pObj1 instanceof CIMElement ? ((CIMElement) pObj1).getName()
				: (String) pObj1;
		return name0.compareToIgnoreCase(name1);
	}

}
