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

public class LongJsonAdapterTest {

  private static final String JSON_DATA =
      "{\"f1\":\"FA\",\"f2\":\"FB11\",\"f3\":\"FC1122\",\"f4\":\"FD112233\",\"f5\":\"F511223344\",\"f6\":\"F61122334455\",\"f7\":\"F7112233445566\",\"f8\":\"F811223344556677\",\"objectValue\":\"FE\",\"oddDigitNumberValue\":\"0F\"}";

  private static class Data {
    long f1 = 0xFAL;
    long f2 = 0xFB11L;
    long f3 = 0xFC1122L;
    long f4 = 0xFD112233L;
    long f5 = 0xF511223344L;
    long f6 = 0xF61122334455L;
    long f7 = 0xF7112233445566L;
    long f8 = 0xF811223344556677L;
    Long objectValue = 0xFEL;
    long oddDigitNumberValue = 0xF;
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
