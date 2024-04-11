/* **************************************************************************************
 * Copyright (c) 2024 Calypso Networks Association https://calypsonet.org/
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
import java.nio.ByteBuffer;
import org.eclipse.keyple.core.util.HexUtil;

/**
 * JSON serializer/deserializer of a ByteBuffer to a hex string.
 *
 * <p>Note: This implementation assumes that the ByteBuffer is backed by an array and is not a
 * direct array.
 *
 * @since 2.3.2
 */
public class ByteBufferJsonAdapter
    implements JsonSerializer<ByteBuffer>, JsonDeserializer<ByteBuffer> {

  /**
   * {@inheritDoc}
   *
   * @since 2.3.2
   */
  @Override
  public JsonElement serialize(
      ByteBuffer buffer, Type typeOfSrc, JsonSerializationContext context) {
    // Caution: if the ByteBuffer is a direct buffer, calling buffer.array() will throw a
    // ReadOnlyBufferException.
    return new JsonPrimitive(HexUtil.toHex(buffer.array()));
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.3.2
   */
  @Override
  public ByteBuffer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    byte[] array = HexUtil.toByteArray(json.getAsString());
    return ByteBuffer.wrap(array);
  }
}
