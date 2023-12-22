/*
  (C) Copyright IBM Corp. 2006, 2013

  THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE
  ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE
  CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.

  You can obtain a current copy of the Eclipse Public License from
  http://www.opensource.org/licenses/eclipse-1.0.php

  @author : Endre Bak, ebak@de.ibm.com
 * 
 * Flag       Date        Prog         Description
 * -------------------------------------------------------------------------------
 * 1720707    2007-05-17  ebak         Conventional Node factory for CIM-XML SAX parser
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2204488 	  2008-10-28  raman_arora  Fix code to remove compiler warnings
 * 2210455    2008-10-30  blaschke-oss Enhance javadoc, fix potential null pointers
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2763216    2009-04-14  blaschke-oss Code cleanup: visible spelling/grammar errors
 * 2845211    2009-08-27  raman_arora  Pull Enumeration Feature (SAX Parser)
 *    2666    2013-09-19  blaschke-oss CR12: Remove ENUMERATIONCONTEXT
 *    2672    2013-09-26  blaschke-oss Remove SIMPLEREQACK support
 *    2690    2013-10-11  blaschke-oss Remove RESPONSEDESTINATION support
 *    2538    2013-11-28  blaschke-oss CR14: Support new CORRELATOR element
 */

package org.sentrysoftware.wbem.sblim.cimclient.internal.cimxml.sax;

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

/**
 * Class NodeConstIf
 * 
 */
public interface NodeConstIf {

