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
 * 1565892    2006-12-04  ebak         Make SBLIM client JSR48 compliant
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 */

package org.sentrysoftware.wbem.sblim.cimclient.internal.cimxml;

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
import org.sentrysoftware.wbem.javax.cim.CIMProperty;

/**
 * Class LocalPathBuilder helps CIM-XML parsers to build local CIMObjectPathes.
 */
public class LocalPathBuilder {

	private CIMObjectPath iBasePath;

	/**
	 * Ctor.
	 * 
	 * @param pBasePath
	 */
	public LocalPathBuilder(CIMObjectPath pBasePath) {
		this.iBasePath = pBasePath;
	}

	/**
	 * getBasePath
	 * 
	 * @return CIMObjectPath
	 */
	public CIMObjectPath getBasePath() {
		return this.iBasePath;
	}

	/**
	 * build
	 * 
	 * @param pObjPathStr
	 * @return CIMObjectPath
	 */
	public CIMObjectPath build(String pObjPathStr) {
		return build(this.iBasePath, pObjPathStr);
	}

	/**
	 * build
	 * 
	 * @param pObjName
	 * @param pNameSpace
	 * @return CIMObjectPath
	 */
	public CIMObjectPath build(String pObjName, String pNameSpace) {
		return build(this.iBasePath, pObjName, pNameSpace);
	}

	/**
	 * build
	 * 
	 * @param pObjName
	 * @param pNameSpace
	 * @param pKeys
	 * @return CIMObjectPath
	 */
	public CIMObjectPath build(String pObjName, String pNameSpace, CIMProperty<?>[] pKeys) {
		return build(this.iBasePath, pObjName, pNameSpace, pKeys);
	}

	/**
	 * build
	 * 
	 * @param pHost
	 * @param pNameSpace
	 * @param pObjName
	 * @param pKeys
	 * @return CIMObjectPath
	 */
	public CIMObjectPath build(String pHost, String pNameSpace, String pObjName,
			CIMProperty<?>[] pKeys) {
		return build(this.iBasePath, pHost, pNameSpace, pObjName, pKeys);
	}

	/**
	 * build
	 * 
	 * @param pScheme
	 * @param pHost
	 * @param pPort
	 * @param pNameSpace
	 * @param pObjName
	 * @param pKeys
	 * @return CIMObjectPath
	 */
	public CIMObjectPath build(String pScheme, String pHost, String pPort, String pNameSpace,
			String pObjName, CIMProperty<?>[] pKeys) {
		return build(this.iBasePath, pScheme, pHost, pPort, pNameSpace, pObjName, pKeys);
	}

	/**
	 * build
	 * 
	 * @param pBasePath
	 * @param pObjPathStr
	 * @return CIMObjectPath
	 */
	public static CIMObjectPath build(CIMObjectPath pBasePath, String pObjPathStr) {
		CIMObjectPath path = new CIMObjectPath(pObjPathStr);
		return build(pBasePath, path.getScheme(), path.getHost(), path.getPort(), path
				.getNamespace(), path.getObjectName(), path.getKeys());
	}

	/**
	 * build
	 * 
	 * @param pBasePath
	 * @param pObjName
	 * @param pNameSpace
	 * @return CIMObjectPath
	 */
	public static CIMObjectPath build(CIMObjectPath pBasePath, String pObjName, String pNameSpace) {
		return build(pBasePath, null, null, null, pNameSpace, pObjName, null);
	}

	/**
	 * build
	 * 
	 * @param pBasePath
	 * @param pObjName
	 * @param pNameSpace
	 * @param pKeys
	 * @return CIMObjectPath
	 */
	public static CIMObjectPath build(CIMObjectPath pBasePath, String pObjName, String pNameSpace,
			CIMProperty<?>[] pKeys) {
		return build(pBasePath, null, null, null, pNameSpace, pObjName, pKeys);
	}

	/**
	 * build
	 * 
	 * @param pBasePath
	 * @param pHost
	 * @param pNameSpace
	 * @param pObjName
	 * @param pKeys
	 * @return CIMObjectPath
	 */
	public static CIMObjectPath build(CIMObjectPath pBasePath, String pHost, String pNameSpace,
			String pObjName, CIMProperty<?>[] pKeys) {
		return build(pBasePath, null, pHost, null, pNameSpace, pObjName, pKeys);
	}

	/**
	 * build
	 * 
	 * @param pBasePath
	 * @param pScheme
	 * @param pHost
	 * @param pPort
	 * @param pNameSpace
	 * @param pObjName
	 * @param pKeys
	 * @return CIMObjectPath
	 */
	public static CIMObjectPath build(CIMObjectPath pBasePath, String pScheme, String pHost,
			String pPort, String pNameSpace, String pObjName, CIMProperty<?>[] pKeys) {
		if (pBasePath == null) return new CIMObjectPath(pScheme, pHost, pPort, pNameSpace,
				pObjName, pKeys);
		return new CIMObjectPath(pScheme == null ? pBasePath.getScheme() : pScheme,
				pHost == null ? pBasePath.getHost() : pHost, pPort == null ? pBasePath.getPort()
						: pPort, pNameSpace == null ? pBasePath.getNamespace() : pNameSpace,
				pObjName == null ? pBasePath.getObjectName() : pObjName, pKeys // local
		// objectpath shouldn't contain keys
		);
	}

}
