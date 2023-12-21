/*
  (C) Copyright IBM Corp. 2005, 2009

  THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE
  ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE
  CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.

  You can obtain a current copy of the Eclipse Public License from
  http://www.opensource.org/licenses/eclipse-1.0.php

  @author : Roberto Pineiro, IBM, roberto.pineiro@us.ibm.com
 * @author : Chung-hao Tan, IBM, chungtan@us.ibm.com
 * 
 * 
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 1535756    2006-08-07  lupusalex    Make code warning free
 * 1565892    2006-11-28  lupusalex    Make SBLIM client JSR48 compliant
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2204488 	  2008-10-28  raman_arora  Fix code to remove compiler warnings
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 */

package org.sentrysoftware.wbem.sblim.cimclient.internal.http;

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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import org.sentrysoftware.wbem.sblim.cimclient.internal.http.io.ASCIIPrintStream;

/**
 * Class HttpServerMethod is responsible for
 * 
 */
public class HttpServerMethod extends HttpMethod {

	private String iMethodName;

	private String iFile;

	private String iProtocol;

	private int iMinor, iMajor;

	private int iStatus;

	private String iReason;

	/**
	 * Ctor.
	 * 
	 * @param pMajor
	 *            Major version
	 * @param pMinor
	 *            Minor version
	 * @param pStatus
	 *            Status
	 * @param pReason
	 *            Reason
	 */
	public HttpServerMethod(int pMajor, int pMinor, int pStatus, String pReason) {
		this.iMinor = pMinor;
		this.iMajor = pMajor;
		this.iStatus = pStatus;
		this.iReason = pReason;
	}

	/**
	 * Ctor.
	 * 
	 * @param pReader
	 *            Inputstream
	 * @throws IOException
	 * @throws HttpException
	 */
	public HttpServerMethod(InputStream pReader) throws IOException, HttpException {
		String line;

		do {
			line = HttpMethod.readLine(pReader);
		} while (line == null || line.length() == 0);
		int next = line.indexOf(' ');
		int prev = 0;
		if (next > -1) {
			this.iMethodName = line.substring(0, next).toUpperCase();
			if (this.iMethodName.equals("GET") && (line.indexOf(' ', next + 1) == -1)) { // Simple
				// request
				this.iFile = line.substring(next + 1);
			} else { // FullRequest
				prev = next + 1;
				next = line.indexOf(' ', prev);
				this.iFile = line.substring(prev, next);

				prev = next + 1;
				this.iProtocol = line.substring(prev).toUpperCase();

				prev = this.iProtocol.indexOf('/');
				next = this.iProtocol.indexOf('.', prev + 1);
				try {
					this.iMajor = Integer.parseInt(this.iProtocol.substring(prev + 1, next));
					this.iMinor = Integer.parseInt(this.iProtocol.substring(next + 1));
				} catch (Exception e) {
					throw new HttpException(HttpURLConnection.HTTP_BAD_METHOD, "Bad method");
				}
			}
		} else throw new HttpException(HttpURLConnection.HTTP_BAD_METHOD, "Bad method");
	}

	/**
	 * Returns the major version
	 * 
	 * @return The major version
	 */
	public int getMajorVersion() {
		return this.iMajor;
	}

	/**
	 * Returns the minor version
	 * 
	 * @return The minor version
	 */
	public int getMinorVersion() {
		return this.iMinor;
	}

	/**
	 * Returns the method name
	 * 
	 * @return The method name
	 */
	public String getMethodName() {
		return this.iMethodName;
	}

	/**
	 * Returns the file
	 * 
	 * @return The file
	 */
	public String getFile() {
		return this.iFile;
	}

	/**
	 * Write to a given output stream
	 * 
	 * @param pStream
	 *            the output stream
	 */
	public void write(ASCIIPrintStream pStream) {
		pStream.print("HTTP/" + this.iMajor + "." + this.iMinor + " " + this.iStatus + " "
				+ this.iReason + "\r\n");
	}
}
