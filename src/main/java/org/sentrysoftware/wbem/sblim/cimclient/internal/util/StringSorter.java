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
 * 1565892    2006-10-17  ebak         Make SBLIM client JSR48 compliant
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2) 
 */

package org.sentrysoftware.wbem.sblim.cimclient.internal.util;

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

import java.util.Arrays;
import java.util.Comparator;

/**
 * Class StringSorter is responsible for non case sensitive sorting and binary
 * searching of String arrays.
 */
public class StringSorter implements Comparator<Object> {

	private static final Comparator<Object> COMPARATOR = new StringSorter();

	/**
	 * Sorts non case sensitively the passed String array, the passed array is
	 * not copied.
	 * 
	 * @param pArray
	 *            the array which will be sorted if it's not null
	 * @return pArray
	 */
	public static String[] sort(String[] pArray) {
		if (pArray == null) return null;
		synchronized (pArray) {
			Arrays.sort(pArray, COMPARATOR);
		}
		return pArray;
	}

	/**
	 * Finds pName, in pArray which must be a non case sensitive sorted array of
	 * Strings.
	 * 
	 * @param pArray
	 * @param pName
	 * @return <code>true</code> if found, otherwise <code>false</code>
	 */
	public static boolean find(String[] pArray, String pName) {
		if (pArray == null) return false;
		synchronized (pArray) {
			int idx = Arrays.binarySearch(pArray, pName, COMPARATOR);
			return idx >= 0;
		}
	}

	/**
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object pO1, Object pO2) {
		String str1 = (String) pO1;
		String str2 = (String) pO2;
		return str1.compareToIgnoreCase(str2);
	}

}
