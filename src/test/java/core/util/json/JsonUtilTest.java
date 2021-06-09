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
package core.util.json;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.eclipse.keyple.core.util.json.BodyError;
import org.eclipse.keyple.core.util.json.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(MockitoJUnitRunner.class)
public class JsonUtilTest {

  private static final Logger logger = LoggerFactory.getLogger(JsonUtilTest.class);

  @Test
  public void serialize_IllegalArgumentException() {
    RuntimeException source = new IllegalArgumentException("IAE message");
    assertSerialization_forException(new BodyError(source), BodyError.class);
  }

  @Test
  public void toJson_whenObjectIsNotEmpty_shouldReturnANotEmptyString() {
    List<String> obj = Arrays.asList("AB", "CD");
    String result = JsonUtil.toJson(obj);
    assertThat(result).isEqualTo("[\"AB\",\"CD\"]");
  }

  @Test
  public void toJson_whenObjectIsEmpty_shouldReturnAnEmptyString() {
    List<String> obj = new ArrayList<String>();
    String result = JsonUtil.toJson(obj);
    assertThat(result).isEqualTo("[]");
  }

  @Test
  public void toJson_whenObjectIsNull_shouldReturnAStringContainingNull() {
    String result = JsonUtil.toJson(null);
    assertThat(result).isEqualTo("null");
  }

  /*
   * Utility Methods
   */

  public static void assertSerialization(Object source, Class objectClass) {
    Gson gson = JsonUtil.getParser();
    String json = gson.toJson(source);
    logger.debug("json : {}", json);
    Object target = gson.fromJson(json, objectClass);
    assertThat(target).usingRecursiveComparison().isEqualTo(source);
  }

  public static void assertSerialization_forList(List<?> source, Type objectType) {
    Gson gson = JsonUtil.getParser();
    String json = gson.toJson(source);
    logger.debug("json : {}", json);
    List<?> target = gson.fromJson(json, objectType);
    assertThat(target).hasSameSizeAs(source);
    assertThat(target.get(0)).usingRecursiveComparison().isEqualTo(source.get(0));
  }

  public static void assertSerialization_forException(
      Object source, Class<? extends BodyError> objectClass) {
    Gson gson = JsonUtil.getParser();
    String json = gson.toJson(source);
    assertThat(json).doesNotContain("suppressedExceptions");
    assertThat(json).doesNotContain("stackTrace");
    logger.debug("json : {}", json);
    BodyError target = gson.fromJson(json, objectClass);
    logger.debug(
        "deserialize exception className : {}", target.getException().getClass().getName());
    assertThat(target).usingRecursiveComparison().isEqualTo(source);
  }
}
