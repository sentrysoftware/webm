package org.sentrysoftware.wbem;

import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.sentrysoftware.wbem.client.WbemExecutor;
import org.sentrysoftware.wbem.client.WbemQueryResult;
import org.sentrysoftware.wbem.javax.wbem.WBEMException;

class WbemExecutorTest {

	@SuppressWarnings("unchecked")
	@Test
	void testExecuteWql() throws Exception {
		final URL url = new URL("https://host:8080");
		final String username = "user";
		final char[] password = {'p', 'a', 's', 's'};
		int timeout = 60*1000;
		final String namespace = "root/emc";
		final String query = "Select * from EMC_StorageSystem";

		Assertions.assertThrows(IllegalArgumentException.class, () -> WbemExecutor.executeWql(null, namespace, username, password, query, timeout, null));
		Assertions.assertThrows(IllegalArgumentException.class, () -> WbemExecutor.executeWql(url, namespace, null, password, query,  timeout, null));
		Assertions.assertThrows(IllegalArgumentException.class, () -> WbemExecutor.executeWql(url, namespace, username, null, query, timeout, null));
		Assertions.assertThrows(IllegalArgumentException.class, () -> WbemExecutor.executeWql(url, namespace, username, password, null, timeout, null));
		Assertions.assertThrows(TimeoutException.class, () -> WbemExecutor.executeWql(url, namespace, username, password, query, 0, null));

		final WbemQueryResult result = Mockito.mock(WbemQueryResult.class);
		final Future<WbemQueryResult> handler = Mockito.mock(Future.class);
		final ExecutorService executor = Mockito.mock(ExecutorService.class);

		try (final MockedStatic<Executors> mockedExecutorService = Mockito.mockStatic(Executors.class)) {
			final WBEMException wbemException = new WBEMException("msg");
			final ExecutionException executionException = new ExecutionException(wbemException);

			mockedExecutorService.when(Executors::newSingleThreadExecutor).thenReturn(executor);

			Mockito.doReturn(handler).when(executor).submit(ArgumentMatchers.any(Callable.class));
			Mockito.doThrow(executionException).when(handler).get(timeout, TimeUnit.MILLISECONDS);

			Assertions.assertThrows(
					WBEMException.class,
					() -> WbemExecutor.executeWql(url, namespace, username, password, query, timeout, null));
		}

		try (final MockedStatic<Executors> mockedExecutorService = Mockito.mockStatic(Executors.class)) {
			final IllegalArgumentException illegalArgumentException = new IllegalArgumentException("msg");
			final ExecutionException executionException = new ExecutionException(illegalArgumentException);

			mockedExecutorService.when(Executors::newSingleThreadExecutor).thenReturn(executor);

			Mockito.doReturn(handler).when(executor).submit(ArgumentMatchers.any(Callable.class));
			Mockito.doThrow(executionException).when(handler).get(timeout, TimeUnit.MILLISECONDS);

			Assertions.assertThrows(
					RuntimeException.class,
					() -> WbemExecutor.executeWql(url, namespace, username, password, query, timeout, null));
		}

		try (final MockedStatic<Executors> mockedExecutorService = Mockito.mockStatic(Executors.class)) {
			mockedExecutorService.when(Executors::newSingleThreadExecutor).thenReturn(executor);

			Mockito.doReturn(handler).when(executor).submit(ArgumentMatchers.any(Callable.class));
			Mockito.doReturn(result).when(handler).get(timeout, TimeUnit.MILLISECONDS);

			Assertions.assertEquals(
					result,
					WbemExecutor.executeWql(url, namespace, username, password, query, timeout, null));
		}
	}
}
