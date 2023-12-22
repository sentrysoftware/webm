/*
  ServiceType.java

  (C) Copyright IBM Corp. 2005, 2009

  THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE
  ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE
  CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.

  You can obtain a current copy of the Eclipse Public License from
  http://www.opensource.org/licenses/eclipse-1.0.php

  @author : Roberto Pineiro, IBM, roberto.pineiro@us.ibm.com
 * @author : Chung-hao Tan, IBM, chungtan@us.ibm.com
 * 
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 1516246    2006-07-22  lupusalex    Integrate SLP client code
 * 1804402    2007-09-28  ebak         IPv6 ready SLP
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2) 
 */

package org.sentrysoftware.wbem.sblim.slp;

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
import java.util.Vector;

/**
 * The Advertiser is the SA interface, allowing clients to register new service
 * instances with SLP, to change the attributes of existing services, and to
 * deregister service instances. New registrations and modifications of
 * attributes are made in the language locale with which the Advertiser was
 * created, deregistrations of service instances are made for all locales.
 */
public interface Advertiser {

	/**
	 * Return the language locale with which this object was created.
	 * 
	 * @return The locale
	 */
	public abstract Locale getLocale();

	/**
	 * Register a new service with SLP having the given attributes. The API
	 * library is required to perform the operation in all scopes obtained
	 * through configuration.
	 * 
	 * @param pURL
	 *            The URL for the service.
	 * @param pAttributes
	 *            A vector of ServiceLocationAttribute objects describing the
	 *            service.
	 * @throws ServiceLocationException
	 */
	public abstract void register(ServiceURL pURL, Vector<ServiceLocationAttribute> pAttributes)
			throws ServiceLocationException;

	/**
	 * Deregister a service from the SLP framework. This has the effect of
	 * deregistering the service from every language locale. The API library is
	 * required to perform the operation in all scopes obtained through
	 * configuration.
	 * 
	 * @param pURL
	 *            The URL for the service.
	 * @throws ServiceLocationException
	 */
	public abstract void deregister(ServiceURL pURL) throws ServiceLocationException;

	/**
	 * Update the registration by adding the given attributes. The API library
	 * is required to perform the operation in all scopes obtained through
	 * configuration.
	 * 
	 * @param pURL
	 *            The URL for the service.
	 * @param pAttributes
	 *            A Vector of ServiceLocationAttribute objects to add to the
	 *            existing registration. Use an empty vector to update the URL
	 *            alone. May not be null.
	 * @throws ServiceLocationException
	 */
	public abstract void addAttributes(ServiceURL pURL, Vector<ServiceLocationAttribute> pAttributes)
			throws ServiceLocationException;

	/**
	 * Delete the attributes from a URL for the locale with which the Advertiser
	 * was created. The API library is required to perform the operation in all
	 * scopes obtained through configuration.
	 * 
	 * @param pURL
	 *            The URL for the service.
	 * @param pAttributeIds
	 *            A vector of Strings indicating the ids of the attributes to
	 *            remove. The strings may be attribute ids or they may be
	 *            wildcard patterns to match ids. See [7] for the syntax of
	 *            wildcard patterns. The strings may include SLP reserved
	 *            characters, they will be escaped by the API before
	 *            transmission. May not be the empty vector or null.
	 * @throws ServiceLocationException
	 */
	public abstract void deleteAttributes(ServiceURL pURL, Vector<String> pAttributeIds)
			throws ServiceLocationException;
}