	/**
	 * The CIM element is the root element of every XML Document that is valid
	 * with respect to this schema. <br>
	 * 
	 * 
	 * &lt;!ELEMENT CIM (MESSAGE|DECLARATION)&gt;
	 * &lt;!ATTLIST CIM
	 * 		CIMVERSION CDATA #REQUIRED
	 * 		DTDVERSION CDATA #REQUIRED&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String CIM = "CIM";

	/**
	 * The DECLARATION element defines a set of one or more declarations of CIM
	 * objects. <br>
	 * 
	 * 
	 * 
	 * &lt;!ELEMENT DECLARATION (DECLGROUP|DECLGROUP.WITHNAME|DECLGROUP.WITHPATH)+&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String DECLARATION = "DECLARATION";

	/**
	 * The DECLGROUP element defines a logical set of CIM Class, Instance and
	 * Qualifier declarations. <br>
	 * 
	 * 
	 * &lt;!ELEMENT DECLGROUP ((LOCALNAMESPACEPATH|NAMESPACEPATH)?,QUALIFIER.DECLARATION*,VALUE.OBJECT*)&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String DECLGROUP = "DECLGROUP";

	/**
	 * The DECLGROUP.WITHNAME element defines a logical set of CIM Class,
	 * Instance and Qualifier declarations. <br>
	 * 
	 * 
	 * &lt;!ELEMENT DECLGROUP.WITHNAME ((LOCALNAMESPACEPATH|NAMESPACEPATH)?,QUALIFIER.DECLARATION*,VALUE.NAMEDOBJECT*)&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String DECLGROUP_WITHNAME = "DECLGROUP.WITHNAME";

	/**
	 * The DECLGROUP.WITHPATH element defines a logical set of CIM Class and
	 * Instance declarations. <br>
	 * 
	 * 
	 * &lt;!ELEMENT DECLGROUP.WITHPATH (VALUE.OBJECTWITHPATH|VALUE.OBJECTWITHLOCALPATH)*&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String DECLGROUP_WITHPATH = "DECLGROUP.WITHPATH";

	/**
	 * The QUALIFIER.DECLARATION element defines a single CIM Qualifier
	 * declaration. <br>
	 * 
	 * 
	 * &lt;!ELEMENT QUALIFIER.DECLARATION (SCOPE?,(VALUE|VALUE.ARRAY)?)&gt;
	 * &lt;!ATTLIST QUALIFIER.DECLARATION
	 * 		%CIMName;
	 * 		%CIMType;               #REQUIRED
	 * 		ISARRAY (true|false)    #IMPLIED
	 * 		%ArraySize;
	 * 		%QualifierFlavor;&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String QUALIFIER_DECLARATION = "QUALIFIER.DECLARATION";

	/**
	 * The SCOPE element defines the scope of a QUALIFIER.DECLARATION in the
	 * case that there are restrictions on the scope of the Qualifier
	 * declaration. <br>
	 * 
	 * 
	 * &lt;!ELEMENT SCOPE EMPTY&gt;
	 * &lt;!ATTLIST SCOPE
	 * 		CLASS       (true|false) &quot;false&quot;
	 * 		ASSOCIATION (true|false) &quot;false&quot;
	 * 		REFERENCE   (true|false) &quot;false&quot;
	 * 		PROPERTY    (true|false) &quot;false&quot;
	 * 		METHOD      (true|false) &quot;false&quot;
	 * 		PARAMETER   (true|false) &quot;false&quot;
	 * 		INDICATION  (true|false) &quot;false&quot;&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String SCOPE = "SCOPE";

	/**
	 * The VALUE element is used to define a single (non-array) non-reference
	 * non-NULL CIM Property value, CIM Qualifier value, CIM Method return
	 * value, or CIM Method Parameter value. <br>
	 * 
	 * 
	 * &lt;!ELEMENT VALUE (#PCDATA)&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String VALUE = "VALUE";

	/**
	 * The VALUE.ARRAY element is used to represent the value of a CIM Property
	 * or Qualifier that has an array type. <br>
	 * 
	 * 
	 * 
	 * &lt;!ELEMENT VALUE.ARRAY (VALUE|VALUE.NULL)*&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String VALUE_ARRAY = "VALUE.ARRAY";

	/**
	 * The VALUE.REFERENCE element is used to define a single CIM reference
	 * Property value. <br>
	 * 
	 * 
	 * &lt;!ELEMENT VALUE.REFERENCE (CLASSPATH|LOCALCLASSPATH|CLASSNAME|INSTANCEPATH|LOCALINSTANCEPATH|INSTANCENAME)&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String VALUE_REFERENCE = "VALUE.REFERENCE";

	/**
	 * The VALUE.REFARRAY element is used to represent the value of an array of
	 * CIM references. <br>
	 * 
	 * 
	 * &lt;!ELEMENT VALUE.REFARRAY (VALUE.REFERENCE|VALUE.NULL)*&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String VALUE_REFARRAY = "VALUE.REFARRAY";

	/**
	 * The VALUE.OBJECT element is used to define a value which is comprised of
	 * a single CIM Class or Instance definition. <br>
	 * 
	 * 
	 * &lt;!ELEMENT VALUE.OBJECT (CLASS|INSTANCE)&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String VALUE_OBJECT = "VALUE.OBJECT";

	/**
	 * The VALUE.NAMEDINSTANCE element is used to define a value which is
	 * comprised of a single named CIM Instance definition. <br>
	 * 
	 * 
	 * 
	 * &lt;!ELEMENT VALUE.NAMEDINSTANCE (INSTANCENAME,INSTANCE)&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String VALUE_NAMEDINSTANCE = "VALUE.NAMEDINSTANCE";

	/**
	 * The VALUE.NAMEDOBJECT element is used to define a value which is
	 * comprised of a single named CIM Class or Instance definition. <br>
	 * 
	 * 
	 * &lt;!ELEMENT VALUE.NAMEDOBJECT (CLASS|(INSTANCENAME,INSTANCE))&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String VALUE_NAMEDOBJECT = "VALUE.NAMEDOBJECT";

	/**
	 * The VALUE.OBJECTWITHLOCALPATH element is used to define a value which is
	 * comprised of a single CIM Object (Class or Instance) definition with
	 * additional information that defines the local path to that Object. <br>
	 * 
	 * 
	 * &lt;!ELEMENT VALUE.OBJECTWITHLOCALPATH ((LOCALCLASSPATH,CLASS)|(LOCALINSTANCEPATH,INSTANCE))&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String VALUE_OBJECTWITHLOCALPATH = "VALUE.OBJECTWITHLOCALPATH";

	/**
	 * The VALUE.OBJECTWITHPATH element is used to define a value which is
	 * comprised of a single CIM Object (Class or Instance) definition with
	 * additional information that defines the absolute path to that Object. <br>
	 * 
	 * 
	 * &lt;!ELEMENT VALUE.OBJECTWITHPATH ((CLASSPATH,CLASS)|(INSTANCEPATH,INSTANCE))&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String VALUE_OBJECTWITHPATH = "VALUE.OBJECTWITHPATH";

	/**
	 * The VALUE.NULL element is used to represent a NULL value. <br>
	 * 
	 * 
	 * &lt;!ELEMENT VALUE.NULL EMPTY&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String VALUE_NULL = "VALUE.NULL";

	/**
	 * The VALUE.INSTANCEWITHPATH element is used to define value that comprises
	 * a single CIM instance definition with additional information that defines
	 * the absolute path to that object. <br>
	 * 
	 * 
	 * &lt;!ELEMENT VALUE.INSTANCEWITHPATH (INSTANCEPATH, INSTANCE)&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String VALUE_INSTANCEWITHPATH = "VALUE.INSTANCEWITHPATH";

	/**
	 * The NAMESPACEPATH element is used to define a Namespace Path. <br>
	 * 
	 * 
	 * &lt;!ELEMENT NAMESPACEPATH (HOST,LOCALNAMESPACEPATH)&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String NAMESPACEPATH = "NAMESPACEPATH";

	/**
	 * The LOCALNAMESPACEPATH element is used to define a local Namespace path
	 * (one without a Host component). <br>
	 * 
	 * 
	 * &lt;!ELEMENT LOCALNAMESPACEPATH (NAMESPACE+)&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String LOCALNAMESPACEPATH = "LOCALNAMESPACEPATH";

	/**
	 * The HOST element is used to define a single Host. <br>
	 * 
	 * 
	 * &lt;!ELEMENT HOST (#PCDATA)&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String HOST = "HOST";

	/**
	 * The NAMESPACE element is used to define a single Namespace component of a
	 * Namespace path. <br>
	 * 
	 * 
	 * &lt;!ELEMENT NAMESPACE EMPTY&gt;
	 * &lt;!ATTLIST NAMESPACE
	 * 		%CIMName;&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String NAMESPACE = "NAMESPACE";

	/**
	 * The CLASSPATH element defines the absolute path to a CIM Class. <br>
	 * 
	 * 
	 * &lt;!ELEMENT CLASSPATH (NAMESPACEPATH,CLASSNAME)&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String CLASSPATH = "CLASSPATH";

	/**
	 * The LOCALCLASSPATH element defines the a local path to a CIM Class. <br>
	 * 
	 * 
	 * 
	 * &lt;!ELEMENT LOCALCLASSPATH (LOCALNAMESPACEPATH, CLASSNAME)&gt;
	 * 
	 * 
	 * 
	 * @see String
	 */
	public static final String LOCALCLASSPATH = "LOCALCLASSPATH";

