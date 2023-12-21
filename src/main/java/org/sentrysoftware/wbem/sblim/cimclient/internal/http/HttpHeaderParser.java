/*
  (C) Copyright IBM Corp. 2005, 2010

  THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE
  ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE
  CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.

  You can obtain a current copy of the Eclipse Public License from
  http://www.opensource.org/licenses/eclipse-1.0.php

  @author : Roberto Pineiro, IBM, roberto.pineiro@us.ibm.com
 * @author : Chung-hao Tan, IBM, chungtan@us.ibm.com
 * 
 * 
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 1535756    2006-08-07  lupusalex    Make code warning free
 * 1565892    2006-11-28  lupusalex    Make SBLIM client JSR48 compliant
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2750520    2009-04-10  blaschke-oss Code cleanup from empty statement et al
 * 3001353    2010-05-18  blaschke-oss HttpHeaderParser ignores return value of toLowerCase()
 */

package org.sentrysoftware.wbem.sblim.cimclient.internal.http;

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
 * Class HttpHeaderParser parses http headers
 * 
 */
public class HttpHeaderParser {

	String iRaw;

	Vector<String[]> iNameValuePair;

	/**
	 * Ctor.
	 * 
	 * @param pHeader
	 *            The header block
	 */
	public HttpHeaderParser(String pHeader) {
		this.iRaw = pHeader;
		this.iNameValuePair = new Vector<String[]>();
		if (this.iRaw != null) {
			this.iRaw = this.iRaw.trim();
			char charArray[] = this.iRaw.toCharArray();
			int startPosValue = 0;
			int currentPos = 0;
			boolean isName = true;
			boolean withinQuote = false;
			int len = charArray.length;
			String nvp[] = new String[2];
			do {
				char c = charArray[currentPos];
				if (c == '=') {
					nvp[0] = (new String(charArray, startPosValue, currentPos - startPosValue))
							.toLowerCase();
					isName = false;
					startPosValue = ++currentPos;
				} else if (c == '"') {
					if (withinQuote) {
						nvp[1] = new String(charArray, startPosValue, currentPos - startPosValue);
						this.iNameValuePair.add(nvp);
						nvp = new String[2];
						withinQuote = false;
						while (++currentPos < len
								&& (charArray[currentPos] == ' ' || charArray[currentPos] == ',' || charArray[currentPos] == ';')) {
							// ignore spaces and preceding comma
						}
						isName = true;
						startPosValue = currentPos;
					} else {
						withinQuote = true;
						startPosValue = ++currentPos;
					}
				} else if (c == ' ' || c == ',' || c == ';') {
					if (withinQuote) {
						currentPos++;
					} else {
						if (isName) {
							nvp[0] = (new String(charArray, startPosValue, currentPos
									- startPosValue)).toLowerCase();
							this.iNameValuePair.add(nvp);
							nvp = new String[2];
						} else {
							nvp[1] = new String(charArray, startPosValue, currentPos
									- startPosValue);
							this.iNameValuePair.add(nvp);
							nvp = new String[2];
						}

						while (++currentPos < len
								&& (charArray[currentPos] == ' ' || charArray[currentPos] == ',' || charArray[currentPos] == ';')) {
							// ignore spaces and preceding comma
						}
						isName = true;
						startPosValue = currentPos;
					}
				} else {
					currentPos++;
				}
			} while (currentPos < len);

			if (--currentPos > startPosValue) {
				if (!isName) {
					if (charArray[currentPos] == '"') {
						nvp[1] = new String(charArray, startPosValue, currentPos - startPosValue);
						this.iNameValuePair.add(nvp);
						nvp = new String[2];
					} else {
						nvp[1] = new String(charArray, startPosValue,
								(currentPos - startPosValue) + 1);
						this.iNameValuePair.add(nvp);
						nvp = new String[2];
					}
				} else {
					nvp[0] = (new String(charArray, startPosValue, (currentPos - startPosValue) + 1))
							.toLowerCase();
				}
				this.iNameValuePair.add(nvp);
			} else if (currentPos == startPosValue) {
				if (!isName) {
					if (charArray[currentPos] == '"') {
						nvp[1] = String.valueOf(charArray[currentPos - 1]);
						this.iNameValuePair.add(nvp);
						nvp = new String[2];
					} else {
						nvp[1] = String.valueOf(charArray[currentPos]);
						this.iNameValuePair.add(nvp);
						nvp = new String[2];
					}
				} else {
					nvp[0] = String.valueOf(charArray[currentPos]).toLowerCase();
				}
				this.iNameValuePair.add(nvp);
			}
		}
	}

	/**
	 * Returns the header field at a given index
	 * 
	 * @param pIndex
	 *            The index
	 * @return The field name
	 */
	public String getField(int pIndex) {
		if (pIndex < 0 || pIndex > this.iNameValuePair.size()) return null;
		return this.iNameValuePair.elementAt(pIndex)[0];
	}

	/**
	 * Returns the value of a header field at a given index
	 * 
	 * @param pIndex
	 * @return The value
	 */
	public String getValue(int pIndex) {
		if (pIndex < 0 || pIndex > this.iNameValuePair.size()) return null;
		return this.iNameValuePair.elementAt(pIndex)[1];
	}

	/**
	 * Returns the value of a header field for a given name
	 * 
	 * @param pName
	 *            The name
	 * @return The value
	 */
	public String getValue(String pName) {
		return getValue(pName, null);
	}

	/**
	 * Returns the value of a header field for a given name specifying a
	 * default.
	 * 
	 * @param pName
	 *            The name of the header field
	 * @param pDefault
	 *            The value returned if no field of the given name exists
	 * @return The value
	 */
	public String getValue(String pName, String pDefault) {
		if (pName == null) return pDefault;
		pName = pName.toLowerCase();
		for (int i = 0; i < this.iNameValuePair.size(); i++) {
			if (this.iNameValuePair.elementAt(i)[0] == null) return pDefault;
			if (pName.equals(this.iNameValuePair.elementAt(i)[0])) return this.iNameValuePair
					.elementAt(i)[1];
		}
		return pDefault;
	}

	/**
	 * Returns the value of a numeric header field for a given name specifying a
	 * default.
	 * 
	 * @param pName
	 *            The name of the header field
	 * @param pDefault
	 *            The value returned if no field of the given name exists
	 * @return The value
	 */
	public int getIntValue(String pName, int pDefault) {
		try {
			return Integer.parseInt(getValue(pName, String.valueOf(pDefault)));
		} catch (Exception e) {
			return pDefault;
		}
	}

	@Override
	public String toString() {
		return "raw:" + this.iRaw;
	}
}
