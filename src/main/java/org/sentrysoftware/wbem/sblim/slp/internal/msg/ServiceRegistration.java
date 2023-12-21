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

import java.io.IOException;
import java.util.List;

import org.sentrysoftware.wbem.sblim.slp.ServiceLocationAttribute;
import org.sentrysoftware.wbem.sblim.slp.ServiceLocationException;
import org.sentrysoftware.wbem.sblim.slp.ServiceURL;

/*
 * 0 1 2 3 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ | Service
 * Location header (function = SrvReg = 3) |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ |
 * <URL-Entry> \
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ | length of
 * service type string | <service-type> \
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ | length of
 * <scope-list> | <scope-list> \
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ | length of
 * attr-list string | <attr-list> \
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ |# of
 * AttrAuths |(if present) Attribute Authentication Blocks...\
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 */
/**
 * ServiceRegistration message
 * 
 */
public class ServiceRegistration extends SLPMessage {

	private ServiceURL iServURL;

	private List<String> iScopeList;

	private List<ServiceLocationAttribute> iAttrList;

	private List<?> iAuthBlockList;

	/**
	 * parse
	 * 
	 * @param pHdr
	 * @param pInStr
	 * @return SLPMessage
	 * @throws ServiceLocationException
	 * @throws IOException
	 */
	public static SLPMessage parse(MsgHeader pHdr, SLPInputStream pInStr)
			throws ServiceLocationException, IOException {
		ServiceURL url = pInStr.readURL();
		pInStr.readServiceType(); // FIXME reading dummy SrvType. Correct?
		return new ServiceRegistration(pHdr, url, pInStr.readStringList(), pInStr
				.readAttributeList(), pInStr.readAuthBlockList());
	}

	/**
	 * Ctor.
	 * 
	 * @param pServURL
	 * @param pScopeList
	 *            - list of scope strings
	 * @param pAttrList
	 *            - list of ServiceLocationAttributes
	 * @param pAuthBlockList
	 */
	public ServiceRegistration(ServiceURL pServURL, List<String> pScopeList,
			List<ServiceLocationAttribute> pAttrList, List<?> pAuthBlockList) {
		super(SRV_REG);
		init(pServURL, pScopeList, pAttrList, pAuthBlockList);
	}

	/**
	 * Ctor.
	 * 
	 * @param pLangTag
	 * @param pServURL
	 * @param pScopeList
	 *            - list of scope strings
	 * @param pAttrList
	 *            - list of ServiceLocationAttributes
	 * @param pAuthBlockList
	 */
	public ServiceRegistration(String pLangTag, ServiceURL pServURL, List<String> pScopeList,
			List<ServiceLocationAttribute> pAttrList, List<?> pAuthBlockList) {
		super(SRV_REG, pLangTag);
		init(pServURL, pScopeList, pAttrList, pAuthBlockList);
	}

	/**
	 * Ctor.
	 * 
	 * @param pHeader
	 * @param pServURL
	 * @param pScopeList
	 *            - list of scope strings
	 * @param pAttrList
	 *            - list of ServiceLocationAttributes
	 * @param pAuthBlockList
	 */
	public ServiceRegistration(MsgHeader pHeader, ServiceURL pServURL, List<String> pScopeList,
			List<ServiceLocationAttribute> pAttrList, List<?> pAuthBlockList) {
		super(pHeader);
		init(pServURL, pScopeList, pAttrList, pAuthBlockList);
	}

	/**
	 * getServiceURL
	 * 
	 * @return ServiceURL
	 */
	public ServiceURL getServiceURL() {
		return this.iServURL;
	}

	/**
	 * getScopeList
	 * 
	 * @return List
	 */
	public List<String> getScopeList() {
		return this.iScopeList;
	}

	/**
	 * getAttributeList
	 * 
	 * @return List
	 */
	public List<ServiceLocationAttribute> getAttributeList() {
		return this.iAttrList;
	}

	/**
	 * @param pOption
	 */
	@Override
	protected boolean serializeBody(SLPOutputStream pOutStr, SerializeOption pOption) {
		return pOutStr.write(this.iServURL) && pOutStr.write(this.iServURL.getServiceType())
				&& pOutStr.writeStringList(this.iScopeList)
				&& pOutStr.writeAttributeList(this.iAttrList)
				&& pOutStr.writeAuthBlockList(this.iAuthBlockList);
	}

	private void init(ServiceURL pServURL, List<String> pScopeList,
			List<ServiceLocationAttribute> pAttrList, List<?> pAuthBlockList) {
		this.iServURL = pServURL;
		this.iScopeList = pScopeList;
		this.iAttrList = pAttrList;
		this.iAuthBlockList = pAuthBlockList;
	}

}
