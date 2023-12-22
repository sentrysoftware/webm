/*
  (C) Copyright IBM Corp. 2007, 2009

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
 * 1678915    2007-03-12  lupusalex    Integrated WBEM service discovery via SLP
 * 1729361    2007-06-04  lupusalex    Multicast discovery is broken in DiscovererSLP
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 */

package org.sentrysoftware.wbem.sblim.cimclient.discovery;

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
 * Interface Discoverer offers methodology for discovering WBEM services.
 * 
 * Implementations SHALL ensure thread-safety
 * @since 2.0.2
 */
public interface Discoverer {

	/**
	 * Finds WBEM services using a given list of directory servers
	 * 
	 * @param pDirectoryUrls
	 *            An array of directory servers. For SLP this would be a list of
	 *            DA URLs.
	 * @return The array of WBEM service advertisements found
	 */
	public WBEMServiceAdvertisement[] findWbemServices(String[] pDirectoryUrls);

	/**
	 * Finds directory services. The semantics of this method might be protocol
	 * specific. E.g. for SLP this sends a multicast into the local subnet
	 * looking first for directory agent, second for service agents.
	 * 
	 * @return A String[] containing the URLs of the directories
	 * @since 2.0.3
	 */
	public String[] findDirectoryServices();
}
