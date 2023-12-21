package org.sentrysoftware.wbem.client;

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

import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.sentrysoftware.wbem.client.exceptions.WqlQuerySyntaxException;
import org.sentrysoftware.wbem.javax.wbem.WBEMException;

/**
 * Functions to execute WBEM query.
 *
 */
public class WbemExecutor {

	private WbemExecutor() { }

	/**
	 * Execute a WBEM query on remote.
	 *
	 * @param url The remote URL.
	 * @param namespace The WBEM namespace.
	 * @param username The user name.
	 * @param password The Password.
	 * @param query The WQL Query to execute.
	 * @param timeout Timeout in milliseconds.
	 * @param arraySeparator The array separator value. default value '|'
	 * @return
	 * @throws WqlQuerySyntaxException On WQL syntax errors.
	 * @throws WBEMException On WBEM errors.
	 * @throws TimeoutException To notify userName of timeout.
	 * @throws InterruptedException
	 */
	public static WbemQueryResult executeWql(
			final URL url,
			final String namespace,
			final String username,
			final char[] password,
			final String query,
			int timeout,
			final String arraySeparator)
					throws WqlQuerySyntaxException,
					WBEMException,
					TimeoutException,
					InterruptedException  {
		return executeMethod(
				url,
				namespace,
				username,
				password,
				query,
				null,
				timeout,
				arraySeparator);
	}

	/**
	 * Execute WBEM get associators on remote.
	 *
	 * @param url The remote URL.
	 * @param username The user name.
	 * @param password The Password.
	 * @param query The WQL Query to execute.
	 * @param objectPathAssociators The object path for ASSOCIATORS.
	 * @param timeout Timeout in milliseconds.
	 * @param arraySeparator The array separator value. default value '|'
	 * @return
	 * @throws WqlQuerySyntaxException On WQL syntax errors.
	 * @throws WBEMException On WBEM errors.
	 * @throws TimeoutException To notify userName of timeout.
	 * @throws InterruptedException
	 */
	public static WbemQueryResult getAssociators(
			final URL url,
			final String username,
			final char[] password,
			final String query,
			final String objectPathAssociators,
			int timeout,
			final String arraySeparator)
					throws WqlQuerySyntaxException,
					WBEMException,
					TimeoutException,
					InterruptedException   {
		return executeMethod(
				url,
				null,
				username,
				password,
				query,
				objectPathAssociators,
				timeout,
				arraySeparator);
	}

	/**
	 * Execute the WBEM method with timeout.
	 *
	 * @param url
	 * @param namespace
	 * @param username
	 * @param password
	 * @param query
	 * @param objectPathAssociators
	 * @param timeout
	 * @param arraySeparator
	 * @return
	 * @throws InterruptedException
	 * @throws TimeoutException
	 * @throws WBEMException
	 * @throws WqlQuerySyntaxException
	 */
	private static WbemQueryResult executeMethod(
			final URL url,
			final String namespace,
			final String username,
			final char[] password,
			final String query,
			final String objectPathAssociators,
			int timeout,
			final String arraySeparator)
					throws InterruptedException,
					TimeoutException,
					WBEMException,
					WqlQuerySyntaxException {

		Utils.checkNonNull(url, "url");
		Utils.checkNonNull(username, "username");
		Utils.checkNonNull(password, "password");

		final WqlQuery wqlQuery = WqlQuery.parseQuery(query);

		final ExecutorService executor = Executors.newSingleThreadExecutor();

		final Future<WbemQueryResult> future = executor.submit(() -> {
			try (final WbemClient matsyaWbemClient = new WbemClient()) {
				matsyaWbemClient.connect(url, username, password, timeout);

				return objectPathAssociators == null ?
						matsyaWbemClient.executeWql(wqlQuery, namespace, arraySeparator) :
						matsyaWbemClient.getAssociators(wqlQuery, objectPathAssociators, arraySeparator);
			}
		});

		try {
			return future.get(timeout, TimeUnit.MILLISECONDS);

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw (InterruptedException) e;

		} catch (TimeoutException e) {
			future.cancel(true);
			throw e;

		}  catch (ExecutionException e) {
			if (e.getCause() instanceof WBEMException) {
				throw (WBEMException) e.getCause();
			}
			// else should be RunTimeException as matsyaWbemClient only thrown
			// WBEMException as checked exceptions.
			throw (RuntimeException) e.getCause();
		}
		finally {
			executor.shutdownNow();
		}
	}
}
