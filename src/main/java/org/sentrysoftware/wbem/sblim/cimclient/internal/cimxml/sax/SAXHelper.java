/*
  (C) Copyright IBM Corp. 2006, 2009

  THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE
  ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE
  CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.

  You can obtain a current copy of the Eclipse Public License from
  http://www.opensource.org/licenses/eclipse-1.0.php

  @author : Endre Bak, ebak@de.ibm.com
 * 
 * Flag       Date        Prog         Description
 * -------------------------------------------------------------------------------
 * 1565892    2006-12-04  ebak         Make SBLIM client JSR48 compliant
 * 1660756    2007-02-22  ebak         Embedded object support
 * 1688273    2007-04-16  ebak         Full support of HTTP trailers
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2797550    2009-06-01  raman_arora  JSR48 compliance - add Java Generics
 */

package org.sentrysoftware.wbem.sblim.cimclient.internal.cimxml.sax;

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

import java.io.InputStreamReader;

import org.sentrysoftware.wbem.javax.cim.CIMArgument;
import org.sentrysoftware.wbem.javax.cim.CIMObjectPath;
import org.sentrysoftware.wbem.javax.wbem.WBEMException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.sentrysoftware.wbem.sblim.cimclient.internal.cimxml.sax.node.AbstractMessageNode;
import org.sentrysoftware.wbem.sblim.cimclient.internal.cimxml.sax.node.CIMNode;
import org.sentrysoftware.wbem.sblim.cimclient.internal.cimxml.sax.node.MessageNode;
import org.sentrysoftware.wbem.sblim.cimclient.internal.cimxml.sax.node.SimpleRspNode;
import org.sentrysoftware.wbem.sblim.cimclient.internal.http.io.TrailerException;
import org.sentrysoftware.wbem.sblim.cimclient.internal.wbem.CIMError;
import org.xml.sax.InputSource;

/**
 * Class SAXHelper stores methods to help high level CIM-XML pull and SAX
 * parsing.
 */
public class SAXHelper {

	/**
	 * parseInvokeMethodResponse
	 * 
	 * @param pIs
	 * @param pOutArgs
	 * @param pDefPath
	 * @return Object, any kind of JSR48 value class
	 * @throws Exception
	 */
	public static Object parseInvokeMethodResponse(InputStreamReader pIs,
			CIMArgument<?>[] pOutArgs, CIMObjectPath pDefPath) throws Exception {
		XMLDefaultHandlerImpl hndlr = new XMLDefaultHandlerImpl(pDefPath);
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		try {
			saxParser.parse(new InputSource(pIs), hndlr);
		} catch (TrailerException e) {
			throw e.getWBEMException();
		}

		CIMNode cimNode = hndlr.getCIMNode();
		MessageNode msgNode = cimNode.getMessageNode();
		AbstractMessageNode absMsgNode = msgNode.getAbstractMessageNode();
		SimpleRspNode simpRspNode = (SimpleRspNode) absMsgNode;
		CIMError cimErr = simpRspNode.getCIMError();
		if (cimErr != null) throw new WBEMException(cimErr.getCode(), cimErr.getDescription(),
				cimErr.getCIMInstances());
		CIMArgument<?>[] outArgs = simpRspNode.getCIMArguments();
		if (pOutArgs != null && outArgs != null) {
			int len = Math.min(pOutArgs.length, outArgs.length);
			for (int i = 0; i < len; i++)
				pOutArgs[i] = outArgs[i];
		}
		return simpRspNode.getReturnValueCount() == 0 ? null : simpRspNode.readReturnValue();
	}

}
