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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.Date;

/**
 * Json utilities based on Gson (com.google.gson).
 *
 * @since 2.0.0
 */
public final class JsonUtil {

  private static Gson parser;
  private static final GsonBuilder gsonBuilder = initGsonBuilder();

  /**
   * Gets the singleton instance of the parser.
   *
   * <p>If not created yet, a default instance is created.
   *
   * <p><b>Caution:</b> to use the {@link Gson} object in return, the invoker must also import the
   * Gson library from Google (see the manifest of the NOTICE document for the version to be used).
   *
   * @return a not null instance.
   * @since 2.0.0
   */
  public static Gson getParser() {
    if (parser == null) {
      // init parser with keyple default value
      parser = gsonBuilder.create();
    }
    return parser;
  }

  /**
   * (private)<br>
   * Constructor.
   */
  private JsonUtil() {}

  /**
   * (private)<br>
   * Initialize and personalize the gson parser used in Keyple.
   *
   * @return A not null builder instance.
   */
  private static GsonBuilder initGsonBuilder() {
    return new GsonBuilder()
        .enableComplexMapKeySerialization()
        .registerTypeAdapter(byte.class, new ByteJsonAdapter())
        .registerTypeAdapter(Byte.class, new ByteJsonAdapter())
        .registerTypeAdapter(short.class, new ShortJsonAdapter())
        .registerTypeAdapter(Short.class, new ShortJsonAdapter())
        .registerTypeAdapter(int.class, new IntegerJsonAdapter())
        .registerTypeAdapter(Integer.class, new IntegerJsonAdapter())
        .registerTypeAdapter(long.class, new LongJsonAdapter())
        .registerTypeAdapter(Long.class, new LongJsonAdapter())
        .registerTypeAdapter(byte[].class, new ByteArrayJsonAdapter())
        .registerTypeAdapter(BodyError.class, new BodyErrorJsonDeserializer())
        .registerTypeAdapter(Date.class, new DateJsonSerializer())
        .registerTypeHierarchyAdapter(ByteBuffer.class, new ByteBufferJsonAdapter())
        .registerTypeHierarchyAdapter(Calendar.class, new CalendarJsonAdapter())
        .registerTypeHierarchyAdapter(Throwable.class, new ThrowableJsonAdapter());
  }

  /**
   * Registers a new type adapter.
   *
   * @param matchingClass The type to be registered.
   * @param adapter The type adapter to be registered (should implement {@link
   *     com.google.gson.JsonSerializer} and/or {@link com.google.gson.JsonDeserializer}).
   * @param withSubclasses Apply this adapter to subclasses of matchingClass also.
   * @since 2.0.0
   */
  public static void registerTypeAdapter(
      Class<?> matchingClass, Object adapter, boolean withSubclasses) {

    // init custom types after allowing the user to overwrite keyple default adapter
    if (withSubclasses) {
      gsonBuilder.registerTypeHierarchyAdapter(matchingClass, adapter);
    } else {
      gsonBuilder.registerTypeAdapter(matchingClass, adapter);
    }
    parser = gsonBuilder.create();
  }

  /**
   * Formats the provided object as a json string.
   *
   * @param obj The object to format.
   * @return A not empty String.
   * @since 2.0.0
   */
  public static String toJson(Object obj) {
    return getParser().toJson(obj);
  }
}
