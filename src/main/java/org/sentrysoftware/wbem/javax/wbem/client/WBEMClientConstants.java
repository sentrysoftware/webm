/*
  (C) Copyright IBM Corp. 2009, 2012

  THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE
  ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE
  CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.

  You can obtain a current copy of the Eclipse Public License from
  http://www.opensource.org/licenses/eclipse-1.0.php

  @author : Dave Blaschke, blaschke@us.ibm.com
 * 
 * Change History
 * Flag       Date        Prog         Description
 * -------------------------------------------------------------------------------
 * 2882448    2009-10-21  blaschke-oss Add WBEMClientConstants from JSR48
 * 2884718    2009-10-23  blaschke-oss Merge JSR48 and SBLIM client properties
 * 2930341    2010-01-12  blaschke-oss Sync up WBEMClientConstants with JSR48 1.0.0
 * 2959264    2010-02-25  blaschke-oss Sync up javax.client.* javadoc with JSR48 1.0.0
 * 3496301    2012-03-02  blaschke-oss Sync up javax.* javadoc with JSR48 1.0.0 Final
 * 3496355    2012-03-02  blaschke-oss JSR48 1.0.0: add new WBEMClientConstants
 * 3521157    2012-05-10  blaschke-oss JSR48 1.0.0: PROP_ENABLE_*_LOGGING is Level, not 0/1
 */
package org.sentrysoftware.wbem.javax.wbem.client;

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

//Sync'd against JSR48 1.0.0 javadoc (build 1.6.0_18) on Thu Mar 01 12:21:26 EST 2012
/**
 * This class defines the constants used for <code>WBEMClient</code>
 * configuration. If a property is tagged as optional an implementation may or
 * may not support the property. If the implementation does not support the
 * property, it must throw an <code>IllegalArgumentException</code> if the
 * property value is attempted to be set.
 */
public class WBEMClientConstants {

	/**
	 * Use this property to set the list of ciphers the client will support.
	 * Setting this value to null will use the default set of ciphers provided
	 * by the version of Java being used. Optional.
	 */
	public static final String PROP_CLIENT_CIPHERS = "javax.wbem.client.ciphers";

	/**
	 * This property along with the PROP_CLIENT_KEYSTORE_PASSWORD and
	 * PROP_CLIENT_TRUSTSTORE are used to configure mutual authentication. This
	 * property is used to provide the filename of the keystore. The path can be
	 * relative or full. Optional.
	 */
	public static final String PROP_CLIENT_KEYSTORE = "javax.wbem.client.keyStore";

	/**
	 * This property along with the PROP_CLIENT_KEYSTORE and
	 * PROP_CLIENT_TRUSTSTORE are used to configure mutual authentication. This
	 * property is used to provide the password of the keystore. Optional.
	 */
	public static final String PROP_CLIENT_KEYSTORE_PASSWORD = "javax.wbem.client.keyStorePassword";

	/**
	 * This property along with the PROP_CLIENT_KEYSTORE and
	 * PROP_CLIENT_KEYSTORE_PASSWORD are used to configure mutual
	 * authentication. This property is used to provide the filename of the
	 * truststore. The path can be relative or full. Optional.
	 */
	public static final String PROP_CLIENT_TRUSTSTORE = "javax.wbem.client.trustStore";

	/**
	 * The value for this property is the level of debug requested.
	 */
	public static final String PROP_ENABLE_CONSOLE_LOGGING = "javax.wbem.client.log.console.enabled";

	/**
	 * The value for this property is the level of debug requested.
	 */
	public static final String PROP_ENABLE_FILE_LOGGING = "javax.wbem.client.log.file.enabled";

	/**
	 * The maximum size in bytes for each log file. The default is 5MB. Note
	 * that when the last entry is written, it may go past the limit.
	 */
	public static final String PROP_LOG_BYTE_LIMIT = "javax.wbem.client.log.maxfilesize";

	/**
	 * Set this property to the directory where the log files are created. The
	 * default is the directory in which the WBEM client program is run.
	 */
	public static final String PROP_LOG_DIR = "javax.wbem.client.log.dir";

	/**
	 * The name of the client log file. For a WBEM client using the CIM-XML or
	 * WS-Management protocol the default is cimclient_log_N.txt where N is the
	 * logfile number. The first client log file number is 0.
	 */
	public static final String PROP_LOG_FILENAME = "javax.wbem.client.log.filename";

	/**
	 * The number of log files that will be used. They will be used in
	 * round-robin fashion. The default is 3.
	 */
	public static final String PROP_LOG_NUM_FILES = "javax.wbem.client.log.numfiles";

	/**
	 * The timeout for the client to wait for connections. This value is in
	 * milliseconds. The default is 0 - unlimited. This value must be a valid
	 * integer.
	 */
	public static final String PROP_TIMEOUT = "javax.wbem.client.timeout";

	/**
	 * This property will enable HTTP chunking for a client request. Set this
	 * property to "1" to use chunking. Set to "0" to not use chunking. The
	 * default is 1. Optional.
	 */
	public static final String PROPERTY_WBEM_CHUNKING = "javax.wbem.chunking";

	/**
	 * The CIM-XML Protocol as defined by the DMTF in the following
	 * specifications:
	 * 
	 * <pre>
	 * DSP0200 - CIM Operations over HTTP 
	 * DSP0201 - Representation of CIM Using XML 
	 * DSP0203 - CIM DTD
	 * 
	 */
	public static final String PROTOCOL_CIMXML = "CIM-XML";

	/**
	 * The WS-Management Protocol as defined by the DMTF in the following
	 * specifications:
	 * 
	 * <pre>
	 * DSP0226 - WS-Management 
	 * DSP0227 - WS-Management CIM Binding Specification 
	 * DSP0230 - WS-CIM Mapping Specification
	 * 
	 */
	public static final String PROTOCOL_WSMANAGEMENT = "WS-Management";
}
