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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class JsonUtilTest {

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
}
