/* **************************************************************************************
 * Copyright (c) 2021 Calypso Networks Association https://www.calypsonet-asso.org/
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

import com.google.gson.*;
import java.lang.reflect.Type;

/**
 * Serializer/Deserializer of an integer to an hex string.
 *
 * @since 2.0
 */
public class IntegerJsonAdapter implements JsonSerializer<Integer>, JsonDeserializer<Integer> {

  /**
   * {@inheritDoc}
   *
   * @since 2.0
   */
  @Override
  public JsonElement serialize(Integer data, Type typeOfSrc, JsonSerializationContext context) {
    return new JsonPrimitive(Integer.toHexString(data));
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.0
   */
  @Override
  public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    return Integer.parseInt(json.getAsString(), 16);
  }
}
