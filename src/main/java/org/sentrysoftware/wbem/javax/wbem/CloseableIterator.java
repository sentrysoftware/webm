/*
  (C) Copyright IBM Corp. 2006, 2010

  THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE
  ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE
  CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.

  You can obtain a current copy of the Eclipse Public License from
  http://www.opensource.org/licenses/eclipse-1.0.php

  @author : Alexander Wolf-Reber, a.wolf-reber@de.ibm.com
 * 
 * Flag       Date        Prog         Description
 * -------------------------------------------------------------------------------
 * 1565892    2006-11-08  lupusalex    Make SBLIM client JSR48 compliant
 * 1737141    2007-06-18  ebak         Sync up with JSR48 evolution
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2) 
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 * 2912104    2009-12-10  blaschke-oss Sync up javax.wbem.* with JSR48 1.0.0
 * 2958941    2010-02-25  blaschke-oss Sync up javax.wbem.* javadoc with JSR48 1.0.0
 */
package org.sentrysoftware.wbem.javax.wbem;

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

import java.util.Iterator;

//Sync'd against JSR48 1.0.0 javadoc (build 1.5.0_10) on Wed Jan 20 02:20:59 EST 2010
/**
 * A <code>CloseableIterator</code> is a subclass of <code>Iterator</code> that
 * adds support for allowing the underlying implementation to serve up the
 * elements as they become available. The methods <code>hasNext()</code> and
 * <code>next()</code> may block while waiting for elements from the underlying
 * implementation. Since <code>next()</code> or <code>hasNext()</code> can only
 * throw runtime exceptions, if a consumer receives a runtime exception for one
 * of these methods they must call <code>getWBEMException()</code> to get the
 * actual <code>WBEMException</code>.
 * 
 * @param <E>
 *            Type parameter.
 */
public interface CloseableIterator<E> extends Iterator<E> {

	/**
	 * Closes the <code>Iterator</code>. This allows the underlying
	 * implementation to do any cleanup and disconnect from any source that it
	 * may be using.
	 */
	public void close();

	/**
	 * If <code>next()</code> or <code>hasNext()</code> throws a
	 * <code>RuntimeException</code>, this method must be called to get the
	 * <code>WBEMException</code>.
	 * 
	 * @return The <code>WBEMException</code> or null if one was not thrown.
	 */
	public WBEMException getWBEMException();

}
