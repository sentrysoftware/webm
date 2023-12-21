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

import org.sentrysoftware.wbem.sblim.cimclient.internal.util.MOF;
import org.sentrysoftware.wbem.sblim.cimclient.internal.util.Util;

/**
 * Class UntypedStringValue parses an untyped string value.
 */
public class StringValue extends Value implements QuotedValue {

	private static final int NORMAL = 0;

	private static final int ESCAPED = 1;

	private static final int CLOSED = 2;

	/**
	 * Factory method for parsing quoted strings.
	 * 
	 * @param pUriStr
	 * @return <code>Value</code> instance
	 * @throws IllegalArgumentException
	 *             if parsing failed
	 */
	public static Value parse(URIString pUriStr) throws IllegalArgumentException {
		URIString uriStr = pUriStr.deepCopy();
		if (uriStr.charAt(0) != '\"') {
			String msg = "Starting '\"' is missing!\n" + uriStr.markPosition();
			throw new IllegalArgumentException(msg);
		}
		int rIdx = 1;
		StringBuffer dstBuf = new StringBuffer();
		int state = NORMAL;
		while (rIdx < uriStr.length()) {
			char ch = uriStr.charAt(rIdx++);
			if (state == NORMAL) {
				if (ch == '\\') {
					state = ESCAPED;
					continue;
				}
				if (ch == '"') {
					state = CLOSED;
					break;
				}
			} else { // skip if Escaped
				state = NORMAL;
			}
			dstBuf.append(ch);
		}
		if (state != CLOSED) {
			String msg = "Closing '\"' is missing!\n" + uriStr.markPosition(rIdx);
			throw new IllegalArgumentException(msg);
		}
		uriStr.cutStarting(rIdx);
		// next character should be ',' or nothing
		if (uriStr.length() != 0 && uriStr.charAt(0) != ',') {
			String msg = "Next character should be ',' or end of string!\n" + uriStr.markPosition();
			throw new IllegalArgumentException(msg);
		}
		pUriStr.set(uriStr);
		return new StringValue(dstBuf.toString());
	}

	private String iStr;

	private StringValue(String pStr) {
		this.iStr = pStr;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.iStr;
	}

	/**
	 * @see QuotedValue#toQuotedString()
	 */
	public String toQuotedString() {
		return Util.quote(this.iStr);
	}

	/**
	 * @see Value#getTypeInfo()
	 */
	@Override
	public String getTypeInfo() {
		return MOF.DT_STR;
	}

}