	/**
	 * The CLASSNAME element defines the qualifying name of a CIM Class. <br>
	 * 
	 * 
	 * &lt;!ELEMENT CLASSNAME EMPTY&gt;
	 * &lt;!ATTLIST CLASSNAME
	 * 		%CIMName;&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String CLASSNAME = "CLASSNAME";

	/**
	 * The INSTANCEPATH element defines the absolute path to a CIM Instance. <br>
	 * 
	 * 
	 * &lt;!ELEMENT INSTANCEPATH (NAMESPACEPATH,INSTANCENAME)&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String INSTANCEPATH = "INSTANCEPATH";

	/**
	 * The LOCALINSTANCEPATH element defines the local path to a CIM Instance. <br>
	 * 
	 * 
	 * &lt;!ELEMENT LOCALINSTANCEPATH (LOCALNAMESPACEPATH,INSTANCENAME)&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String LOCALINSTANCEPATH = "LOCALINSTANCEPATH";

	/**
	 * The INSTANCENAME element defines the location of a CIM Instance within a
	 * Namespace. <br>
	 * 
	 * 
	 * &lt;!ELEMENT INSTANCENAME (KEYBINDING*|KEYVALUE?|VALUE.REFERENCE?)&gt;
	 * &lt;!ATTLIST INSTANCENAME
	 * 		%ClassName;&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String INSTANCENAME = "INSTANCENAME";

	/**
	 * The OBJECTPATH element is used to define a full path to a single CIM
	 * Object (Class or Instance). <br>
	 * 
	 * 
	 * &lt;!ELEMENT OBJECTPATH (INSTANCEPATH|CLASSPATH)&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String OBJECTPATH = "OBJECTPATH";

	/**
	 * The KEYBINDING element defines a single key property value binding. <br>
	 * 
	 * 
	 * &lt;!ELEMENT KEYBINDING (KEYVALUE|VALUE.REFERENCE)&gt;
	 * &lt;!ATTLIST KEYBINDING
	 * 		%CIMName;&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String KEYBINDING = "KEYBINDING";

	/**
	 * The KEYVALUE element defines a single property key value when the key
	 * property is a non-reference type. <br>
	 * 
	 * 
	 * &lt;!ELEMENT KEYVALUE (#PCDATA)&gt;
	 * &lt;!ATTLIST KEYVALUE
	 * 		VALUETYPE (string|boolean|numeric) &quot;string&quot;
	 * 		%CIMType; #IMPLIED&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String KEYVALUE = "KEYVALUE";

	/**
	 * The CLASS element defines a single CIM Class. <br>
	 * 
	 * 
	 * &lt;!ELEMENT CLASS (QUALIFIER*,(PROPERTY|PROPERTY.ARRAY|PROPERTY.REFERENCE)*,METHOD*)&gt;
	 * &lt;!ATTLIST CLASS
	 * 		%CIMName;
	 * 		%SuperClass;&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String CLASS = "CLASS";

	/**
	 * The INSTANCE element defines a single CIM Instance of a CIM Class. <br>
	 * 
	 * 
	 * &lt;!ELEMENT INSTANCE (QUALIFIER*,(PROPERTY|PROPERTY.ARRAY|PROPERTY.REFERENCE)*)&gt;
	 * &lt;!ATTLIST INSTANCE
	 * 		%ClassName;
	 * 		xml:lang NMTOKEN #IMPLIED&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String INSTANCE = "INSTANCE";

	/**
	 * The QUALIFIER element defines a single CIM Qualifier. <br>
	 * 
	 * 
	 * &lt;!ELEMENT QUALIFIER ((VALUE|VALUE.ARRAY)?)&gt;
	 * &lt;!ATTLIST QUALIFIER
	 * 		%CIMName;
	 * 		%CIMType; #REQUIRED
	 * 		%Propagated;
	 * 		%QualifierFlavor;
	 * 		xml:lang NMTOKEN #IMPLIED&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String QUALIFIER = "QUALIFIER";

	/**
	 * The PROPERTY element defines the value in a CIM Instance or the
	 * definition in a CIM Class of a single (non-array) CIM Property that is
	 * not a reference. <br>
	 * 
	 * 
	 * &lt;!ELEMENT PROPERTY (QUALIFIER*,VALUE?)&gt;
	 * &lt;!ATTLIST PROPERTY
	 * 		%CIMName;
	 * 		%CIMType; #REQUIRED
	 * 		%ClassOrigin;
	 * 		%Propagated;
	 * 		%EmbeddedObject;
	 * 		xml:lang NMTOKEN #IMPLIED&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String PROPERTY = "PROPERTY";

	/**
	 * The PROPERTY.ARRAY element defines the value in a CIM Instance or the
	 * definition in a CIM Class of a single CIM Property with an array type. <br>
	 * 
	 * 
	 * &lt;!ELEMENT PROPERTY.ARRAY (QUALIFIER*,VALUE.ARRAY?)&gt;
	 * &lt;!ATTLIST PROPERTY.ARRAY
	 * 		%CIMName;
	 * 		%CIMType; #REQUIRED
	 * 		%ArraySize;
	 * 		%ClassOrigin;
	 * 		%Propagated;
	 * 		%EmbeddedObject;
	 * 		xml:lang NMTOKEN #IMPLIED&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String PROPERTY_ARRAY = "PROPERTY.ARRAY";

	/**
	 * The PROPERTY.REFERENCE element defines the value in a CIM Instance or the
	 * definition in a CIM Class of a single CIM Property with reference
	 * semantics. <br>
	 * 
	 * 
	 * &lt;!ELEMENT PROPERTY.REFERENCE (QUALIFIER*,VALUE.REFERENCE?)&gt;
	 * &lt;!ATTLIST PROPERTY.REFERENCE
	 * 		%CIMName;
	 * 		%ReferenceClass;
	 * 		%ClassOrigin;
	 * 		%Propagated;&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String PROPERTY_REFERENCE = "PROPERTY.REFERENCE";

	/**
	 * 
	 * The METHOD element defines a single CIM Method. <br>
	 * 
	 * 
	 * &lt;!ELEMENT METHOD (QUALIFIER*,(PARAMETER|PARAMETER.REFERENCE|PARAMETER.ARRAY|PARAMETER.REFARRAY)*)&gt;
	 * &lt;!ATTLIST METHOD
	 * 		%CIMName;
	 * 		%CIMType; #IMPLIED
	 * 		%ClassOrigin;
	 * 		%Propagated;&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String METHOD = "METHOD";

	/**
	 * The PARAMETER element defines a single (non-array, non-reference)
	 * Parameter to a CIM Method. <br>
	 * 
	 * 
	 * &lt;!ELEMENT PARAMETER (QUALIFIER*)&gt;
	 * &lt;!ATTLIST PARAMETER
	 * 		%CIMName;
	 * 		%CIMType; #REQUIRED&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String PARAMETER = "PARAMETER";

	/**
	 * The PARAMETER.REFERENCE element defines a single reference Parameter to a
	 * CIM Method. <br>
	 * 
	 * 
	 * &lt;!ELEMENT PARAMETER.REFERENCE (QUALIFIER*)&gt;
	 * &lt;!ATTLIST PARAMETER.REFERENCE
	 * 		%CIMName;
	 * 		%ReferenceClass;&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String PARAMETER_REFERENCE = "PARAMETER.REFERENCE";

	/**
	 * The PARAMETER.ARRAY element defines a single Parameter to a CIM Method
	 * that has an array type. <br>
	 * 
	 * 
	 * &lt;!ELEMENT PARAMETER.ARRAY (QUALIFIER*)&gt;
	 * &lt;!ATTLIST PARAMETER.ARRAY
	 * 		%CIMName;
	 * 		%CIMType; #REQUIRED
	 * 		%ArraySize;&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String PARAMETER_ARRAY = "PARAMETER.ARRAY";

	/**
	 * The PARAMETER.REFARRAY element defines a single Parameter to a CIM Method
	 * that has an array of references type. <br>
	 * 
	 * 
	 * &lt;!ELEMENT PARAMETER.REFARRAY (QUALIFIER*)&gt;
	 * &lt;!ATTLIST PARAMETER.REFARRAY
	 * 		%CIMName;
	 * 		%ReferenceClass;
	 * 		%ArraySize;&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String PARAMETER_REFARRAY = "PARAMETER.REFARRAY";

	/*
	 * TABLE stuff is missing yet
	 */

