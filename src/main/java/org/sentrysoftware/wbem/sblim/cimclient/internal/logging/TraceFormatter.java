/*
  (C) Copyright IBM Corp. 2006, 2009

  THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE
  ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE
  CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.

  You can obtain a current copy of the Eclipse Public License from
  http://www.opensource.org/licenses/eclipse-1.0.php

  @author : Alexander Wolf-Reber, IBM, a.wolf-reber@de.ibm.com
 * 
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 1565892    2006-11-15  lupusalex    Make SBLIM client JSR48 compliant
 * 1745282    2007-06-29  ebak         Uniform time stamps for log files
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 */

package org.sentrysoftware.wbem.sblim.cimclient.internal.logging;

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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Class TraceFormatter implements the formatting algorithm for our console log.
 * 
 */
public class TraceFormatter extends Formatter {

	private final String iLineSeparator = System.getProperty("line.separator");

	/**
	 * Ctor.
	 */
	public TraceFormatter() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
	 */
	@Override
	public String format(LogRecord pRecord) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(TimeStamp.format(pRecord.getMillis()));
		buffer.append(" >");
		buffer.append(String.valueOf(pRecord.getThreadID()));
		buffer.append("< ");
		buffer.append(pRecord.getSourceMethodName());
		buffer.append(this.iLineSeparator);
		buffer.append(pRecord.getLevel().getName());
		buffer.append(": ");
		buffer.append(pRecord.getMessage());
		buffer.append(this.iLineSeparator);
		if (pRecord.getThrown() != null) {
			buffer.append("---> ");
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			pRecord.getThrown().printStackTrace(printWriter);
			printWriter.close();
			buffer.append(stringWriter.toString());
			buffer.append(this.iLineSeparator);
		}
		return buffer.toString();
	}
}
