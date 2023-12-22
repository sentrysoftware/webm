/*
  (C) Copyright IBM Corp. 2007, 2013

  THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE
  ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE
  CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.

  You can obtain a current copy of the Eclipse Public License from
  http://www.opensource.org/licenses/eclipse-1.0.php

  @author : Endre Bak, IBM, ebak@de.ibm.com
 * 
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 1804402    2007-09-28  ebak         IPv6 ready SLP
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 *    2650    2013-07-18  blaschke-oss SLP opaque value handling incorrect
 */

package org.sentrysoftware.wbem.sblim.slp.internal;

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

import java.util.Iterator;
import java.util.Vector;

import org.sentrysoftware.wbem.sblim.slp.ServiceLocationAttribute;

/**
 * AttributeHandler
 * 
 */
public class AttributeHandler {

	/*
	 * attr-list = attribute / attribute `,' attr-list attribute = `(' attr-tag
	 * `=' attr-val-list `)' / attr-tag attr-val-list = attr-val / attr-val `,'
	 * attr-val-list attr-tag = 1*safe-tag attr-val = intval / strval / boolval
	 * / opaque intval = [-]1*DIGIT strval = 1*safe-val boolval = "true" /
	 * "false" opaque = "\FF" 1*escape-val safe-val = ; Any character except
	 * reserved. safe-tag = ; Any character except reserved, star and bad-tag.
	 * reserved = `(' / `)' / `,' / `\' / `!' / `<' / `=' / `>' / `~' / CTL
	 * escape-val = `\' HEXDIG HEXDIG bad-tag = CR / LF / HTAB / `_' star = `*'
	 */
	/**
	 * @param pAttr
	 * @return String
	 */
	public static String buildString(ServiceLocationAttribute pAttr) {
		StringBuffer buf = new StringBuffer();
		Vector<?> valVec = pAttr.getValues();
		if (valVec != null && valVec.size() > 0) buf.append('(');
		buf.append(Convert.escape(pAttr.getId(), Convert.ATTR_RESERVED));
		if (valVec != null && valVec.size() > 0) {
			buf.append('=');
			Iterator<?> itr = valVec.iterator();
			boolean first = true;
			while (itr.hasNext()) {
				if (first) first = false;
				else buf.append(',');
				buf.append(AttributeHandler.escapeValue(itr.next()));
			}
			buf.append(')');
		}
		return buf.toString();
	}

	/**
	 * escapeValue
	 * 
	 * @param pValue
	 * @return String
	 */
	public static String escapeValue(Object pValue) {
		if (pValue instanceof String) {
			return Convert.escape((String) pValue, Convert.ATTR_RESERVED);
		} else if (pValue instanceof Integer) {
			return ((Integer) pValue).toString();
		} else if (pValue instanceof Boolean) {
			return ((Boolean) pValue).toString();
		} else if (pValue instanceof byte[]) {
			return AttributeHandler.mkOpaqueStr((byte[]) pValue);
		} else if (pValue == null) { return ""; }
		throw new IllegalArgumentException("Type: " + pValue.getClass().getName()
				+ " cannot be an attribute value!");
	}

	/**
	 * mkOpaqueStr
	 * 
	 * @param pBytes
	 * @return String
	 */
	public static String mkOpaqueStr(byte[] pBytes) {
		StringBuilder buf = new StringBuilder("\\FF");
		for (int i = 0; i < pBytes.length; i++) {
			int value = pBytes[i] & 0xff;
			String hexStr = Integer.toString(value, 16).toUpperCase();
			buf.append('\\');
			if (hexStr.length() == 1) buf.append('0');
			buf.append(hexStr);
		}
		return buf.toString();
	}

}
