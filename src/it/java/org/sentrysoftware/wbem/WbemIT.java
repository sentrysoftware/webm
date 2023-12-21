package org.sentrysoftware.wbem;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.jboss.weld.context.RequestContext;
import org.jboss.weld.context.unbound.UnboundLiteral;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import com.sentrysoftware.emulation.image.EmulatorImageConfiguration;
import com.sentrysoftware.emulation.server.EmulatorServer;
import com.sentrysoftware.matsya.QueryResponse;
import com.sentrysoftware.matsya.Utils;

import picocli.CommandLine;

class WbemIT {

	private static final String NAMESPACE = "root/hitachi/smis";
	private static final String USER = "user";
	private static final char[] PASSWORD = {'p', 'w', 'd'};

	private static final String IT_RESOURCES = Paths.get("src", "it", "resources").toAbsolutePath().toString();
	private static final String RESULTS_DIRECTORY = Paths.get(IT_RESOURCES, "results").toAbsolutePath().toString();

	private static final Weld WELD = new Weld();

	@BeforeAll
	static void startEmulator() throws Exception {

		final WeldContainer weldContainer = WELD.initialize();

		final RequestContext requestContext = weldContainer.select(RequestContext.class, UnboundLiteral.INSTANCE).get();
		requestContext.activate();

		final Path imagePath = Paths.get(IT_RESOURCES, "image");

		final EmulatorImageConfiguration emulatorImageConfiguration = weldContainer.select(EmulatorImageConfiguration.class).get();
		emulatorImageConfiguration.setImage(imagePath.toAbsolutePath().toString());

		try (final EmulatorServer emulatorServer= weldContainer.select(EmulatorServer.class).get()) {
			final Thread thread = new Thread(emulatorServer);
			thread.start();
		}
	}

	@AfterAll
	static void stopEmulator() {
		WELD.shutdown();
	}

	@Test
	@EnabledOnOs({OS.LINUX, OS.WINDOWS})
	void testWbemCliMainLinux() throws Exception {

		try(final StringWriter stringWriter = new StringWriter();
				final PrintWriter printWriter = new PrintWriter(stringWriter)) {

			final String[] args = {
					"--hostname", "localhost",
					"--namespace", NAMESPACE,
					"--username", USER,
					"--password", String.valueOf(PASSWORD),
					"--query", "Select * from HITACHI_FCPort"};

			// Execute
			new CommandLine(new WbemCliMain())
			.setOut(printWriter)
			.setErr(printWriter)
			.execute(args);

			final List<String> expected = Files.readAllLines(Paths.get(RESULTS_DIRECTORY, "cliResponse_HITACHI_FCPort.txt"));

			Assertions.assertEquals(
					expected.stream().collect(Collectors.joining(System.lineSeparator())),
					stringWriter.toString());
		}
	}

	@Test
	void testWbemQueryProcessor() throws Exception {

		final String matsyaQuery =
				"<operation>WQL</operation>\n" +
				"<Protocol>https</Protocol>\n" +
				"<Hostname>localhost</Hostname>\n" +
				"<Port>5989</Port>\n" +
				"<Username>user</Username>\n" +
				"<Password>pwd</Password>\n" +
				"<Namespace>root/hitachi/smis</Namespace>\n" +
				"<wql>SELECT Name,PermanentAddress,PortType,MaxSpeed,OperationalStatus,__Path FROM HITACHI_FCPort</wql>\n" +
				"<columnseparator>;</columnseparator>\n" +
				"<arrayseparator>|</arrayseparator>";

		final QueryResponse queryResponse = new QueryResponse(Utils.EMPTY, 0);

		new WbemQueryProcessor().executeQuery(matsyaQuery, queryResponse, null);

		final List<String> expected = Files.readAllLines(Paths.get(RESULTS_DIRECTORY, "queryResponse_HITACHI_FCPort.txt"));

		Assertions.assertEquals(
				expected.stream().collect(Collectors.joining(Utils.NEW_LINE)),
				queryResponse.getContent().toString());
	}

	@Test
	void testWbemExecutor() throws Exception {

		final WbemQueryResult wbemQueryResult = WbemExecutor.executeWql(
				new URL("https://localhost:5989"),
				NAMESPACE,
				USER,
				PASSWORD,
				"Select Name, __Path, Name from HITACHI_FCPort",
				30 * 1000,
				"|");

		Assertions.assertNotNull(wbemQueryResult);

		Assertions.assertEquals(Arrays.asList("Name", "__Path", "Name"), wbemQueryResult.getProperties());

		Assertions.assertEquals(
				Arrays.asList(
						Arrays.asList("CL3-A", "HITACHI_FCPort.CreationClassName=\"HITACHI_FCPort\",DeviceID=\"50060E8022393620\",SystemCreationClassName=\"HITACHI_StorageProcessorSystem\",SystemName=\"VSP G200.480182.0\"", "CL3-A"),
						Arrays.asList("CL5-A", "HITACHI_FCPort.CreationClassName=\"HITACHI_FCPort\",DeviceID=\"50060E8022393640\",SystemCreationClassName=\"HITACHI_StorageProcessorSystem\",SystemName=\"VSP G200.480182.0\"", "CL5-A"),
						Arrays.asList("CL7-A", "HITACHI_FCPort.CreationClassName=\"HITACHI_FCPort\",DeviceID=\"50060E8022393660\",SystemCreationClassName=\"HITACHI_StorageProcessorSystem\",SystemName=\"VSP G200.480182.0\"", "CL7-A"),
						Arrays.asList("CL2-A", "HITACHI_FCPort.CreationClassName=\"HITACHI_FCPort\",DeviceID=\"50060E8022393610\",SystemCreationClassName=\"HITACHI_StorageProcessorSystem\",SystemName=\"VSP G200.480182.2\"", "CL2-A"),
						Arrays.asList("CL4-A", "HITACHI_FCPort.CreationClassName=\"HITACHI_FCPort\",DeviceID=\"50060E8022393630\",SystemCreationClassName=\"HITACHI_StorageProcessorSystem\",SystemName=\"VSP G200.480182.2\"", "CL4-A"),
						Arrays.asList("CL6-A", "HITACHI_FCPort.CreationClassName=\"HITACHI_FCPort\",DeviceID=\"50060E8022393650\",SystemCreationClassName=\"HITACHI_StorageProcessorSystem\",SystemName=\"VSP G200.480182.2\"", "CL6-A"),
						Arrays.asList("CL8-A", "HITACHI_FCPort.CreationClassName=\"HITACHI_FCPort\",DeviceID=\"50060E8022393670\",SystemCreationClassName=\"HITACHI_StorageProcessorSystem\",SystemName=\"VSP G200.480182.2\"", "CL8-A"),
						Arrays.asList("CL1-A", "HITACHI_FCPort.CreationClassName=\"HITACHI_FCPort\",DeviceID=\"50060E8022393600\",SystemCreationClassName=\"HITACHI_StorageProcessorSystem\",SystemName=\"VSP G200.480182.0\"", "CL1-A")),
				wbemQueryResult.getValues());
	}
}
