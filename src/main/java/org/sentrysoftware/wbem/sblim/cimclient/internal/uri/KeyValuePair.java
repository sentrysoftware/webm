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
 * 1565892    2006-11-05  ebak         Make SBLIM client JSR48 compliant
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 */

package org.sentrysoftware.wbem.sblim.cimclient.internal.uri;

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

import java.util.regex.Pattern;

/**
 * key_name "=" key_value
 */
public class KeyValuePair {

	private static final Pattern KEYNAMEPAT = Pattern.compile("^([A-Za-z][0-9A-Za-z_]*).*");

	/**
	 * Tries to get an <code>KeyValuePair</code> from the passed
	 * <code>pUriStr</code>.
	 * 
	 * @param pTyped
	 * @param pUriStr
	 * @return an <code>KeyValuePair</code> or <code>null</code> if failed.
	 * @throws IllegalArgumentException
	 */
	public static KeyValuePair parse(boolean pTyped, URIString pUriStr)
			throws IllegalArgumentException {
		// TODO: tracing TRC.log(uriStr.toString());
		URIString uriStr = pUriStr.deepCopy();
		if (!uriStr.matchAndCut(KEYNAMEPAT, 1)) {
			String msg = "keyName expected!\n" + uriStr.markPosition();
			throw new IllegalArgumentException(msg);
		}
		String key = uriStr.group(1);
		if (!uriStr.cutStarting('=')) {
			String msg = "'=' expected!\n" + uriStr.markPosition();
			throw new IllegalArgumentException(msg);
		}
		Value value = Value.parse(pTyped, uriStr);
		if (value == null) {
			String msg = "value expected!\n" + uriStr.markPosition();
			throw new IllegalArgumentException(msg);
		}
		pUriStr.set(uriStr);
		return new KeyValuePair(key, value, pTyped);
	}

	private String iKey;

	private Value iValue;

	private boolean iTyped;

	private KeyValuePair(String pKey, Value pValue, boolean pTyped) {
		this.iKey = pKey;
		this.iValue = pValue;
		this.iTyped = pTyped;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer(this.iKey + '=');
		if (this.iTyped) buf.append('(' + this.iValue.getTypeInfo() + ')');
		buf.append((this.iValue instanceof QuotedValue) ? ((QuotedValue) this.iValue)
				.toQuotedString() : this.iValue.toString());
		return buf.toString();
	}

	/**
	 * getKey
	 * 
	 * @return the key String
	 */
	public String getKey() {
		return this.iKey;
	}

	/**
	 * getValue
	 * 
	 * @return the value String
	 */
	public Value getValue() {
		return this.iValue;
	}

}
