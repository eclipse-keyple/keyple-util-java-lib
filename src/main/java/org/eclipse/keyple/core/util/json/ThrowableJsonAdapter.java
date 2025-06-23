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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

/**
 * JSON serializer of a {@link Throwable}.
 *
 * <p>Only the field "message" is serialized during the process.
 *
 * @since 2.0.0
 */
public class ThrowableJsonAdapter
    implements JsonSerializer<Throwable>, JsonDeserializer<Throwable> {

  /**
   * {@inheritDoc}
   *
   * @since 2.0.0
   */
  @Override
  public JsonElement serialize(
      Throwable exception, Type type, JsonSerializationContext jsonSerializationContext) {

    JsonObject json = new JsonObject();
    json.addProperty("detailMessage", exception.getMessage());
    return json;
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.3.2
   */
  @Override
  public Throwable deserialize(
      JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
      throws JsonParseException {

    String message = ((JsonObject) jsonElement).get("detailMessage").getAsString();

    Class<? extends Exception> exceptionClass;
    try {
      exceptionClass = (Class<? extends Exception>) Class.forName(type.getTypeName());
    } catch (ClassNotFoundException e) {
      return new JsonParseException(
          String.format(
              "Exception [%s] not founded in runtime environment. Original message: %s",
              type, message));
    }

    try {
      Constructor<? extends Exception> constructor;
      try {
        constructor = exceptionClass.getConstructor(String.class);
        return constructor.newInstance(message);
      } catch (NoSuchMethodException ignored) {
      }
      try {
        constructor = exceptionClass.getConstructor(String.class, Throwable.class);
        return constructor.newInstance(message, null);
      } catch (NoSuchMethodException ignored) {
      }
      try {
        constructor = exceptionClass.getConstructor(String.class, Exception.class);
        return constructor.newInstance(message, null);
      } catch (NoSuchMethodException ignored) {
      }
      try {
        constructor = exceptionClass.getConstructor(String.class, RuntimeException.class);
        return constructor.newInstance(message, null);
      } catch (NoSuchMethodException e) {
        return new JsonParseException(
            String.format(
                "No valid constructor found for exception [%s] with message [%s]", type, message));
      }
    } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
      return new JsonParseException(
          String.format(
              "Error while trying to build exception [%s] with message [%s]", type, message));
    }
  }
}
