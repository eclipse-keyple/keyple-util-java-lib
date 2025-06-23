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

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class ByteJsonAdapterTest {

  private static final String JSON_DATA =
      "{\"primitiveValue\":\"F1\",\"objectValue\":\"F2\",\"oddDigitNumberValue\":\"03\",\"mapValue\":{\"04\":\"test_04\",\"0A\":\"test_0A\"}}";

  private static class Data {
    byte primitiveValue = (byte) 0xF1;
    Byte objectValue = (byte) 0xF2;
    byte oddDigitNumberValue = (byte) 0x3;
    Map<Byte, String> mapValue = new HashMap<Byte, String>();

    {
      mapValue.put((byte) 4, "test_04");
      mapValue.put((byte) 10, "test_0A");
    }
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
