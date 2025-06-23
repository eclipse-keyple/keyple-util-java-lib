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
package org.eclipse.keyple.core.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class ApduUtilTest {
  private static final byte CLA = (byte) 0x11;
  private static final byte INS = (byte) 0x22;
  private static final byte P1 = (byte) 0x33;
  private static final byte P2 = (byte) 0x44;
  private static final byte[] DATA_IN =
      new byte[] {(byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78};
  private static final Byte LE = 3;

  private static final byte[] CASE1 = HexUtil.toByteArray("1122334400");
  private static final byte[] CASE2 = HexUtil.toByteArray("1122334403");
  private static final byte[] CASE3 = HexUtil.toByteArray("112233440412345678");
  private static final byte[] CASE4 = HexUtil.toByteArray("11223344041234567803");

  @Test
  public void build_whenDataInAndLeAreNull_shouldReturnCase1() {
    byte[] apduCommand = ApduUtil.build(CLA, INS, P1, P2, null, null);
    assertThat(apduCommand).isEqualTo(CASE1);
  }

  @Test
  public void build_whenDataInIsNull_shouldReturnCase2() {
    byte[] apduCommand = ApduUtil.build(CLA, INS, P1, P2, null, LE);
    assertThat(apduCommand).isEqualTo(CASE2);
  }

  @Test
  public void build_whenLeIsNull_shouldReturnCase3() {
    byte[] apduCommand = ApduUtil.build(CLA, INS, P1, P2, DATA_IN, null);
    assertThat(apduCommand).isEqualTo(CASE3);
  }

  @Test
  public void build_whenDataInAndLeAreNotNull_shouldReturnCase4() {
    byte[] apduCommand = ApduUtil.build(CLA, INS, P1, P2, DATA_IN, LE);
    assertThat(apduCommand).isEqualTo(CASE4);
  }

  @Test
  public void isCase4_whenCase1_shouldReturnFalse() {
    assertThat(ApduUtil.isCase4(CASE1)).isFalse();
  }

  @Test
  public void isCase4_whenCase2_shouldReturnFalse() {
    assertThat(ApduUtil.isCase4(CASE2)).isFalse();
  }

  @Test
  public void isCase4_whenCase3_shouldReturnFalse() {
    assertThat(ApduUtil.isCase4(CASE3)).isFalse();
  }

  @Test
  public void isCase4_whenCase4_shouldReturnFalse() {
    assertThat(ApduUtil.isCase4(CASE4)).isTrue();
  }
}
