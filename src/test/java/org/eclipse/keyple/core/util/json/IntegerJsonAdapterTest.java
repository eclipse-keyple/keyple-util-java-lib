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

public class IntegerJsonAdapterTest {

  private static final String JSON_DATA =
      "{\"f1\":\"FA\",\"f2\":\"FB11\",\"f3\":\"FC1122\",\"f4\":\"FD112233\",\"objectValue\":\"FE\",\"oddDigitNumberValue\":\"0F\"}";

  private static class Data {
    int f1 = 0xFA;
    int f2 = 0xFB11;
    int f3 = 0xFC1122;
    int f4 = 0xFD112233;
    Integer objectValue = 0xFE;
    int oddDigitNumberValue = 0xF;
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
