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
import java.util.Arrays;
import java.util.List;

/**
 * JSON deserializer of a {@link BodyError}.
 *
 * <p>It is not necessary to define a serializer because the type of the associated object
 * registered is a class and not an interface, and therefore has fields. Gson will then use its
 * default reflexivity mechanism to serialize the object.
 *
 * <p>The original exception is only rebuilt if its class belongs to the {@code java}, {@code
 * org.eclipse.keyple} or {@code org.eclipse.keypop} packages (or their sub-packages) and is a
 * subclass of {@link Exception}. Otherwise, or if the original exception cannot be rebuilt, it is
 * replaced by a {@link RuntimeException} whose message contains the name of the original class and
 * its original message.
 *
 * @since 2.0.0
 */
public class BodyErrorJsonDeserializer implements JsonDeserializer<BodyError> {

  /** Prefixes of the class names of the exceptions which can be rebuilt. */
  private static final List<String> ALLOWED_CLASS_NAME_PREFIXES =
      Arrays.asList("java.", "org.eclipse.keyple.", "org.eclipse.keypop.");

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

    Exception exception = null;
    if (isAllowed(exceptionName)) {
      exception = rebuildException(exceptionName, bodyException, context);
    }
    if (exception == null) {
      exception =
          new RuntimeException(
              String.format(
                  "Remote exception [%s]: %s", exceptionName, getDetailMessage(bodyException)));
    }
    return new BodyError(exception);
  }

  /**
   * Checks if the provided class name belongs to an allowed package.
   *
   * @param className The class name.
   * @return True if the class name is allowed.
   */
  private static boolean isAllowed(String className) {
    for (String prefix : ALLOWED_CLASS_NAME_PREFIXES) {
      if (className.startsWith(prefix)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Rebuilds the original exception.
   *
   * @param exceptionName The name of the exception class.
   * @param bodyException The JSON content of the exception.
   * @param context The deserialization context.
   * @return Null if the class is not found, is not a subclass of {@link Exception}, or cannot be
   *     instantiated.
   */
  private static Exception rebuildException(
      String exceptionName, JsonObject bodyException, JsonDeserializationContext context) {
    try {
      Class<?> exceptionClass =
          Class.forName(exceptionName, false, BodyErrorJsonDeserializer.class.getClassLoader());
      if (!Exception.class.isAssignableFrom(exceptionClass)) {
        return null;
      }
      Object exception = context.deserialize(bodyException, exceptionClass);
      // The Throwable adapter returns another exception type if the original one cannot be built.
      return exceptionClass.isInstance(exception) ? (Exception) exception : null;
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * Gets the original message of the exception.
   *
   * @param bodyException The JSON content of the exception.
   * @return A nullable string.
   */
  private static String getDetailMessage(JsonObject bodyException) {
    JsonElement detailMessage = bodyException.get("detailMessage");
    return detailMessage != null && detailMessage.isJsonPrimitive()
        ? detailMessage.getAsString()
        : null;
  }
}
