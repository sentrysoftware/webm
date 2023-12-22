/*
  SLPLimits.java

  (C) Copyright IBM Corp. 2006, 2009

  THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE
  ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE
  CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.

  You can obtain a current copy of the Eclipse Public License from
  http://www.opensource.org/licenses/eclipse-1.0.php

  @author : Alexander Wolf-Reber, IBM a.wolf-reber@de.ibm.com
 * 
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 1535793    2006-09-14  lupusalex    Fix&Integrate CIM&SLP configuration classes
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2907527    2009-12-02  blaschke-oss Fix SLP properties issues
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

/**
 * SLPLimits
 * 
 */
public interface SLPLimits {

	/**
	 * MAX_DISCOVERY_GRANULARITY
	 */
	public static final int MAX_DISCOVERY_GRANULARITY = 10800;

	/**
	 * MAX_DISCOVERY_INTERVAL
	 */
	public static final int MAX_DISCOVERY_INTERVAL = 3 * 60 * 60; // 3h

	/**
	 * MAX_MTU
	 */
	public static final int MAX_MTU = 8192;

	/**
	 * MAX_MULTICAST_MAXIMUM_WAIT
	 */
	public static final int MAX_MULTICAST_MAXIMUM_WAIT = 60000;

	/**
	 * MAX_MULTICAST_RADIUS
	 */
	public static final int MAX_MULTICAST_RADIUS = 255;

	/**
	 * MAX_SERVER_SOCKET_QUEUE_LENGTH
	 */
	public static final int MAX_SERVER_SOCKET_QUEUE_LENGTH = 0x7fffffff;

	/**
	 * MAX_TCP_TIMEOUT
	 */
	public static final int MAX_TCP_TIMEOUT = 360000;

	/**
	 * MIN_DISCOVERY_GRANULARITY
	 */
	public static final int MIN_DISCOVERY_GRANULARITY = 0;

	/**
	 * MIN_DISCOVERY_INTERVAL
	 */
	public static final int MIN_DISCOVERY_INTERVAL = 0;

	/**
	 * MIN_MTU
	 */
	public static final int MIN_MTU = 128;

	/**
	 * MIN_MULTICAST_MAXIMUM_WAIT
	 */
	public static final int MIN_MULTICAST_MAXIMUM_WAIT = 1000;

	/**
	 * MIN_MULTICAST_RADIUS
	 */
	public static final int MIN_MULTICAST_RADIUS = 1;

	/**
	 * MIN_SERVER_SOCKET_QUEUE_LENGTH
	 */
	public static final int MIN_SERVER_SOCKET_QUEUE_LENGTH = 0;

	/**
	 * MIN_TCP_TIMEOUT
	 */
	public static final int MIN_TCP_TIMEOUT = 100;

}
