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
import org.eclipse.keyple.core.util.HexUtil;

/**
 * JSON serializer/deserializer of a short to a hex string.
 *
 * @since 2.1.0
 */
public class ShortJsonAdapter implements JsonSerializer<Short>, JsonDeserializer<Short> {

  /**
   * {@inheritDoc}
   *
   * @since 2.1.0
   */
  @Override
  public JsonElement serialize(Short data, Type typeOfSrc, JsonSerializationContext context) {
    return new JsonPrimitive(HexUtil.toHex(data));
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.1.0
   */
  @Override
  public Short deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    return HexUtil.toShort(json.getAsString());
  }
}
