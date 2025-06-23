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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.junit.BeforeClass;
import org.junit.Test;

public class DateJsonSerializerTest {

  private static String JSON_DATA;

  private static class Data {
    Date d = new Date(0);
  }

  @BeforeClass
  public static void beforeClass() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    sdf.setTimeZone(TimeZone.getTimeZone("CET"));
    JSON_DATA = "{\"d\":\"" + sdf.format(new Date(0)) + "\"}";
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
