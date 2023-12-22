/*
  (C) Copyright IBM Corp. 2006, 2010

  THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE
  ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE
  CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.

  You can obtain a current copy of the Eclipse Public License from
  http://www.opensource.org/licenses/eclipse-1.0.php

  @author : Alexander Wolf-Reber, IBM, a.wolf-reber@de.ibm.com
 * 
 * Change History
 * Flag       Date        Prog         Description
 * ------------------------------------------------------------------------------- 
 * 1516242    2006-07-05  lupusalex    Support of OpenPegasus local authentication
 * 1565892    2006-11-28  lupusalex    Make SBLIM client JSR48 compliant
 * 1710066    2007-04-30  lupsualex    LocalAuth fails for z/OS Pegasus
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2204488 	  2008-10-28  raman_arora  Fix code to remove compiler warnings
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 * 3027618    2010-07-14  blaschke-oss Close files/readers in finally blocks
 */
package org.sentrysoftware.wbem.sblim.cimclient.internal.http;

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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.logging.Level;

import org.sentrysoftware.wbem.sblim.cimclient.internal.logging.LogAndTraceBroker;
import org.sentrysoftware.wbem.sblim.cimclient.internal.logging.Messages;
import org.sentrysoftware.wbem.sblim.cimclient.internal.util.WBEMConstants;

/**
 * Implements OpenPegasus local authentication
 */
public class PegasusLocalAuthInfo extends AuthorizationInfo {

	private boolean iChallenged = false;

	/**
	 * Default ctor.
	 */
	public PegasusLocalAuthInfo() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sblim.cimclient.internal.http.AuthorizationInfo#updateAuthenticationInfo
	 * (org.sblim.cimclient.internal.http.Challenge, java.net.URI,
	 * java.lang.String)
	 */
	/**
	 * @param challenge
	 * @param authenticate
	 * @param url
	 * @param requestMethod
	 */
	@Override
	public void updateAuthenticationInfo(Challenge challenge, String authenticate, URI url,
			String requestMethod) {
		this.iChallenged = true;
		this.iResponse = authenticate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		if (this.iChallenged && this.iResponse != null && this.iResponse.startsWith("Local ")) {

			String fileName = "";
			BufferedReader in = null;

			try {

				fileName = this.iResponse.substring(7, this.iResponse.length() - 1);

				if (fileName.length() == 0) throw new IOException(
						"No local authorization file specified");

				File authorizationFile = new File(fileName);

				if (!authorizationFile.canRead()) throw new IOException(
						"Local authorization file not accessible");

				in = WBEMConstants.Z_OS.equals(System.getProperty(WBEMConstants.OS_NAME)) ? new BufferedReader(
						new InputStreamReader(new FileInputStream(authorizationFile),
								WBEMConstants.ISO_8859_1))
						: new BufferedReader(new FileReader(authorizationFile));

				StringBuffer buffer = new StringBuffer();
				String line;

				while (true) {
					line = in.readLine();
					if (line == null) break;
					buffer.append(line);
				}

				StringBuffer header = new StringBuffer();
				header.append("Local \"");
				header.append(getCredentials().getUserName());
				header.append(':');
				header.append(fileName);
				header.append(':');
				header.append(buffer);
				header.append('"');

				return header.toString();

			} catch (IOException e) {
				LogAndTraceBroker logger = LogAndTraceBroker.getBroker();
				logger.trace(Level.FINER,
						"Exception while reading OpenPegasus local authorization file", e);
				logger.message(Messages.HTTP_PEGASUS_LOCAL_AUTH_READ, fileName);
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						LogAndTraceBroker.getBroker().trace(Level.FINER,
								"Exception while closing OpenPegasus local authorization file", e);
					}
				}
			}
		}

		return "Local \"" + getCredentials().getUserName() + "\"";
	}

	@Override
	public String getHeaderFieldName() {
		return "PegasusAuthorization";
	}

	@Override
	public boolean isSentOnFirstRequest() {
		return true;
	}

	@Override
	public boolean isKeptAlive() {
		return true;
	}

}
