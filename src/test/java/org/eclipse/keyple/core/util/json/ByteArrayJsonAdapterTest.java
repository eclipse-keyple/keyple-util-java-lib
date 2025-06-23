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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class ByteArrayJsonAdapterTest {

  private static final String JSON_DATA = "{\"primitiveValue\":\"F1F2\"}";

  private static class Data {
    byte[] primitiveValue = {(byte) 0xF1, (byte) 0xF2};
  }

  @Test
  public void serialize() {
    assertThat(JsonUtil.getParser().toJson(new Data())).isEqualTo(JSON_DATA);
  }

  @Test
  public void deserialize() {
    assertThat(JsonUtil.getParser().fromJson(JSON_DATA, Data.class))
        .isEqualToComparingFieldByField(new Data());
  }
}
