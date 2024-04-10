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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.util.Map;
import java.util.Properties;
import org.junit.Test;

public class BodyErrorJsonDeserializerTest {

  private static final String DETAIL_MESSAGE = "DETAIL_MESSAGE";

  @Test
  public void deserialize() {
    String context = getContextInfoAsJson();
    BodyError bodyError = new BodyError(new IllegalArgumentException(DETAIL_MESSAGE));
    BodyError result = JsonUtil.getParser().fromJson(JsonUtil.toJson(bodyError), BodyError.class);
    assertThat(result.getCode()).isEqualTo(bodyError.getCode());
    assertThat(result.getException().getMessage()).isEqualTo(DETAIL_MESSAGE);
    assertThat(result.getException()).isInstanceOf(IllegalArgumentException.class);
  }

  private String getContextInfoAsJson() {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    JsonObject json = new JsonObject();

    // Version de la JVM
    json.addProperty("java.version", System.getProperty("java.version"));

    // Liste des dépendances chargées
    ClassLoader loader = ClassLoader.getSystemClassLoader();
    json.addProperty("dependencies", loader.toString());

    // Propriétés système
    Properties systemProperties = System.getProperties();
    for (Map.Entry<Object, Object> entry : systemProperties.entrySet()) {
      json.addProperty((String) entry.getKey(), entry.getValue().toString());
    }

    // Chemin de classe
    json.addProperty("java.class.path", System.getProperty("java.class.path"));

    // Nom de l'utilisateur
    json.addProperty("user.name", System.getProperty("user.name"));

    return gson.toJson(json);
  }
}
