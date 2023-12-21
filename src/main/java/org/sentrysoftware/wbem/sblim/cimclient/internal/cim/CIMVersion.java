/*
  CIMVersion.java

  (C) Copyright IBM Corp. 2005, 2010

  THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE
  ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE
  CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.

  You can obtain a current copy of the Eclipse Public License from
  http://www.opensource.org/licenses/eclipse-1.0.php

  @author : Roberto Pineiro, IBM, roberto.pineiro@us.ibm.com
 * @author : Chung-hao Tan, IBM ,chungtan@us.ibm.com
 * 
 * Change History
 * Flag       Date        Prog         Description
 *------------------------------------------------------------------------------- 
 * 2807325    2009-06-22  blaschke-oss Change licensing from CPL to EPL
 * 2834838    2009-08-11  blaschke-oss Add interface to retrieve version number and product name
 * 3027618    2010-07-14  blaschke-oss Close files/readers in finally blocks
 */

package org.sentrysoftware.wbem.sblim.cimclient.internal.cim;

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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class CIMVersion is responsible for providing the exact version number,
 * product name, etc. of the Java CIM Client
 */
public class CIMVersion {

	private static String PRODUCT_NAME = "n/a";

	private static String VERSION = "n/a";

	private static String COPYRIGHT = "n/a";

	private static String BUILDDATE = "n/a";

	private static String BUILDTIME = "n/a";

	static {
		Properties properties = new Properties();
		InputStream versionIS = null;
		try {
			versionIS = CIMVersion.class.getClassLoader().getResourceAsStream("com/sentrysoftware/sblim/cimclient/version.txt");
			properties.load(versionIS);
			PRODUCT_NAME = properties.getProperty("PRODUCTNAME");
			VERSION = properties.getProperty("VERSION");
			COPYRIGHT = properties.getProperty("COPYRIGHT");
			BUILDDATE = properties.getProperty("BUILDDATE");
			BUILDTIME = properties.getProperty("BUILDTIME");
		} catch (FileNotFoundException e) {
			System.out.println("Error: Could not open version.txt");
		} catch (IOException e) {
			System.out.println("Error while reading version.txt");
		} finally {
			if (versionIS != null) {
				try {
					versionIS.close();
				} catch (IOException e) {
					/* Ignore exception on close */
				}
			}
		}
	}

	/**
	 * Gets the build date
	 * 
	 * @return The build date
	 */
	public static String getBuildDate() {
		return BUILDDATE;
	}

	/**
	 * Gets the build time
	 * 
	 * @return The build time
	 */
	public static String getBuildTime() {
		return BUILDTIME;
	}

	/**
	 * Gets the copyright statement
	 * 
	 * @return THe copyright
	 */
	public static String getCopyright() {
		return COPYRIGHT;
	}

	/**
	 * Gets the product name
	 * 
	 * @return The product name
	 */
	public static String getProductName() {
		return PRODUCT_NAME;
	}

	/**
	 * Gets the version
	 * 
	 * @return The version
	 */
	public static String getVersion() {
		return VERSION;
	}

	/**
	 * toString
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return (PRODUCT_NAME + "\n" + VERSION + "\n" + COPYRIGHT + "\n" + BUILDDATE + "\n" + BUILDTIME);
	}

	/**
	 * main
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(new CIMVersion());
	}
}
