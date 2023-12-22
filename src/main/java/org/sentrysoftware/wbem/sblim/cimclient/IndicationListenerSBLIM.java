/*
  (C) Copyright IBM Corp. 2012

  THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE
  ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE
  CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.

  You can obtain a current copy of the Eclipse Public License from
  http://www.opensource.org/licenses/eclipse-1.0.php

  @author : Alexander Wolf-Reber, IBM, a.wolf-reber@de.ibm.com
 * @author : Dave Blaschke, IBM, blaschke@us.ibm.com
 * 
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 3477087    2012-01-23  blaschke-oss Need Access to an Indication Sender's IP Address                  
 */

package org.sentrysoftware.wbem.sblim.cimclient;

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
import java.util.EventListener;

import org.sentrysoftware.wbem.javax.cim.CIMInstance;

/**
 * This interface is implemented by the code that wants to create a listener for
 * indications. See the <code>WBEMListenerFactory</code> class for an example.
 * 
 * The difference between this interface and <code>IndicationListener</code> is
 * that the JSR48 standard (<code>javax.wbem.listener.IndicationListener</code>)
 * does not allow for the listener to receive the IP of the indication sender
 * whereas this internal interface (
 * <code>org.sblim.cimclinet.IndicationListenerSBLIM</code>) does.
 */
public interface IndicationListenerSBLIM extends EventListener {

	/**
	 * Called when an indication has been received by the listener
	 * 
	 * @param pIndicationURL
	 *            The URL to which the indication was posted. For example if the
	 *            indication was delivered over the https protocol to the
	 *            destination listener https://hostname:6111/, pIndicationURL
	 *            would be set to https://hostname:6111/.
	 * @param pIndication
	 *            The indication received.
	 * @param pSenderIP
	 *            The internet address of the indication sender.
	 */
	public void indicationOccured(String pIndicationURL, CIMInstance pIndication,
			InetAddress pSenderIP);

}
