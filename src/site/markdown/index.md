# WBEM Java Utility

The Web-Based Entreprise Management (WBEM) Java Client is a library that enables to:
* Connect to a WBEM Server
* Execute WQL (WBEM Query Language) queries such as EnumerateInstances
It uses HTTP/HTTPS protocol for that purpose.

# How to run the WBEM Client inside Java

Add WBEM in the list of dependencies in your [Maven **pom.xml**](https://maven.apache.org/pom.html):

```xml
<dependencies>
	<!-- [...] -->
	<dependency>
		<groupId>${project.groupId}</groupId>
		<artifactId>${project.artifactId}</artifactId>
		<version>${project.version}</version>
	</dependency>
</dependencies>
```

Add the following `opens` and `exports` into the arguments:
```properties
--add-exports java.xml/com.sun.org.apache.xerces.internal.parsers
--add-opens java.base/java.lang java.base/java.util java.base/sun.net.www.protocol.http java.base/sun.security.ssl
```

Use it as follows:
```Java
package org.sentrysoftware.wbem;

import java.net.MalformedURLException;
import java.net.URL;
import org.sentrysoftware.wbem.client.WbemClient;
import org.sentrysoftware.wbem.client.WqlQuery;
import org.sentrysoftware.wbem.javax.wbem.WBEMException;
import org.sentrysoftware.wbem.utils.exceptions.WqlQuerySyntaxException;

public class Main {

	public static void main(String[] args) throws MalformedURLException, WBEMException, WqlQuerySyntaxException {

		// Connection parameters
		final URL url = new URL("my-url");
		final String username = "my-username";
		final char[] password = new char[] { 'P', 'a', 's', 's'};
		final int timeout = 60 * 1000; // in milliseconds

		// Create a new WBEM Client
		final WbemClient wbemClient = new WbemClient();

		// Connect to the WBEM Server
		wbemClient.connect(url, username, password, timeout);

		// Create a WQL Query
		final WqlQuery queryHandler = WqlQuery
				.parseQuery("SELECT ElementName, Name, OperationalStatus from EMC_StorageSystem");
		final String namespace = "root/emc";

		// Execute the WQL Query and print the result
		wbemClient.executeWql(queryHandler, namespace, null).getValues().forEach(System.out::println);

		// Close the connection
		wbemClient.close();

	}
}

```