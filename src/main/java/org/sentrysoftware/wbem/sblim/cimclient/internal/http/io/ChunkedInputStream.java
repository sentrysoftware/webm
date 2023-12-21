/*
  (C) Copyright IBM Corp. 2005, 2013

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
 * 13799      2004-12-07  thschaef     Fixes on chunking
 * 1535756    2006-08-07  lupusalex    Make code warning free
 * 1565892    2006-11-28  lupusalex    Make SBLIM client JSR48 compliant
 * 1660575    2007-02-15  lupusalex    Chunking broken on SUN JRE
 * 1688273    2007-04-16  ebak         Full support of HTTP trailers
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2204488 	  2008-10-28  raman_arora  Fix code to remove compiler warnings
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 3304058    2011-05-20  blaschke-oss Use same date format in change history
 * 3557283    2012-11-05  blaschke-oss Print full response when get EOF from CIMOM
 * 3601894    2013-01-23  blaschke-oss Enhance HTTP and CIM-XML tracing
 *    2621    2013-02-23  blaschke-oss Not all chunked input has trailers
 *    2709    2013-11-13  blaschke-oss Lower the level of the EOF message to FINE
 */
package org.sentrysoftware.wbem.sblim.cimclient.internal.http.io;

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

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.sentrysoftware.wbem.sblim.cimclient.internal.http.HttpHeader;
import org.sentrysoftware.wbem.sblim.cimclient.internal.http.HttpMethod;
import org.sentrysoftware.wbem.sblim.cimclient.internal.logging.LogAndTraceBroker;

/**
 * Class ChunkedInputStream implements an input stream for chunked messages
 * 
 */
public class ChunkedInputStream extends InputStream {

	private InputStream iIn;

	private String iTrailerFields;

	private String iOrigin;

	private long iChunkSize = 0;

	private boolean iEof = false;

	private HttpHeader iTrailers = new HttpHeader();

	private boolean iClosed = false;

	private byte[] iTmp = new byte[1];

	/**
	 * Ctor.
	 * 
	 * @param pStream
	 *            The stream to create this one upon
	 * @param pTrailerFields
	 *            The names of trailer fields
	 */
	public ChunkedInputStream(InputStream pStream, String pTrailerFields) {
		this(pStream, pTrailerFields, null);
	}

	/**
	 * Ctor.
	 * 
	 * @param pStream
	 *            The stream to create this one upon
	 * @param pTrailerFields
	 *            The names of trailer fields
	 * @param pOrigin
	 *            The origin of the stream (response, indication request, etc.)
	 */
	public ChunkedInputStream(InputStream pStream, String pTrailerFields, String pOrigin) {
		this.iIn = pStream;
		this.iTrailerFields = pTrailerFields;
		this.iOrigin = pOrigin == null ? "Unknown" : pOrigin;
	}

	@Override
	public synchronized int read() throws IOException {
		return (read(this.iTmp, 0, 1) > 0) ? (this.iTmp[0] & 0xFF) : -1;
	}

	@Override
	public synchronized int read(byte[] buf, int off, int len) throws IOException {
		int total = 0;
		if (this.iEof || this.iClosed) return -1; // 13799 (return -1 if closed,
		// not if !closed)

		if (this.iChunkSize == 0) {
			String line = HttpMethod.readLine(this.iIn); // TODO read line using
			// valid character encoding

			if ("".equals(line)) {
				// 13799 The chunked data is ending with
				// CRLF, so the first line read after it
				// results ""
				line = HttpMethod.readLine(this.iIn);
				// 13799 Except first chunk, the
				// above only read the CRLF !
			}
			// TODO - get rid of ";*" suffix
			try {
				this.iChunkSize = Long.parseLong(line, 16);
			} catch (Exception e) {
				LogAndTraceBroker.getBroker().trace(Level.FINER,
						"Invalid chunk size on HTTP stream", e);
				this.iEof = true;
				throw new IOException("Invalid chunk size");
			}
		}
		if (this.iChunkSize > 0) {
			total = this.iIn.read(buf, off, (this.iChunkSize < len) ? (int) this.iChunkSize
					: (int) len);
			if (total > 0) {
				this.iChunkSize -= total;
			}
			if (total == -1) {
				LogAndTraceBroker.getBroker().trace(
						Level.FINE,
						"Unexpected EOF trying to read "
								+ (this.iChunkSize < len ? this.iChunkSize : len)
								+ " bytes from HTTP chunk with remaining length of "
								+ this.iChunkSize);
				throw new EOFException("Unexpected EOF reading chunk");
			}
		} else {
			// read trailer
			this.iEof = true;
			if (this.iTrailerFields != null && this.iTrailerFields.trim().length() > 0) {
				try {
					this.iTrailers = new HttpHeader(this.iIn);
					// ebak: http trailers
					this.iTrailers.examineTrailer(this.iOrigin);
				} catch (IOException e) {
					LogAndTraceBroker.getBroker().trace(
							Level.FINE,
							"Unexpected EOF reading trailer, expected fields were "
									+ this.iTrailerFields);
					throw new EOFException("Unexpected EOF reading trailer");
				}
			}
		}
		return total > 0 ? total : -1;
	}

	/**
	 * Return the http header trailers
	 * 
	 * @return The trailers
	 */
	public synchronized HttpHeader getTrailers() {
		return this.iTrailers;
	}

	@Override
	public synchronized long skip(long total) throws IOException {
		byte[] tmp = new byte[(int) total];
		return read(tmp, 0, (int) total);
	}

	/**
	 * @return int
	 * 
	 */
	@Override
	public synchronized int available() {
		return (this.iEof ? 0 : (this.iChunkSize > 0 ? (int) this.iChunkSize : 1));
	}

	@Override
	public void close() throws IOException {
		if (!this.iClosed) {
			this.iClosed = true;
			byte[] buf = new byte[512];
			while (read(buf, 0, buf.length) > -1) {
				// empty
			}
			this.iIn.close();
		} else throw new IOException("Error while closing stream");
	}
}
