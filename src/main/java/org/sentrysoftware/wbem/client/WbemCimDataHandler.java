package org.sentrysoftware.wbem.client;

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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.sentrysoftware.wbem.javax.cim.CIMDataType;
import org.sentrysoftware.wbem.javax.cim.CIMDateTimeAbsolute;
import org.sentrysoftware.wbem.javax.cim.CIMDateTimeInterval;
import org.sentrysoftware.wbem.javax.cim.CIMInstance;
import org.sentrysoftware.wbem.javax.cim.CIMObjectPath;
import org.sentrysoftware.wbem.javax.cim.CIMProperty;

/**
 * Handler for CIMProperty conversion to string.
 *
 */
public class WbemCimDataHandler {

	private WbemCimDataHandler() { }

	public static final String PATH_PROPERTY = "__Path";

	private static final Map<Integer, Function<Object, String>> CONVERT_MAP;
	static {
		final Map<Integer, Function<Object, String>> map = new HashMap<>();
		map.put(CIMDataType.STRING, String::valueOf);
		map.put(CIMDataType.BOOLEAN, String::valueOf);
		map.put(CIMDataType.SINT8, String::valueOf);
		map.put(CIMDataType.SINT16, String::valueOf);
		map.put(CIMDataType.SINT32, String::valueOf);
		map.put(CIMDataType.SINT64, String::valueOf);
		map.put(CIMDataType.CHAR16, String::valueOf);
		map.put(CIMDataType.REAL32, String::valueOf);
		map.put(CIMDataType.REAL64, String::valueOf);
		map.put(CIMDataType.OBJECT, String::valueOf);
		map.put(CIMDataType.DATETIME, WbemCimDataHandler::convertDateTime);
		map.put(CIMDataType.UINT8, String::valueOf);
		map.put(CIMDataType.UINT16, String::valueOf);
		map.put(CIMDataType.UINT32, String::valueOf);
		map.put(CIMDataType.UINT64, String::valueOf);
		map.put(CIMDataType.REFERENCE, WbemCimDataHandler::convertReference);
		CONVERT_MAP = Collections.unmodifiableMap(map);
	}

	/**
	 * Get the value of the CIM property.
	 *
	 * @param property The property name.
	 * @param cimInstance The CIM instance.
	 * @param arraySeparator The array separator value. default value '|'
	 * @return
	 */
	public static String getCimPropertyAsString(
			final String property,
			final CIMInstance cimInstance,
			final String arraySeparator) {
		Utils.checkNonNull(property, "property");
		Utils.checkNonNull(cimInstance, "cimInstance");

		if (PATH_PROPERTY.equalsIgnoreCase(property)) {
			final CIMObjectPath objectPath = cimInstance.getObjectPath();
			Utils.checkNonNull(objectPath, "objectPath");
			return convertReference(objectPath);
		}

		// first check getProperty
		CIMProperty<?> cimProperty = cimInstance.getProperty(property);

		// then, if null, check getKeys
		final String separator = arraySeparator == null? Utils.DEFAULT_ARRAY_SEPARATOR : arraySeparator;
		if (cimProperty == null && cimInstance.getKeys() != null) {
			return Stream.of(cimInstance.getKeys())
					.filter(key -> key.getName() != null && key.getName().equalsIgnoreCase(property))
					.findFirst()
					.map(cp -> convertCimPropertyValue(cp, separator))
					.orElse(Utils.EMPTY);
		}

		return cimProperty == null ?
				Utils.EMPTY : convertCimPropertyValue(cimProperty, separator);
	}

	/**
	 * Convert the CIM property value into a String.
	 *
	 * @param cimProperty The CIM property.
	 * @param arraySeparator The array separator value.
	 * @return
	 */
	private static String convertCimPropertyValue(
			final CIMProperty<?> cimProperty,
			final String arraySeparator) {
		final Object cimPropertyValue = cimProperty.getValue();
		if (cimPropertyValue == null) {
			return Utils.EMPTY;
		}

		final CIMDataType dataType = cimProperty.getDataType();
		if (dataType == null) {
			return cimPropertyValue.toString();
		}

		return dataType.isArray() ?
				convertArray(cimProperty, arraySeparator) :
				CONVERT_MAP.getOrDefault(dataType.getType(), Object::toString).apply(cimPropertyValue);
	}

	/**
	 * Convert a CIM property value array.
	 *
	 * @param cimProperty The CIM property.
	 * @param arraySeparator The array separator value.
	 * @return
	 */
	private static String convertArray(
			final CIMProperty<?> cimProperty,
			final String arraySeparator) {
		return Arrays.stream((Object[]) cimProperty.getValue())
				.map(obj -> (obj == null) ? Utils.EMPTY : CONVERT_MAP.getOrDefault(cimProperty.getDataType().getType(), Object::toString).apply(obj))
				.collect(Collectors.joining(arraySeparator, Utils.EMPTY, arraySeparator));
	}

	/**
	 * Convert a CIMObjectPath reference to string:
	 * <li>take the part after ':'</li>
	 * <li>escape back slash</li>
	 * <li>escape double quotes</li>
	 *
	 * @param cimPropertyValue
	 * @return
	 */
	private static String convertReference(final Object cimPropertyValue) {
		final CIMObjectPath objectPath = (CIMObjectPath) cimPropertyValue;
		final String str = objectPath.toString();
		return str
				.substring(str.indexOf(':') + 1) // take the part after : (if exists)
				.replace("\\\\", "\\") // Replace \\ by \
				.replace("\\\"", "\""); // Replace \" by "
	}

	/**
	 * Convert a CIMDateTime into a string containing to Epoch time milliseconds.
	 *
	 * @param cimPropertyValue
	 * @return
	 */
	private static String convertDateTime(final Object cimPropertyValue) {
		if (cimPropertyValue == null) {
			return Utils.EMPTY;
		}

		if (cimPropertyValue instanceof CIMDateTimeAbsolute) {
			final String dateTimeString = ((CIMDateTimeAbsolute) cimPropertyValue).getDateTimeString();
			return String.valueOf(Utils.convertCimDateTime(dateTimeString).toInstant().toEpochMilli());
		}
		return String.valueOf(((CIMDateTimeInterval) cimPropertyValue).getTotalMilliseconds());
	}
}