	/**
	 * The MESSAGE element models a single CIM message. <br>
	 * 
	 * 
	 * &lt;!ELEMENT MESSAGE (SIMPLEREQ|MULTIREQ|SIMPLERSP|MULTIRSP|SIMPLEEXPREQ|MULTIEXPREQ|SIMPLEEXPRSP|MULTIEXPRSP)&gt;
	 * &lt;!ATTLIST MESSAGE
	 * 		ID              CDATA #REQUIRED
	 * 		PROTOCOLVERSION CDATA #REQUIRED&gt;&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String MESSAGE = "MESSAGE";

	/**
	 * The MULTIREQ element defines a Multiple CIM Operation request. <br>
	 * 
	 * 
	 * &lt;!ELEMENT MULTIREQ (SIMPLEREQ,SIMPLEREQ+)&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String MULTIREQ = "MULTIREQ";

	/**
	 * The MULTIEXPREQ element defines a Multiple CIM Export request. <br>
	 * 
	 * 
	 * &lt;!ELEMENT MULTIEXPREQ (SIMPLEEXPREQ,SIMPLEEXPREQ+)&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String MULTIEXPREQ = "MULTIEXPREQ";

	/**
	 * The SIMPLEREQ element defines a Simple CIM Operation request. <br>
	 * 
	 * 
	 * &lt;!ELEMENT SIMPLEREQ (METHODCALL|IMETHODCALL)&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String SIMPLEREQ = "SIMPLEREQ";

	/**
	 * The SIMPLEEXPREQ element defines a Simple CIM Export request. <br>
	 * 
	 * 
	 * &lt;!ELEMENT SIMPLEEXPREQ (EXPMETHODCALL)&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String SIMPLEEXPREQ = "SIMPLEEXPREQ";

	/**
	 * The IMETHODCALL element defines a single intrinsic method invocation. <br>
	 * 
	 * 
	 * &lt;!ELEMENT IMETHODCALL (LOCALNAMESPACEPATH,IPARAMVALUE*)&gt;
	 * &lt;!ATTLIST IMETHODCALL
	 * 		%CIMName;&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String IMETHODCALL = "IMETHODCALL";

	/**
	 * The METHODCALL element defines a single method invocation on a Class or
	 * Instance. <br>
	 * 
	 * 
	 * &lt;!ELEMENT METHODCALL ((LOCALCLASSPATH|LOCALINSTANCEPATH),PARAMVALUE*)&gt;
	 * &lt;!ATTLIST METHODCALL
	 * 		%CIMName;&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String METHODCALL = "METHODCALL";

	/**
	 * The EXPMETHODCALL element defines a single export method invocation. <br>
	 * 
	 * 
	 * &lt;!ELEMENT EXPMETHODCALL (EXPPARAMVALUE*)&gt;
	 * &lt;!ATTLIST EXPMETHODCALL
	 * 		%CIMName;&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String EXPMETHODCALL = "EXPMETHODCALL";

	/**
	 * The PARAMVALUE element defines a single extrinsic method named parameter
	 * value. <br>
	 * 
	 * 
	 * &lt;!ELEMENT PARAMVALUE (VALUE|VALUE.REFERENCE|VALUE.ARRAY|VALUE.REFARRAY)?&gt;
	 * &lt;!ATTLIST PARAMVALUE
	 * 		%CIMName;
	 * 		%ParamType; #IMPLIED
	 * 		%EmbeddedObject;&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String PARAMVALUE = "PARAMVALUE";

	/**
	 * The IPARAMVALUE element defines a single intrinsic method named parameter
	 * value. <br>
	 * 
	 * 
	 * &lt;!ELEMENT IPARAMVALUE (VALUE|VALUE.ARRAY|VALUE.REFERENCE|CLASSNAME|INSTANCENAME|QUALIFIER.DECLARATION|CLASS|INSTANCE|VALUE.NAMEDINSTANCE)?&gt;
	 * &lt;!ATTLIST IPARAMVALUE
	 * 		%CIMName;&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String IPARAMVALUE = "IPARAMVALUE";

	/**
	 * The EXPPARAMVALUE element defines a single export method named parameter
	 * value. <br>
	 * 
	 * 
	 * &lt;!ELEMENT EXPPARAMVALUE (INSTANCE?)&gt;
	 * &lt;!ATTLIST EXPPARAMVALUE
	 * 		%CIMName;&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String EXPPARAMVALUE = "EXPPARAMVALUE";

	/**
	 * The MULTIRSP element defines a Multiple CIM Operation response. <br>
	 * 
	 * 
	 * &lt;!ELEMENT MULTIRSP (SIMPLERSP,SIMPLERSP+)&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String MULTIRSP = "MULTIRSP";

	/**
	 * The MULTIEXPRSP element defines a Multiple CIM Export response. <br>
	 * 
	 * 
	 * &lt;!ELEMENT MULTIEXPRSP (SIMPLEEXPRSP,SIMPLEEXPRSP+)&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String MULTIEXPRSP = "MULTIEXPRSP";

	/**
	 * The SIMPLERSP element defines a Simple CIM Operation response. <br>
	 * 
	 * 
	 * &lt;!ELEMENT SIMPLERSP (METHODRESPONSE|IMETHODRESPONSE)&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String SIMPLERSP = "SIMPLERSP";

	/**
	 * The SIMPLEEXPRSP element defines a Simple CIM Export response. <br>
	 * 
	 * 
	 * &lt;!ELEMENT SIMPLEEXPRSP (EXPMETHODRESPONSE)&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String SIMPLEEXPRSP = "SIMPLEEXPRSP";

	/**
	 * The METHODRESPONSE defines the response to a single CIM extrinsic method
	 * invocation. <br>
	 * 
	 * 
	 * 
	 * &lt;!ELEMENT METHODRESPONSE (ERROR|(RETURNVALUE?,PARAMVALUE*))&gt;
	 * &lt;!ATTLIST METHODRESPONSE %CIMName;&gt;
	 * 
	 * 
	 * 
	 * @see String
	 */
	public static final String METHODRESPONSE = "METHODRESPONSE";

