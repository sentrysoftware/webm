/*
  (C) Copyright IBM Corp. 2007, 2009

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
 */

package org.sentrysoftware.wbem.sblim.slp.internal.msg;

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

/**
 * FunctionIDs
 */
public interface FunctionIDs {

	/**
	 * SRV_RQST
	 */
	public static final int SRV_RQST = 1;

	/**
	 * SRV_RPLY
	 */
	public static final int SRV_RPLY = 2;

	/**
	 * SRV_REG
	 */
	public static final int SRV_REG = 3;

	/**
	 * SRV_DEREG
	 */
	public static final int SRV_DEREG = 4;

	/**
	 * SRV_ACK
	 */
	public static final int SRV_ACK = 5;

	/**
	 * ATTR_RQST
	 */
	public static final int ATTR_RQST = 6;

	/**
	 * ATTR_RPLY
	 */
	public static final int ATTR_RPLY = 7;

	/**
	 * DA_ADVERT
	 */
	public static final int DA_ADVERT = 8;

	/**
	 * SRV_TYPE_RQST
	 */
	public static final int SRV_TYPE_RQST = 9;

	/**
	 * SRV_TYPE_RPLY
	 */
	public static final int SRV_TYPE_RPLY = 10;

	/**
	 * SA_ADVERT
	 */
	public static final int SA_ADVERT = 11;

	/**
	 * FIRST_ID
	 */
	public static final int FIRST_ID = SRV_RQST;

	/**
	 * LAST_ID
	 */
	public static final int LAST_ID = SA_ADVERT;

}
