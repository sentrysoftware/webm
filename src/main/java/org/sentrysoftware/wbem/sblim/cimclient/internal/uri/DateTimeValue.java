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

import org.sentrysoftware.wbem.javax.cim.CIMDateTime;
import org.sentrysoftware.wbem.javax.cim.CIMDateTimeAbsolute;
import org.sentrysoftware.wbem.javax.cim.CIMDateTimeInterval;

import org.sentrysoftware.wbem.sblim.cimclient.internal.util.MOF;

/**
 * Class DateTimeValue is parses and encapsulates a datetime.
 * 
 */
public class DateTimeValue extends Value implements QuotedValue {

	private CIMDateTime iDateTime;

	/**
	 * datetimeValue = // quoted datetime string
	 * 
	 * @param pStrVal
	 *            - the dateTime string in an unquoted form
	 * @param pThrow
	 * @return <code>Value</code> or <code>null</code> if parsing failed and
	 *         <code>pThrow</code> is <code>false</code>
	 * @throws IllegalArgumentException
	 *             if parsing failed and pThrow is true.
	 */
	public static Value parse(String pStrVal, boolean pThrow) throws IllegalArgumentException {
		CIMDateTime dateTime;
		try {
			dateTime = new CIMDateTimeAbsolute(pStrVal);
		} catch (IllegalArgumentException e0) {
			try {
				dateTime = new CIMDateTimeInterval(pStrVal);
			} catch (IllegalArgumentException e1) {
				if (pThrow) {
					String msg = "Value=" + pStrVal + "\nFailed to parse as DateTimeAbsolute!:\n"
							+ e0.getMessage() + "\nFailed to parse as DateTimeInterval!:\n"
							+ e1.getMessage();
					throw new IllegalArgumentException(msg);
				}
				return null;
			}
		}
		return new DateTimeValue(dateTime);
	}

	/**
	 * @see #parse(String, boolean)
	 * @param pStrVal
	 * @return a Value or null if parsing failed.
	 */
	public static Value parse(String pStrVal) {
		return parse(pStrVal, false);
	}

	private DateTimeValue(CIMDateTime pDateTime) {
		this.iDateTime = pDateTime;
	}

	/**
	 * getDateTime
	 * 
	 * @return CIMDateTime
	 */
	public CIMDateTime getDateTime() {
		return this.iDateTime;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.iDateTime.toString();
	}

	/**
	 * @see QuotedValue#toQuotedString()
	 */
	public String toQuotedString() {
		return "\"" + this.iDateTime.toString() + '"';
	}

	/**
	 * @see Value#getTypeInfo()
	 */
	@Override
	public String getTypeInfo() {
		return MOF.DT_DATETIME;
	}

}
