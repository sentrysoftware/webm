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
 * 1565892    2006-11-28  lupusalex    Make SBLIM client JSR48 compliant
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
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

import java.io.*;

/**
 * Class BoundedInputStream implements an input stream with a maximum byte
 * count.
 * 
 */
public class BoundedInputStream extends FilterInputStream {

	private long maxLen, used;

	private boolean closed = false;

	/**
	 * Ctor. Creates the stream with unlimited length.
	 * 
	 * @param pStream
	 *            The stream this one is build upon
	 */
	public BoundedInputStream(InputStream pStream) {
		this(pStream, -1);
	}

	/**
	 * Ctor.
	 * 
	 * @param pStream
	 *            The stream this one is build upon
	 * @param pMaximumLength
	 *            The maximum number of bytes that can be read from this stream.
	 *            A value of -1 represents unlimited mode.
	 */
	public BoundedInputStream(InputStream pStream, long pMaximumLength) {
		super(pStream);
		this.maxLen = pMaximumLength;
		this.used = 0;
		this.in = pStream;
	}

	@Override
	public int read() throws IOException {
		if (this.maxLen > -1) {
			if (this.used >= this.maxLen) return -1;

			int value = this.in.read();
			if (value > -1) this.used++;
			return value;
		}
		return this.in.read();
	}

	@Override
	public int read(byte buf[]) throws IOException {
		return read(buf, 0, buf.length);
	}

	@Override
	public int read(byte buf[], int off, int len) throws IOException {
		if (this.closed) throw new IOException("I/O error - the stream is closed");

		if (this.maxLen > -1) {
			if (this.used >= this.maxLen) return -1;

			long min = ((this.used + len) > this.maxLen) ? this.maxLen - this.used : len;
			int total = this.in.read(buf, off, (int) min);
			if (total > -1) this.used += total;
			return total;
		}
		return this.in.read(buf, off, len);
	}

	@Override
	public long skip(long len) throws IOException {
		if (this.maxLen > -1) {
			if (len >= 0) {
				long min = ((this.used + len) > this.maxLen) ? this.maxLen - this.used : len;
				long total = this.in.skip(min);
				if (total > -1) {
					this.used += total;
				}
				return total;
			}
			return -1;
		}
		return this.in.skip(len);
	}

	@Override
	public int available() throws IOException {
		if (this.maxLen > -1) { return (int) (this.maxLen - this.used); }
		return this.in.available();
	}

	@Override
	public synchronized void close() throws IOException {
		if (this.maxLen > -1) {
			if (!this.closed) {
				byte[] buf = new byte[512];
				while (read(buf, 0, buf.length) > -1) {
					// empty
				}
				this.closed = true;
			}
			// else
			// throw new IOException();
		} else {
			this.in.close();
		}
	}
}
