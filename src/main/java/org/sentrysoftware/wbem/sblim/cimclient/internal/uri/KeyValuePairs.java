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
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2) 
 */

package org.sentrysoftware.wbem.sblim.cimclient.internal.uri;

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

import java.util.Vector;

/**
 * key_value_pair *("," key_value_pair)
 */
public class KeyValuePairs extends Vector<Object> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7518417983119426792L;

	/**
	 * Tries to parse the key-value pairs from the passed <code>pUriStr</code>.
	 * 
	 * @param pTyped
	 * @param pUriStr
	 * @return instance of <code>UntypedKeyValuePairs</code> or
	 *         <code>null</code> if failed.
	 * @throws IllegalArgumentException
	 */
	public static KeyValuePairs parse(boolean pTyped, URIString pUriStr)
			throws IllegalArgumentException {
		// TODO: tracing TRC.log(uriStr.toString());
		URIString uriStr = pUriStr.deepCopy();
		KeyValuePairs pairs = new KeyValuePairs();
		KeyValuePair pair = null;
		while ((pair = KeyValuePair.parse(pTyped, uriStr)) != null) {
			pairs.add(pair);
			if (uriStr.length() > 0) {
				if (!uriStr.cutStarting(',')) {
					String msg = "',' expected!\n" + uriStr.markPosition();
					throw new IllegalArgumentException(msg);
				}
			}
			if (uriStr.length() == 0) break;
		}
		if (pairs.size() > 0) {
			pUriStr.set(uriStr);
			return pairs;
		}
		return null;
	}

	/**
	 * @see java.util.Vector#toString()
	 */
	@Override
	public String toString() {
		String sep = null;
		StringBuffer dstBuf = new StringBuffer();
		for (int i = 0; i < size(); i++) {
			KeyValuePair pair = (KeyValuePair) elementAt(i);
			if (sep != null) dstBuf.append(sep);
			else sep = ",";
			dstBuf.append(pair.toString());

		}
		return dstBuf.toString();
	}
}