	/**
	 * The EXPMETHODRESPONSE defines the response to a single export method
	 * invocation. <br>
	 * 
	 * 
	 * &lt;!ELEMENT EXPMETHODRESPONSE (ERROR|IRETURNVALUE?)&gt;
	 * &lt;!ATTLIST EXPMETHODRESPONSE
	 * 		%CIMName;&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String EXPMETHODRESPONSE = "EXPMETHODRESPONSE";

	/**
	 * The IMETHODRESPONSE defines the response to a single intrinsic CIM method
	 * invocation. <br>
	 * 
	 * 
	 * &lt;!ELEMENT IMETHODRESPONSE (ERROR|IRETURNVALUE?)&gt;
	 * &lt;!ATTLIST IMETHODRESPONSE
	 * 		%CIMName;&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String IMETHODRESPONSE = "IMETHODRESPONSE";

	/**
	 * The ERROR element is used to define a fundamental error which prevented a
	 * method from executing normally. <br>
	 * 
	 * 
	 * &lt;!ELEMENT ERROR (INSTANCE*)
	 * &lt;!ATTLIST ERROR
	 * 		CODE        CDATA #REQUIRED
	 * 		DESCRIPTION CDATA #IMPLIED&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String ERROR = "ERROR";

	/**
	 * The RETURNVALUE element specifies the value returned from an extrinsic
	 * method call. <br>
	 * 
	 * 
	 * &lt;!ELEMENT RETURNVALUE (VALUE|VALUE.REFERENCE)?&gt;
	 * &lt;!ATTLIST RETURNVALUE
	 * 		%EmbeddedObject;
	 * 		%ParamType; #IMPLIED&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String RETURNVALUE = "RETURNVALUE";

	/**
	 * The IRETURNVALUE element specifies the value returned from an intrinsic
	 * method call. <br>
	 * 
	 * 
	 * &lt;!ELEMENT IRETURNVALUE (CLASSNAME*|INSTANCENAME*|VALUE*|VALUE.OBJECTWITHPATH*|VALUE.OBJECTWITHLOCALPATH*VALUE.OBJECT*|OBJECTPATH*|QUALIFIER.DECLARATION*|VALUE.ARRAY?|VALUE.REFERENCE?|CLASS*|INSTANCE*|VALUE.NAMEDINSTANCE*)&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String IRETURNVALUE = "IRETURNVALUE";

	/**
	 * The CORRELATOR element represents a server-defined operation correlator. <br>
	 * 
	 * 
	 * &lt;!ELEMENT CORRELATOR (VALUE)&gt;
	 * &lt;!ATTLIST CORRELATOR
	 * 		%CIMName;
	 * 		%CIMType; #REQUIRED&gt;
	 * 
	 * 
	 * @see String
	 */
	public static final String CORRELATOR = "CORRELATOR";
}
