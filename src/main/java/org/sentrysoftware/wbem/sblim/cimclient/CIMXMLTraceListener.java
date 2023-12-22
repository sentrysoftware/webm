/*
  (C) Copyright IBM Corp. 2012

  THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE
  ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE
  CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.

  You can obtain a current copy of the Eclipse Public License from
  http://www.opensource.org/licenses/eclipse-1.0.php

  @author : Alexander Wolf-Reber, IBM, a.wolf-reber@de.ibm.com
 *           Dave Blaschke, IBM, blaschke@us.ibm.com
 * 
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 3554738    2012-08-16  blaschke-oss dump CIM xml by LogAndTraceBroker.trace()
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

import java.util.logging.Level;

/**
 * The interface CIMXMLTraceListener must be implemented if you want to attach
 * your own CIM-XML logging framework to the CIM Client.
 * 
 * @see LogAndTraceManager
 */
public interface CIMXMLTraceListener {

	/**
	 * Receive a CIM-XML trace message.
	 * 
	 * @param pLevel
	 *            One of the message level identifiers, e.g. FINE
	 * @param pMessage
	 *            The CIM-XML message text
	 * @param pOutgoing
	 *            <code>true</code> if CIM-XML is outgoing (being sent from
	 *            client to server), <code>false</code> if CIM-XML is incoming
	 *            (being sent from server to client)
	 */
	public void traceCIMXML(Level pLevel, String pMessage, boolean pOutgoing);

}
