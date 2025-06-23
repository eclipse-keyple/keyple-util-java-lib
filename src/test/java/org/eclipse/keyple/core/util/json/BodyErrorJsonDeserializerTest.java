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

  @Test
  public void deserialize() {
    BodyError bodyError = new BodyError(new IllegalArgumentException(DETAIL_MESSAGE));
    BodyError result = JsonUtil.getParser().fromJson(JsonUtil.toJson(bodyError), BodyError.class);
    assertThat(result.getCode()).isEqualTo(bodyError.getCode());
    assertThat(result.getException().getMessage()).isEqualTo(DETAIL_MESSAGE);
    assertThat(result.getException()).isInstanceOf(IllegalArgumentException.class);
  }
}
