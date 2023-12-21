/*
  (C) Copyright IBM Corp. 2007, 2009

  THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE
  ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE
  CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.

  You can obtain a current copy of the Eclipse Public License from
  http://www.opensource.org/licenses/eclipse-1.0.php

  @author : Endre Bak, IBM, ebak@de.ibm.com
 * 
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 1804402    2007-09-28  ebak         IPv6 ready SLP
 * 1892103    2008-02-12  ebak         SLP improvements
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2)
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 */

package org.sentrysoftware.wbem.sblim.slp.internal.msg;

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
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.sentrysoftware.wbem.sblim.slp.internal.msg.SLPMessage;
import org.sentrysoftware.wbem.sblim.slp.ServiceLocationException;

/**
 * RequestMessage
 * 
 */
public abstract class RequestMessage extends SLPMessage {

	private SortedSet<String> iPrevResponderSet;

	private List<String> iScopeList;

	/**
	 * Ctor.
	 * 
	 * @param pFunctionID
	 * @param pPrevResponderSet
	 *            - set of address strings
	 * @param pScopeList
	 *            - list of scope strings
	 */
	public RequestMessage(int pFunctionID, SortedSet<String> pPrevResponderSet,
			List<String> pScopeList) {
		super(pFunctionID);
		init(pPrevResponderSet, pScopeList);
	}

	/**
	 * Ctor.
	 * 
	 * @param pFunctionID
	 * @param pLangTag
	 * @param pPrevResponderSet
	 *            - set of address strings
	 * @param pScopeList
	 *            - list of scope strings
	 */
	public RequestMessage(int pFunctionID, String pLangTag, SortedSet<String> pPrevResponderSet,
			List<String> pScopeList) {
		super(pFunctionID, pLangTag);
		init(pPrevResponderSet, pScopeList);
	}

	/**
	 * Ctor.
	 * 
	 * @param pHeader
	 * @param pPrevResponderSet
	 *            - set of address strings
	 * @param pScopeList
	 *            - list of scope strings
	 */
	public RequestMessage(MsgHeader pHeader, SortedSet<String> pPrevResponderSet,
			List<String> pScopeList) {
		super(pHeader);
		init(pPrevResponderSet, pScopeList);
	}

	/**
	 * getPrevResponderSet
	 * 
	 * @return SortedSet
	 */
	public SortedSet<String> getPrevResponderSet() {
		return this.iPrevResponderSet;
	}

	/**
	 * getPrevRespondersItr
	 * 
	 * @return Iterator
	 */
	public Iterator<String> getPrevRespondersItr() {
		return this.iPrevResponderSet == null ? null : this.iPrevResponderSet.iterator();
	}

	/**
	 * updatePrevResponders
	 * 
	 * @param pResponder
	 * @return boolean
	 */
	public boolean updatePrevResponders(String pResponder) {
		if (this.iPrevResponderSet == null) this.iPrevResponderSet = new TreeSet<String>();
		return this.iPrevResponderSet.add(pResponder);
	}

	/**
	 * getScopeList
	 * 
	 * @return List of scope strings
	 */
	public List<String> getScopeList() {
		return this.iScopeList;
	}

	/**
	 * isAllowedResponseType
	 * 
	 * @param pRspMsg
	 * @return boolean
	 */
	public boolean isAllowedResponseType(SLPMessage pRspMsg) {
		if (pRspMsg == null) return false;
		int id = pRspMsg.getFunctionID();
		int[] rspIDs = getAllowedResponseIDs();
		if (rspIDs == null) return true;
		for (int i = 0; i < rspIDs.length; i++)
			if (id == rspIDs[i]) return true;
		return false;
	}

	/**
	 * serializeWithoutResponders
	 * 
	 * @param pSetMulticastFlag
	 * @param pDatagramLimited
	 * @param pKeepXID
	 * @return byte[]
	 * @throws ServiceLocationException
	 */
	public byte[] serializeWithoutResponders(boolean pSetMulticastFlag, boolean pDatagramLimited,
			boolean pKeepXID) throws ServiceLocationException {

		return serialize(pSetMulticastFlag, pDatagramLimited, pKeepXID, new SkipResponders());
	}

	@Override
	protected boolean serializeBody(SLPOutputStream pOutStr, SerializeOption pSkipResponders)
			throws ServiceLocationException {
		if (!pOutStr.writeStringList(pSkipResponders == null ? getPrevRespondersItr() : null)) throw new ServiceLocationException(
				ServiceLocationException.PREVIOUS_RESPONDER_OVERFLOW,
				"Previous responder list has overflowed!");
		return serializeRequestBody(pOutStr);
	}

	protected abstract boolean serializeRequestBody(SLPOutputStream pOutStr)
			throws ServiceLocationException;

	protected abstract int[] getAllowedResponseIDs();

	private void init(SortedSet<String> pPrevResponderSet, List<String> pScopeList) {
		this.iPrevResponderSet = pPrevResponderSet;
		this.iScopeList = pScopeList;
	}

	class SkipResponders extends SerializeOption {
		/*
		 * non-null instance indicates that the PreviousResponderList
		 * serialization have to be skipped
		 */
	}

}
