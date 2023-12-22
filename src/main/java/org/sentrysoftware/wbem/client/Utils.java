package org.sentrysoftware.wbem.client;

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

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

	/**
	 * Check if the required argument is not null.
	 *
	 * @param argument
	 * @param name
	 * @throws IllegalArgumentException if the argument is null
	 */
	public static <T> void checkNonNull(final T argument, final String name) {
		if (argument == null) {
			throw new IllegalArgumentException(name + " must not be null.");
		}
	}

	/**
	 * Default separator for array values
	 */
	public static final String DEFAULT_ARRAY_SEPARATOR = "|";

	public static final String EMPTY = "";

	/**
	 * Formatter/Parser of the first part of CIM_DATETIME
	 */
	public static final DateTimeFormatter WBEM_CIM_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

	/**
	 * Regex that matches with a CIM_DATETIME string format See
	 * https://docs.microsoft.com/en-us/windows/win32/wmisdk/cim-datetime
	 * <ul>
	 * <li>group(1): <code>yyyymmddHHMMSS</code>
	 * <li>group(2): optional fraction of seconds
	 * <li>group(3): timezone offset... in minutes (sigh)
	 */
	private static final Pattern CIM_DATETIME_PATTERN = Pattern
			.compile("^([0-9]{14})(?:\\.([0-9]{3,6}))?([+-][0-9]{3})$");

	/**
	 * Convert a String holding a CIM_DATETIME (i.e. a string in the form of
	 * <code>yyyymmddHHMMSS.mmmmmmsUUU</code>) to an OffsetDateTime object
	 * 
	 * @param stringValue String value with a CIM_DATETIME
	 * @return OffsetDateTime instance
	 */
	public static OffsetDateTime convertCimDateTime(final String stringValue) {

		if (stringValue == null) {
			return null;
		}

		final Matcher dateTimeMatcher = CIM_DATETIME_PATTERN.matcher(stringValue);
		if (!dateTimeMatcher.find()) {
			throw new IllegalArgumentException("Not a valid CIM_DATETIME value: " + stringValue);
		}

		// LocalDateTime
		final LocalDateTime localDateTime = LocalDateTime.parse(dateTimeMatcher.group(1), WBEM_CIM_DATETIME_FORMATTER);

		// Note: we're not taking the milliseconds and microseconds into account from
		// group(2)

		// Zone Offset
		final String zoneOffset = dateTimeMatcher.group(3);
		if (zoneOffset == null) {
			throw new IllegalStateException(
					"Unable to get the timezone offset from CIM_DATETIME value: " + stringValue);
		}
		final int secondsOffset = Integer.parseInt(zoneOffset) * 60;
		final ZoneOffset offset = ZoneOffset.ofTotalSeconds(secondsOffset);

		return OffsetDateTime.of(localDateTime, offset);
	}

}
