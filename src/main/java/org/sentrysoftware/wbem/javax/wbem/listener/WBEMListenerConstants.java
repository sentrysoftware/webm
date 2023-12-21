/*
  (C) Copyright IBM Corp. 2012

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
 * 3496380    2012-03-02  blaschke-oss JSR48 1.0.0: add new WBEMListenerConstants
 */
package org.sentrysoftware.wbem.javax.wbem.listener;

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

//Sync'd against JSR48 1.0.0 javadoc (build 1.6.0_18) on Thu Mar 01 12:21:26 EST 2012
/**
 * This class defines the constants used for <code>WBEMListener</code>
 * configuration. If a property is tagged as optional an implementation may or
 * may not support the property. If the implementation does not support the
 * property, it must throw an <code>IllegalArgumentException</code> if the
 * property value is attempted to be set.
 */
public class WBEMListenerConstants {

	/**
	 * Use this property to set the list of ciphers the listener will support.
	 * Setting this value to null will use the default set of ciphers provided
	 * by the version of Java being used. Optional.
	 */
	public static final String PROP_LISTENER_CIPHERS = "javax.wbem.listener.ciphers";

	/**
	 * This property along with the PROP_LISTENER_KEYSTORE_PASSWORD and
	 * PROP_LISTENER_TRUSTSTORE are used to configure mutual authentication.
	 * This property is used to provide the filename of the keystore. The path
	 * can be relative or full. Optional.
	 */
	public static final String PROP_LISTENER_KEYSTORE = "javax.wbem.listener.keyStore";

	/**
	 * This property along with the PROP_LISTENER_KEYSTORE and
	 * PROP_LISTENER_TRUSTSTORE are used to configure mutual authentication.
	 * This property is used to provide the password of the keystore. Optional.
	 */
	public static final String PROP_LISTENER_KEYSTORE_PASSWORD = "javax.wbem.listener.keyStorePassword";

	/**
	 * This property along with the PROP_LISTENER_KEYSTORE and
	 * PROP_LISTENER_KEYSTORE_PASSWORD are used to configure mutual
	 * authentication. This property is used to provide the filename of the
	 * truststore. The path can be relative or full. Optional.
	 */
	public static final String PROP_LISTENER_TRUSTSTORE = "javax.wbem.listener.trustStore";
}
