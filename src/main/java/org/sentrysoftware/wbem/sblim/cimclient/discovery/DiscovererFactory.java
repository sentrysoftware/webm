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

import java.util.Locale;

import org.sentrysoftware.wbem.sblim.cimclient.discovery.Discoverer;
import org.sentrysoftware.wbem.sblim.cimclient.internal.discovery.slp.DiscovererSLP;

/**
 * Class DiscovererFactory is responsible for creating concrete instances of the
 * Discoverer interface.
 * 
 * This class is thread-safe.
 * 
 * @since 2.0.2
 */
public class DiscovererFactory {

	/**
	 * The Service Location Protocol (SLP)
	 */
	public static final String SLP = "SLP";

	private static final String[] cProtocols = new String[] { SLP };

	/**
	 * Returns the concrete Discoverer for a given discovery protocol.
	 * 
	 * @param pProtocol The discovery protocol, e.g. "SLP"
	 * @return The corresponding discoverer
	 * @throws IllegalArgumentException On unsupported protocols
	 * Factory Method
	 */
	public static Discoverer getDiscoverer(String pProtocol) throws IllegalArgumentException {
		if (SLP.equalsIgnoreCase(pProtocol)) {
			return new DiscovererSLP(Locale.US);
		}
		throw new IllegalArgumentException("Protocol " + pProtocol + " not supported.");
	}

	/**
	 * Return an array of all supported discovery protocols
	 * 
	 * @return The supported protocols
	 */
	public static String[] getSupportedProtocols() {
		return cProtocols;
	}

	private DiscovererFactory() {
		/**/}
}
