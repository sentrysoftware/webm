package org.sentrysoftware.wbem;

import java.net.URL;
import java.util.Locale;

import javax.security.auth.Subject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.sentrysoftware.wbem.client.WbemClient;
import org.sentrysoftware.wbem.client.WbemQueryResult;
import org.sentrysoftware.wbem.client.WqlQuery;
import org.sentrysoftware.wbem.javax.cim.CIMInstance;
import org.sentrysoftware.wbem.javax.cim.CIMObjectPath;
import org.sentrysoftware.wbem.javax.wbem.CloseableIterator;
import org.sentrysoftware.wbem.javax.wbem.client.EnumerateResponse;
import org.sentrysoftware.wbem.javax.wbem.client.WBEMClient;
import org.sentrysoftware.wbem.javax.wbem.client.WBEMClientConstants;
import org.sentrysoftware.wbem.javax.wbem.client.WBEMClientFactory;

class WbemClientTest {

	@Test
	void testConnect() throws Exception {
		final URL url = new URL("https://host:8080");
		final String username = "user";
		final char[] password = {'p', 'a', 's', 's'};
		int timeout = 60*1000;

		Assertions.assertThrows(IllegalArgumentException.class, () -> new WbemClient().connect(null, username, password, timeout));
		Assertions.assertThrows(IllegalArgumentException.class, () -> new WbemClient().connect(url, null, password, timeout));
		Assertions.assertThrows(IllegalArgumentException.class, () -> new WbemClient().connect(url, username, null, timeout));

		try (final MockedStatic<WBEMClientFactory> mockedWBEMClientFactory = Mockito.mockStatic(WBEMClientFactory.class);
				final WbemClient wbemClient = new WbemClient()) {
			final WBEMClient client = Mockito.mock(WBEMClient.class);

			mockedWBEMClientFactory.when(() -> WBEMClientFactory.getClient("CIM-XML")).thenReturn(client);
			Mockito.doNothing().when(client).setProperty(WBEMClientConstants.PROP_TIMEOUT, String.valueOf(timeout));
			Mockito.doNothing().when(client).initialize(
					ArgumentMatchers.any(CIMObjectPath.class),
					ArgumentMatchers.any(Subject.class),
					ArgumentMatchers.eq(new Locale[] {Locale.ENGLISH}));

			wbemClient.connect(url, username, password, timeout);
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	void testExecuteWql() throws Exception {
		final String namespace = "root/emc";
		final WqlQuery queryHandler = WqlQuery.parseQuery("Select * from EMC_StorageSystem");

		Assertions.assertThrows(IllegalArgumentException.class, () -> new WbemClient().executeWql(null, namespace, null));
		Assertions.assertThrows(IllegalArgumentException.class, () -> new WbemClient().executeWql(queryHandler, null, null));

		Assertions.assertThrows(IllegalStateException.class, () -> new WbemClient().executeWql(queryHandler, namespace, null));

		final URL url = new URL("https://host:8080");
		final String username = "user";
		final char[] password = {'p', 'a', 's', 's'};
		int timeout = 60*1000;
		final WBEMClient client = Mockito.mock(WBEMClient.class);
		final CloseableIterator<CIMInstance> iterator = Mockito.mock(CloseableIterator.class);
		final WbemQueryResult result = Mockito.mock(WbemQueryResult.class);

		try (final MockedStatic<WBEMClientFactory> mockedWBEMClientFactory = Mockito.mockStatic(WBEMClientFactory.class);
				final MockedStatic<WbemClient> mockedMatsyaWbemClient = Mockito.mockStatic(WbemClient.class);
				final WbemClient matsyaWbemClient =new WbemClient()) {

			mockedWBEMClientFactory.when(() -> WBEMClientFactory.getClient("CIM-XML")).thenReturn(client);

			Mockito.doNothing().when(client).setProperty(WBEMClientConstants.PROP_TIMEOUT, String.valueOf(timeout));

			Mockito.doNothing().when(client).initialize(
					ArgumentMatchers.any(CIMObjectPath.class),
					ArgumentMatchers.any(Subject.class),
					ArgumentMatchers.eq(new Locale[] {Locale.ENGLISH}));

			Mockito.doReturn(iterator).when(client).enumerateInstances(
					ArgumentMatchers.any(CIMObjectPath.class),
					ArgumentMatchers.eq(true),
					ArgumentMatchers.eq(false),
					ArgumentMatchers.eq(true),
					ArgumentMatchers.eq(queryHandler.getPropertiesArray()));

			mockedMatsyaWbemClient.when(() -> WbemClient.enumerateInstances(
					ArgumentMatchers.any(WqlQuery.class),
					ArgumentMatchers.any(CloseableIterator.class),
					ArgumentMatchers.isNull()))
			.thenReturn(result);

			matsyaWbemClient.connect(url, username, password, timeout);

			Assertions.assertEquals(result, matsyaWbemClient.executeWql(queryHandler, namespace, null));
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	void testGetAssociators() throws Exception {
		final URL url = new URL("https://host:8080");
		final String username = "user";
		final char[] password = {'p', 'a', 's', 's'};
		int timeout = 60*1000;
		final WBEMClient client = Mockito.mock(WBEMClient.class);
		final WqlQuery queryHandler = WqlQuery.parseQuery("Select * from EMC_StorageSystem");
		final EnumerateResponse<CIMInstance> response = Mockito.mock(EnumerateResponse.class);
		final CloseableIterator<CIMInstance> iterator = Mockito.mock(CloseableIterator.class);
		final WbemQueryResult result = Mockito.mock(WbemQueryResult.class);

		final String objectPathAssociators = "objectPathAssociators";
		Assertions.assertThrows(IllegalArgumentException.class, () -> new WbemClient().getAssociators(null, objectPathAssociators, null));
		Assertions.assertThrows(IllegalArgumentException.class, () -> new WbemClient().getAssociators(queryHandler, null, null));

		Assertions.assertThrows(IllegalStateException.class, () -> new WbemClient().getAssociators(queryHandler, objectPathAssociators, null));

		try (final MockedStatic<WBEMClientFactory> mockedWBEMClientFactory = Mockito.mockStatic(WBEMClientFactory.class);
				final MockedStatic<WbemClient> mockedMatsyaWbemClient = Mockito.mockStatic(WbemClient.class);
				final WbemClient matsyaWbemClient = new WbemClient()) {

			mockedWBEMClientFactory.when(() -> WBEMClientFactory.getClient("CIM-XML")).thenReturn(client);

			Mockito.doNothing().when(client).setProperty(WBEMClientConstants.PROP_TIMEOUT, String.valueOf(timeout));

			Mockito.doNothing().when(client).initialize(
					ArgumentMatchers.any(CIMObjectPath.class),
					ArgumentMatchers.any(Subject.class),
					ArgumentMatchers.eq(new Locale[] {Locale.ENGLISH}));

			Mockito.doReturn(response).when(client).associators(
					ArgumentMatchers.any(CIMObjectPath.class),
					ArgumentMatchers.eq(queryHandler.getClassName()),
					ArgumentMatchers.isNull(),
					ArgumentMatchers.isNull(),
					ArgumentMatchers.isNull(),
					ArgumentMatchers.eq(false),
					ArgumentMatchers.eq(queryHandler.getPropertiesArray()),
					ArgumentMatchers.isNull(),
					ArgumentMatchers.isNull(),
					ArgumentMatchers.isNull(),
					ArgumentMatchers.eq(false),
					ArgumentMatchers.isNull());

			Mockito.doReturn(iterator).when(response).getResponses();

			mockedMatsyaWbemClient.when(() -> WbemClient.enumerateInstances(
					ArgumentMatchers.any(WqlQuery.class),
					ArgumentMatchers.any(CloseableIterator.class),
					ArgumentMatchers.isNull()))
			.thenReturn(result);

			matsyaWbemClient.connect(url, username, password, timeout);

			Assertions.assertEquals(result, matsyaWbemClient.getAssociators(queryHandler, objectPathAssociators, null));
		}
	}

}
