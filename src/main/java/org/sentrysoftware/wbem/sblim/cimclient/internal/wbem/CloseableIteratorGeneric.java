/*
  (C) Copyright IBM Corp. 2009

  THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE
  ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE
  CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.

  You can obtain a current copy of the Eclipse Public License from
  http://www.opensource.org/licenses/eclipse-1.0.php

  @author : Ramandeep S Arora, IBM, arorar@us.ibm.com
 * 
 * Flag       Date        Prog         Description
 * --------------------------------------------------------------------------
 * 2878054    2009-10-25  raman_arora  Pull Enumeration Feature (PULL Parser)
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

import java.util.Iterator;

import org.sentrysoftware.wbem.javax.wbem.CloseableIterator;
import org.sentrysoftware.wbem.javax.wbem.WBEMException;

/**
 * Class CloseableIteratorGeneric creates new CloseableIterator from an Iterator
 * and WBEMException.
 * 
 * @param <E>
 *            : Type
 */
public class CloseableIteratorGeneric<E> implements CloseableIterator<Object> {

	private Iterator<E> iterator;

	private WBEMException iWBEMException;

	/**
	 * Ctor. : creates new CloseableIterator from an Iterator and WBEMException.
	 * 
	 * @param pIterator
	 *            : Iterator to be used in closeableIterator
	 * @param pException
	 *            : WBEMException thrown by parser (this can be null)
	 */
	public CloseableIteratorGeneric(Iterator<E> pIterator, WBEMException pException) {
		this.iterator = pIterator;
		this.iWBEMException = pException;
	}

	/**
	 * Ctor. : creates new CloseableIterator from an Iterator.
	 * 
	 * @param pIterator
	 *            : Iterator to be used in closeableIterator
	 */
	public CloseableIteratorGeneric(Iterator<E> pIterator) {
		this(pIterator, null);
	}

	public void close() {
		this.iterator = null;
		this.iWBEMException = null;
	}

	/**
	 * Returns WBEMException
	 * 
	 * @return WBEMException : This can be null
	 * 
	 */
	public WBEMException getWBEMException() {
		return this.iWBEMException;
	}

	public boolean hasNext() {
		return this.iterator.hasNext();
	}

	public Object next() {
		return this.iterator.next();
	}

	/**
	 * iterator.remove() is not supported
	 */
	public void remove() {
		throw new UnsupportedOperationException("Cannot remove elements from iterator");
	}
}
