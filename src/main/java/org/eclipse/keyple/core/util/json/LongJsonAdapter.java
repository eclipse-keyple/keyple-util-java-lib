/* **************************************************************************************
 * Copyright (c) 2021 Calypso Networks Association https://calypsonet.org/
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

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import org.eclipse.keyple.core.util.ByteArrayUtil;

/**
 * JSON serializer/deserializer of a long to a hex string.
 *
 * @since 2.1.0
 */
public class LongJsonAdapter implements JsonSerializer<Long>, JsonDeserializer<Long> {

  /**
   * {@inheritDoc}
   *
   * @since 2.1.0
   */
  @Override
  public JsonElement serialize(Long data, Type typeOfSrc, JsonSerializationContext context) {
    return new JsonPrimitive(ByteArrayUtil.toHex(data));
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.1.0
   */
  @Override
  public Long deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    return ByteArrayUtil.hexToLong(json.getAsString());
  }
}
