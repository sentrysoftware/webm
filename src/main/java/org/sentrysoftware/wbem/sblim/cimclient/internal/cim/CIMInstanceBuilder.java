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
 * 1565892    2006-11-30  ebak         Make SBLIM client JSR48 compliant
 * 1669961    2006-04-16  lupusalex    CIMTypedElement.getType() =>getDataType()
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2204488 	  2008-10-28  raman_arora  Fix code to remove compiler warnings
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
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

import java.util.ArrayList;
import java.util.List;

import org.sentrysoftware.wbem.javax.cim.CIMObjectPath;
import org.sentrysoftware.wbem.javax.cim.CIMProperty;

/**
 * Class CIMInstanceBuilder provides help for CIMInstance(CIMObjectPath,
 * CIMProperty[]) constructor.<br>
 * <br>
 * CIMObjectPath param has to contain the key properties only.<br>
 * ( VALUE.NAMEDINSTANCE->INSTANCENAME->KEYBINDING )<br>
 * CIMProperty[] param has to contain all properties, including key properties.<br>
 * ( VALUE.NAMEDINSTANCE->INSTANCE->PROPERTY* )<br>
 * The implementation merges the properties from both params.<br>
 * From CIMObjectPath's keys only the type and value information is considered.<br>
 * 
 */
public class CIMInstanceBuilder {

	private CIMProperty<?>[] iProperties;

	private static final Object[] EMPTY_RPOP_A = new CIMProperty[0];

	/**
	 * Ctor.
	 * 
	 * @param pPath
	 * @param pProps
	 * @throws IllegalArgumentException
	 */
	public CIMInstanceBuilder(CIMObjectPath pPath, CIMProperty<?>[] pProps)
			throws IllegalArgumentException {
		this.iProperties = pProps != null ? pProps : new CIMProperty[0];
		CIMElementSorter.sort(this.iProperties);
		addPathKeys(pPath);
	}

	/**
	 * Extends the keys of the passed CIMObjectPath.
	 * 
	 * @param pPath
	 * @return the new CIMObjectPath
	 */
	public CIMObjectPath setKeys(CIMObjectPath pPath) {
		List<CIMProperty<?>> keys = new ArrayList<CIMProperty<?>>();
		for (int i = 0; i < this.iProperties.length; ++i) {
			CIMProperty<?> prop = this.iProperties[i];
			if (prop.isKey()) keys.add(prop);
		}

		/*
		 * CIMObjectPath( String scheme, String host, String port, String
		 * namespace, String objectName, CIMProperty[] keys )
		 */
		return new CIMObjectPath(pPath.getScheme(), pPath.getHost(), pPath.getPort(), pPath
				.getNamespace(), pPath.getObjectName(), (CIMProperty[]) keys.toArray(EMPTY_RPOP_A));
	}

	/**
	 * getAllPropertis
	 * 
	 * @return all properties in an ordered way
	 */
	public CIMProperty<?>[] getAllPropertis() {
		return this.iProperties;
	}

	private void addPathKeys(CIMObjectPath pPath) throws IllegalArgumentException {
		CIMProperty<?>[] keys = pPath.getKeys();
		for (int i = 0; i < keys.length; i++) {
			CIMProperty<?> key = keys[i];
			int pos = CIMElementSorter.findIdx(this.iProperties, key.getName());
			if (pos < 0) {
				pos = -pos - 1;
				CIMProperty<?>[] newArray = new CIMProperty[this.iProperties.length + 1];
				System.arraycopy(this.iProperties, 0, newArray, 0, pos);
				newArray[pos] = key;
				System.arraycopy(this.iProperties, pos, newArray, pos + 1, this.iProperties.length
						- pos);
				this.iProperties = newArray;
			} else {
				CIMProperty<?> prop = this.iProperties[pos];
				// typeAndValueCheck(key, prop);
				if (!prop.isKey()) this.iProperties[pos] = mkKey(prop);
			}
		}
	}

	private static CIMProperty<Object> mkKey(CIMProperty<?> pProp) {
		return new CIMProperty<Object>(pProp.getName(), pProp.getDataType(), pProp.getValue(),
				true, pProp.isPropagated(), pProp.getOriginClass());
	}

	/*
	 * private static void typeAndValueCheck(CIMProperty pPathProp, CIMProperty
	 * pArrayProp) throws IllegalArgumentException { CIMDataType pType =
	 * pPathProp.getDataType(), aType = pArrayProp.getDataType(); if (pType ==
	 * null ? aType != null : pType.getType() != aType.getType()) throw new
	 * IllegalArgumentException( pPathProp.getName() + " property presents in
	 * CIMObjectPath param and CIMProperty[] param " + "with different types!");
	 * Object pValue = pPathProp.getValue(), aValue = pArrayProp.getValue(); if
	 * (pValue == null ? aValue != null : !pValue.equals(aValue)) throw new
	 * IllegalArgumentException( pPathProp.getName() + " property conatins
	 * different values in CIMObjectPath and " + "CIMProperty params!"); }
	 */

}
