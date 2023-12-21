/*
  (C) Copyright IBM Corp. 2009, 2013

  THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE
  ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE
  CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.

  You can obtain a current copy of the Eclipse Public License from
  http://www.opensource.org/licenses/eclipse-1.0.php

  @author : Ramandeep S Arora, IBM, arorar@us.ibm.com
 * 
 * Flag       Date        Prog         Description
 * ---------------------------------------------------------------------------
 * 2878054    2009-10-25  raman_arora  Pull Enumeration Feature (PULL Parser)
 *    2666    2013-09-19  blaschke-oss CR12: Remove ENUMERATIONCONTEXT
 */

package org.sentrysoftware.wbem.sblim.cimclient.internal.wbem;

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
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.sentrysoftware.wbem.javax.cim.CIMArgument;
import org.sentrysoftware.wbem.javax.cim.CIMObjectPath;
import org.sentrysoftware.wbem.javax.wbem.CloseableIterator;
import org.sentrysoftware.wbem.javax.wbem.WBEMException;
import org.sentrysoftware.wbem.javax.wbem.client.EnumerateResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * Class EnumerateResponsePULL is responsible for all helper functions of PULL
 * parser related with EnumerateResponse.
 * 
 * @param <T>
 */
public class EnumerateResponsePULL<T> {

	private EnumerateResponse<T> enumResponse;

	/**
	 * Ctor.
	 * 
	 * @param pStream
	 *            Input stream to be parsed
	 * @param pPath
	 *            CIMObject path
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws WBEMException
	 */
	@SuppressWarnings("unchecked")
	public EnumerateResponsePULL(InputStreamReader pStream, CIMObjectPath pPath)
			throws IOException, SAXException, ParserConfigurationException, WBEMException {

		String enumContext = null;
		Boolean endOfSequence = null;
		ArrayList<T> list = new ArrayList<T>();

		CloseableIterator<?> iter = new CloseableIteratorPULL(pStream, pPath);

		// iterate through 'iter' for getCIMArguments to populate
		try {
			while (iter.hasNext())
				list.add((T) iter.next());
		} catch (RuntimeException e) {
			iter.close();
			if (e.getCause() != null && e.getCause() instanceof WBEMException) { throw (WBEMException) e
					.getCause(); }
			throw e;
		}

		// pOutArgA can never be null
		CIMArgument<?>[] pOutArgA = ((CloseableIteratorPULL) iter).getCIMArguments();
		if (pOutArgA == null) { throw new IllegalArgumentException(
				"Output auguments not found during CIM-XML PULL parser"); }

		for (int i = 0; i < pOutArgA.length; i++) {
			if (pOutArgA[i].getName().equals("EnumerationContext")) enumContext = (String) pOutArgA[i]
					.getValue();
			else if (pOutArgA[i].getName().equals("EndOfSequence")) endOfSequence = (Boolean) pOutArgA[i]
					.getValue();
			else throw new IllegalArgumentException(
					"Invalid argument : only EnumerationContext and EndOfSequence are allowed");
		}
		// EndOfSequence can never be null
		if (endOfSequence == null) { throw new IllegalArgumentException(
				"Invalid argument : EndOfSequence can never be null"); }

		// EnumerationContext can't be null if there is more data available
		if ((endOfSequence.booleanValue() == false) && (enumContext == null)) { throw new IllegalArgumentException(
				"Invalid argument : EnumerationContext cannot be null if there is more data available"); }

		// create new closeableIterator as we cannot reuse 'iter'
		CloseableIterator<T> iterPull = (CloseableIterator<T>) new CloseableIteratorGeneric<T>(list
				.iterator(), iter.getWBEMException());

		this.enumResponse = new EnumerateResponse<T>(enumContext, iterPull, endOfSequence
				.booleanValue());
	}

	/**
	 * Returns enumResponse
	 * 
	 * @return The value of enumResponse.
	 */
	public EnumerateResponse<T> getEnumResponse() {
		return this.enumResponse;
	}

}
