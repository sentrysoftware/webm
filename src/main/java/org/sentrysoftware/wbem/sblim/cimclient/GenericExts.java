/*
  (C) Copyright IBM Corp. 2009

  THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE
  ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE
  CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.

  You can obtain a current copy of the Eclipse Public License from
  http://www.opensource.org/licenses/eclipse-1.0.php

  @author : Ramandeep Arora, arorar@us.ibm.com
 * 
 * Flag       Date        Prog         Description
 * -------------------------------------------------------------------------------
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2) 
 * 2797696    2009-05-27  raman_arora  Input files use unsafe operations
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 */

package org.sentrysoftware.wbem.sblim.cimclient;

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

import java.util.ArrayList;
import java.util.Vector;

/**
 * Class GenericExts is responsible for generic initialization
 */
public class GenericExts {

	/**
	 * initArrayList : If arrayList is null then it will return the new
	 * arrayList of same type if it is not null then it will clear the arrayList
	 * 
	 * @param <T>
	 *            : Type Parameter
	 * 
	 * @param pAL
	 *            : ArrayList to be initialized
	 * @return ArrayList : initialized ArrayList
	 */
	public static <T> ArrayList<T> initClearArrayList(ArrayList<T> pAL) {
		if (pAL == null) return new ArrayList<T>();
		pAL.clear();
		return pAL;
	}

	/**
	 * initArrayList : If arrayList is null then it will return the new
	 * arrayList of same type if it is not null then it will return the same
	 * arrayList
	 * 
	 * @param <T>
	 *            : Type Parameter
	 * 
	 * @param pAL
	 *            : ArrayList to be initialized
	 * @return ArrayList : initialized ArrayList
	 */
	public static <T> ArrayList<T> initArrayList(ArrayList<T> pAL) {
		if (pAL == null) return new ArrayList<T>();
		return pAL;
	}

	/**
	 * cloneVector : Generic deep copy of the vector. If original vector is null
	 * then return value will also be null.
	 * 
	 * @param <T>
	 *            : Type of vector
	 * 
	 * @param oldVec
	 *            : The original vector.
	 * 
	 * @return Vector&lt;T&gt; : Deep copy of original vector.
	 */
	public static synchronized <T> Vector<T> cloneVector(Vector<T> oldVec) {
		if (oldVec == null) return null;
		Vector<T> newVec = new Vector<T>(oldVec.size());
		for (T obj : oldVec)
			newVec.add(obj);
		return newVec;
	}
}
