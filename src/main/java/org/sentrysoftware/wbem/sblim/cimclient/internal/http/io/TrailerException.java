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
 * 1688273    2007-04-16  ebak         Full support of HTTP trailers
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 */

package org.sentrysoftware.wbem.sblim.cimclient.internal.http.io;

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

import org.sentrysoftware.wbem.javax.wbem.WBEMException;

import org.sentrysoftware.wbem.sblim.cimclient.internal.util.WBEMConstants;

/**
 * TrailerException is thrown by ChunkedInputStream when it receives a http
 * trailer which contains the following entries: CIMStatusCode,
 * CIMStatusCodeDescription. These http trailer entries are known to be used by
 * Pegasus CIMOM.
 */
public class TrailerException extends RuntimeException {

	private static final long serialVersionUID = 4355341648542585509L;

	private WBEMException iWBEMException;

	/**
	 * Ctor.
	 * 
	 * @param pException
	 *            The contained WBEMException
	 */
	public TrailerException(WBEMException pException) {
		super(WBEMConstants.HTTP_TRAILER_STATUS_CODE + ":" + pException.getID() + " "
				+ WBEMConstants.HTTP_TRAILER_STATUS_DESCRIPTION + ":" + pException.getMessage());
		this.iWBEMException = pException;
	}

	/**
	 * getWBEMException
	 * 
	 * @return WBEMException
	 */
	public WBEMException getWBEMException() {
		return this.iWBEMException;
	}

}
