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

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

public class BodyErrorJsonDeserializerTest {

  private static final String DETAIL_MESSAGE = "DETAIL_MESSAGE";

  // Flags are held outside the test types so that reading them does not load these types
  public static boolean otherExceptionLoaded;
  public static boolean otherExceptionCreated;
  private static boolean unexpectedTypeLoaded;
  private static boolean unexpectedTypeCreated;
  private static boolean errorTypeCreated;

  @Test
  public void deserialize() {
    BodyError bodyError = new BodyError(new IllegalArgumentException(DETAIL_MESSAGE));
    BodyError result = JsonUtil.getParser().fromJson(JsonUtil.toJson(bodyError), BodyError.class);
    assertThat(result.getCode()).isEqualTo(bodyError.getCode());
    assertThat(result.getException().getMessage()).isEqualTo(DETAIL_MESSAGE);
    assertThat(result.getException()).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void deserialize_whenExceptionBelongsToKeyplePackage_shouldRebuildOriginalException() {
    BodyError bodyError = new BodyError(new KeypleTestException(DETAIL_MESSAGE));
    BodyError result = JsonUtil.getParser().fromJson(JsonUtil.toJson(bodyError), BodyError.class);
    assertThat(result.getException())
        .isInstanceOf(KeypleTestException.class)
        .hasMessage(DETAIL_MESSAGE);
  }

  @Test
  public void deserialize_whenExceptionIsOutsideAllowedPackages_shouldReturnRuntimeException() {
    // The package "org.eclipse.keypleother" starts like "org.eclipse.keyple" but is not allowed
    String json = buildJson("org.eclipse.keypleother.OtherException", DETAIL_MESSAGE);
    BodyError result = JsonUtil.getParser().fromJson(json, BodyError.class);
    assertFallback(result, "org.eclipse.keypleother.OtherException");
    assertThat(otherExceptionLoaded).isFalse();
    assertThat(otherExceptionCreated).isFalse();
  }

  @Test
  public void deserialize_whenExceptionIsAThirdPartyException_shouldReturnRuntimeException() {
    String json = buildJson("com.google.gson.JsonParseException", DETAIL_MESSAGE);
    assertFallback(
        JsonUtil.getParser().fromJson(json, BodyError.class), "com.google.gson.JsonParseException");
  }

  @Test
  public void deserialize_whenExceptionBelongsToJavaxPackage_shouldReturnRuntimeException() {
    String json = buildJson("javax.management.JMException", DETAIL_MESSAGE);
    assertFallback(
        JsonUtil.getParser().fromJson(json, BodyError.class), "javax.management.JMException");
  }

  @Test
  public void deserialize_whenCodeIsAnArrayClassName_shouldReturnRuntimeException() {
    String json = buildJson("[Ljava.lang.Exception;", DETAIL_MESSAGE);
    assertFallback(JsonUtil.getParser().fromJson(json, BodyError.class), "[Ljava.lang.Exception;");
  }

  @Test
  public void deserialize_whenAllowedClassIsUnknown_shouldReturnRuntimeException() {
    String json = buildJson("org.eclipse.keyple.DoesNotExist", DETAIL_MESSAGE);
    assertFallback(
        JsonUtil.getParser().fromJson(json, BodyError.class), "org.eclipse.keyple.DoesNotExist");
  }

  @Test
  public void deserialize_whenAllowedClassIsNotAnException_shouldReturnRuntimeException() {
    String json = buildJson(NotAnException.class.getName(), DETAIL_MESSAGE);
    assertFallback(
        JsonUtil.getParser().fromJson(json, BodyError.class), NotAnException.class.getName());
    assertThat(unexpectedTypeLoaded).isFalse();
    assertThat(unexpectedTypeCreated).isFalse();
  }

  @Test
  public void
      deserialize_whenAllowedClassIsAThrowableButNotAnException_shouldReturnRuntimeException() {
    String json = buildJson(ErrorType.class.getName(), DETAIL_MESSAGE);
    assertFallback(JsonUtil.getParser().fromJson(json, BodyError.class), ErrorType.class.getName());
    assertThat(errorTypeCreated).isFalse();
  }

  @Test
  public void
      deserialize_whenAllowedExceptionHasNoUsableConstructor_shouldReturnRuntimeException() {
    String json = buildJson(NoMessageConstructorException.class.getName(), DETAIL_MESSAGE);
    assertFallback(
        JsonUtil.getParser().fromJson(json, BodyError.class),
        NoMessageConstructorException.class.getName());
  }

  @Test
  public void deserialize_whenFallbackAndMessageIsMissing_shouldReturnRuntimeException() {
    String json = "{\"code\":\"com.unknown.DoesNotExist\",\"exception\":{}}";
    BodyError result = JsonUtil.getParser().fromJson(json, BodyError.class);
    assertThat(result.getException())
        .isExactlyInstanceOf(RuntimeException.class)
        .hasMessage("Remote exception [com.unknown.DoesNotExist]: null");
  }

  private static String buildJson(String code, String detailMessage) {
    return "{\"code\":\""
        + code
        + "\",\"exception\":{\"detailMessage\":\""
        + detailMessage
        + "\"}}";
  }

  private static void assertFallback(BodyError result, String originalClassName) {
    assertThat(result.getException())
        .isExactlyInstanceOf(RuntimeException.class)
        .hasMessage("Remote exception [" + originalClassName + "]: " + DETAIL_MESSAGE);
    assertThat(result.getCode()).isEqualTo(RuntimeException.class.getName());
  }

  public static class KeypleTestException extends RuntimeException {
    public KeypleTestException(String message) {
      super(message);
    }
  }

  public static class NoMessageConstructorException extends RuntimeException {
    public NoMessageConstructorException(int code) {
      super(String.valueOf(code));
    }
  }

  public static class NotAnException {
    static {
      unexpectedTypeLoaded = true;
    }

    public String payload;

    public NotAnException() {
      unexpectedTypeCreated = true;
    }
  }

  public static class ErrorType extends Error {
    public ErrorType(String message) {
      super(message);
      errorTypeCreated = true;
    }
  }
}
