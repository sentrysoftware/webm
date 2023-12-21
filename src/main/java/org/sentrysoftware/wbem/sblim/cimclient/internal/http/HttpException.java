/*
  (C) Copyright IBM Corp. 2005, 2009

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

import java.io.IOException;

/**
 * Class HttpException represents HTTP related problems
 * 
 */
public class HttpException extends IOException {

	private static final long serialVersionUID = 934925248736106630L;

	int iStatus;

	/**
	 * Ctor. Equivalent to <code>HttpException(-1, null, null)</code>
	 */
	public HttpException() {
		this(-1, null, null);
	}

	/**
	 * Ctor. Equivalent to <code>HttpException(-1, reason, null)</code>
	 * 
	 * @param reason
	 *            The reason
	 */
	public HttpException(String reason) {
		this(-1, reason, null);
	}

	/**
	 * Ctor. Equivalent to <code>HttpException(-1, reason, null)</code>
	 * 
	 * @param status
	 *            The status
	 * @param reason
	 *            The reason
	 */
	public HttpException(int status, String reason) {
		this(status, reason, null);
	}

	/**
	 * Ctor. Equivalent to <code>HttpException(-1, reason, null)</code>
	 * 
	 * @param status
	 *            The status
	 * @param reason
	 *            The reason
	 * @param cimError
	 *            The CIM error
	 */
	public HttpException(int status, String reason, String cimError) {
		super(reason);
		this.iStatus = status;
	}

	/**
	 * Returns the status
	 * 
	 * @return The status
	 */
	public int getStatus() {
		return this.iStatus;
	}

	@Override
	public String toString() {
		return super.toString() + "(status:" + this.iStatus + ")";
	}
}
