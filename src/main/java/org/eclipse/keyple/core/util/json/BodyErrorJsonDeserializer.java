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

import com.google.gson.*;
import java.lang.reflect.Type;

/**
 * Serializer of a {@link BodyError} that contains a {@link RuntimeException}.
 *
 * @since 2.0.0
 */
public class BodyErrorJsonDeserializer implements JsonDeserializer<BodyError> {

  /**
   * {@inheritDoc}
   *
   * @since 2.0.0
   */
  @Override
  public BodyError deserialize(JsonElement json, Type type, JsonDeserializationContext context)
      throws JsonParseException {

    String exceptionName = json.getAsJsonObject().get("code").getAsString();
    JsonObject bodyException = json.getAsJsonObject().get("exception").getAsJsonObject();

    try {
      Class<Exception> exceptionClass = (Class<Exception>) Class.forName(exceptionName);
      return new BodyError((Exception) context.deserialize(bodyException, exceptionClass));
    } catch (Exception e) {
      throw new JsonParseException(e);
    }
  }
}
