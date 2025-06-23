/* **************************************************************************************
 * Copyright (c) 2022 Calypso Networks Association https://calypsonet.org/
 *
 * See the NOTICE file(s) distributed with this work for additional information
 * regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License 2.0 which is available at http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 ************************************************************************************** */
package org.eclipse.keyple.core.util.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * JSON serializer of a {@link Date} to a string in ISO 8601 format.
 *
 * @since 2.2.0
 */
public class DateJsonSerializer implements JsonSerializer<Date> {

  private final SimpleDateFormat sdf;

  /**
   * Builds a new instance.
   *
   * @since 2.2.0
   */
  public DateJsonSerializer() {
    // We're using "Z" instead of "XXX" to be compliant with JRE 1.6
    sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    sdf.setTimeZone(TimeZone.getTimeZone("CET"));
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.2.0
   */
  @Override
  public JsonElement serialize(Date data, Type typeOfSrc, JsonSerializationContext context) {
    return new JsonPrimitive(sdf.format(data));
  }
}
