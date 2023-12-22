/*
  CIMEvent.java

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
 *------------------------------------------------------------------------------
 * 17931      2005-07-28  thschaef     Add InetAddress field 
 *                                     + constructor and getter
 * 1535756    2006-08-07  lupusalex    Make code warning free
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2) 
 */

package org.sentrysoftware.wbem.sblim.cimclient.internal.wbem.indications;

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

import java.net.InetAddress;

import org.sentrysoftware.wbem.javax.cim.CIMInstance;

/**
 * Class CIMEvent is required for indication handling.
 * 
 */
public class CIMEvent {

	protected CIMInstance iIndication;

	protected String iID;

	protected InetAddress iInetAddress = null;

	/**
	 * Ctor.
	 * 
	 * @param pIndication
	 */
	public CIMEvent(CIMInstance pIndication) {
		this(pIndication, null);
	}

	/**
	 * Ctor.
	 * 
	 * @param pIndication
	 * @param id
	 */
	public CIMEvent(CIMInstance pIndication, String id) {
		this.iIndication = pIndication;
		this.iID = id;
	}

	/**
	 * Constructor that takes the CIMInstance of the indication, the id as well
	 * as the InetAddress of the remote machine.
	 * 
	 * @param pIndication
	 *            The indication instance
	 * @param pId
	 *            The id
	 * @param pInetAddress
	 *            The address
	 */
	public CIMEvent(CIMInstance pIndication, String pId, InetAddress pInetAddress) {
		this.iIndication = pIndication;
		this.iID = pId;
		this.iInetAddress = pInetAddress;
	}

	/**
	 * This method returns the InetAddress of the machine that hosts the CIM
	 * Agent that sent the indication. Be aware the remote machine could have
	 * multiple network adapters - thus the result can be ambiguous.
	 * 
	 * @return The InetAddress of the remote machine
	 */
	public InetAddress getInetAddress() {
		return this.iInetAddress;
	}

	/**
	 * getIndication
	 * 
	 * @return CIMInstance
	 */
	public CIMInstance getIndication() {
		return this.iIndication;
	}

	/**
	 * getID
	 * 
	 * @return String
	 */
	public String getID() {
		return this.iID;
	}
}
