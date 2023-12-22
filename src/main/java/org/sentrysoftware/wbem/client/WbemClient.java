package org.sentrysoftware.wbem.client;

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

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.security.auth.Subject;

import org.sentrysoftware.wbem.javax.cim.CIMInstance;
import org.sentrysoftware.wbem.javax.cim.CIMObjectPath;
import org.sentrysoftware.wbem.javax.cim.CIMProperty;
import org.sentrysoftware.wbem.javax.wbem.CloseableIterator;
import org.sentrysoftware.wbem.javax.wbem.WBEMException;
import org.sentrysoftware.wbem.javax.wbem.client.EnumerateResponse;
import org.sentrysoftware.wbem.javax.wbem.client.PasswordCredential;
import org.sentrysoftware.wbem.javax.wbem.client.UserPrincipal;
import org.sentrysoftware.wbem.javax.wbem.client.WBEMClient;
import org.sentrysoftware.wbem.javax.wbem.client.WBEMClientConstants;
import org.sentrysoftware.wbem.javax.wbem.client.WBEMClientFactory;

/**
 * Matsya WBEM client for query execution.
 *
 */
public class WbemClient implements AutoCloseable {

	private static final Locale[] FIXED_AVAILABLE_LOCALES_ARRAY = {Locale.ENGLISH};

	private WBEMClient client;

	private CloseableIterator<CIMInstance> iterator;

	/**
	 * Connect to WBEM client.
	 *
	 * @param url
	 * @param username
	 * @param password
	 * @param timeout
	 * @throws WBEMException
	 */
	public void connect(
			final URL url,
			final String username,
			final char[] password,
			int timeout) throws WBEMException {

		Utils.checkNonNull(url, "url");
		Utils.checkNonNull(username, "username");
		Utils.checkNonNull(password, "password");

		final CIMObjectPath cimObjectPath = new CIMObjectPath(
				url.getProtocol(),
				url.getHost(),
				String.valueOf(url.getPort()),
				null,
				null,
				null);

		final Subject subject = new Subject();
		subject.getPrincipals().add(new UserPrincipal(username));
		subject.getPrivateCredentials().add(new PasswordCredential(password));

		// Create and initialize a WBEM client.
		client = WBEMClientFactory.getClient("CIM-XML");
		client.setProperty(WBEMClientConstants.PROP_TIMEOUT, String.valueOf(timeout));
		client.initialize(cimObjectPath, subject, FIXED_AVAILABLE_LOCALES_ARRAY);
	}

	@Override
	public void close() {
		if (iterator != null) {
			iterator.close();
		}

		if (client != null) {
			client.close();
		}
	}

	/**
	 * Execute a WQL query on remote.
	 * @param wqlQuery The query handler.
	 * @param namespace The WBEM namespace.
	 * @param arraySeparator The array separator value. default value '|'
	 * @return
	 * @throws WBEMException
	 */
	public WbemQueryResult executeWql(
			final WqlQuery wqlQuery,
			final String namespace,
			final String arraySeparator) throws WBEMException {

		Utils.checkNonNull(wqlQuery, "wqlQuery");
		Utils.checkNonNull(namespace, "namespace");

		if (client == null) {
			throw new IllegalStateException("client must be connected first.");
		}

		iterator = client.enumerateInstances(
				new CIMObjectPath(null, null, null, namespace, wqlQuery.getClassName(), null),
				true,
				false,
				true,
				wqlQuery.getPropertiesArray());

		return enumerateInstances(wqlQuery, iterator, arraySeparator);
	}

	/**
	 * Get associators.
	 * @param wqlQuery The query handler.
	 * @param objectPathAssociators The object path for ASSOCIATORS.
	 * @param arraySeparator The array separator value. default value '|'
	 * @return
	 * @throws WBEMException
	 */
	public WbemQueryResult getAssociators(
			final WqlQuery wqlQuery,
			final String objectPathAssociators,
			final String arraySeparator) throws WBEMException {

		Utils.checkNonNull(wqlQuery, "wqlQuery");
		Utils.checkNonNull(objectPathAssociators, "objectPathAssociators");

		if (client == null) {
			throw new IllegalStateException("client must be connected first.");
		}

		final EnumerateResponse<CIMInstance> response = client.associators(
				new CIMObjectPath(objectPathAssociators),
				wqlQuery.getClassName(),
				null,
				null,
				null,
				false,
				wqlQuery.getPropertiesArray(),
				null,
				null,
				null,
				false,
				null);
		iterator = response.getResponses();

		return enumerateInstances(wqlQuery, iterator, arraySeparator);
	}

	public static WbemQueryResult enumerateInstances(
			final WqlQuery wqlQuery,
			final CloseableIterator<CIMInstance> iterator,
			final String arraySeparator) {
		if (iterator == null)  {
			return new WbemQueryResult(new ArrayList<>(), new ArrayList<>());
		}

		Set<String> properties = null;
		List<String> originalProperties = null;
		final List<List<String>> values = new ArrayList<>();

		while (iterator.hasNext()) {
			final CIMInstance cimInstance = iterator.next();

			if (properties == null) {
				properties =
						wqlQuery.getProperties().isEmpty() ?
						Stream.of(cimInstance.getProperties())
								.map(CIMProperty::getName)
								.collect(Collectors.toCollection(LinkedHashSet::new)) :
						wqlQuery.getProperties();
			}

			if (originalProperties == null) {
				originalProperties = wqlQuery.hasDuplicateProperties() ?
						wqlQuery.getOriginalProperties() :
						properties.stream().collect(Collectors.toList());
			}

			final List<String> row;
			if (wqlQuery.hasDuplicateProperties()) {

				final Map<String, String> cimProperties = properties.stream().collect(Collectors.toMap(
						String::toLowerCase,
						property -> WbemCimDataHandler.getCimPropertyAsString(property, cimInstance, arraySeparator)));

				row = originalProperties.stream()
						.map(property -> cimProperties.get(property.toLowerCase()))
						.collect(Collectors.toList());

			} else {
				row = properties.stream()
						.map(property -> WbemCimDataHandler.getCimPropertyAsString(property, cimInstance, arraySeparator))
						.collect(Collectors.toList());
			}

			values.add(row);
		}

		return new WbemQueryResult(originalProperties, values);
	}
}
