/* **************************************************************************************
 * Copyright (c) 2022 Calypso Networks Association https://calypsonet.org/
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

public class HexUtilTest {

  @Test
  public void isValid_whenHexIsNull_shouldReturnFalse() {
    assertThat(HexUtil.isValid(null)).isFalse();
  }

  @Test
  public void isValid_whenHexIsValid_shouldReturnTrue() {
    assertThat(HexUtil.isValid("0123456789ABCDEF")).isTrue();
  }

  @Test
  public void isValid_whenHexHasOddLength_shouldReturnFalse() {
    assertThat(HexUtil.isValid("0123456789ABCDE")).isFalse();
  }

  @Test
  public void isValid_whenHexContainsEmptySpaces_shouldReturnFalse() {
    assertThat(HexUtil.isValid("01 23456789ABCDEF")).isFalse();
  }

  @Test
  public void isValid_whenHexContainsNotHexDigits_shouldReturnFalse() {
    assertThat(HexUtil.isValid("0123456789ABCDEG")).isFalse();
  }

  @Test
  public void toByteArray_whenHexIsNull_shouldReturnEmptyArray() {
    assertThat(HexUtil.toByteArray(null)).isEmpty();
  }

  @Test
  public void toByteArray_whenHexIsEmpty_shouldReturnEmptyArray() {
    assertThat(HexUtil.toByteArray("")).isEmpty();
  }

  @Test(expected = StringIndexOutOfBoundsException.class)
  public void toByteArray_whenHexIsOddLength_shouldThrowSIOOBE() {
    assertThat(HexUtil.toByteArray("1")).containsExactly((byte) 0x1);
  }

  @Test
  public void toByteArray_whenHexIsValid_shouldBeSuccessful() {
    assertThat(HexUtil.toByteArray("ABCDEFabcdef"))
        .containsExactly(
            (byte) 0xAB, (byte) 0xCD, (byte) 0xEF, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF);
  }

  @Test
  public void toByte_whenHexIsNull_shouldReturn0() {
    assertThat(HexUtil.toByte(null)).isZero();
  }

  @Test
  public void toByte_whenHexIsEmpty_shouldReturn0() {
    assertThat(HexUtil.toByte("")).isZero();
  }

  @Test
  public void toByte_whenHexLengthIsGreaterThan2_shouldTruncateLeft() {
    assertThat(HexUtil.toByte("1234")).isEqualTo((byte) 0x34);
  }

  @Test
  public void toByte_whenHexIsValid_shouldBeSuccessful() {
    assertThat(HexUtil.toByte("1")).isEqualTo((byte) 0x1);
    assertThat(HexUtil.toByte("AB")).isEqualTo((byte) 0xAB);
    assertThat(HexUtil.toByte("CD")).isEqualTo((byte) 0xCD);
    assertThat(HexUtil.toByte("EF")).isEqualTo((byte) 0xEF);
    assertThat(HexUtil.toByte("ab")).isEqualTo((byte) 0xAB);
    assertThat(HexUtil.toByte("cd")).isEqualTo((byte) 0xCD);
    assertThat(HexUtil.toByte("ef")).isEqualTo((byte) 0xEF);
  }

  @Test
  public void toShort_whenHexIsNull_shouldReturn0() {
    assertThat(HexUtil.toShort(null)).isZero();
  }

  @Test
  public void toShort_whenHexIsEmpty_shouldReturn0() {
    assertThat(HexUtil.toShort("")).isZero();
  }

  @Test
  public void toShort_whenHexLengthIsGreaterThan4_shouldTruncateLeft() {
    assertThat(HexUtil.toShort("123456")).isEqualTo((short) 0x3456);
  }

  @Test
  public void toShort_whenHexIsValid_shouldBeSuccessful() {
    assertThat(HexUtil.toShort("1")).isEqualTo((short) 0x1);
    assertThat(HexUtil.toShort("ABCD")).isEqualTo((short) 0xABCD);
    assertThat(HexUtil.toShort("EF")).isEqualTo((short) 0xEF);
    assertThat(HexUtil.toShort("abcd")).isEqualTo((short) 0xABCD);
    assertThat(HexUtil.toShort("ef")).isEqualTo((short) 0xEF);
  }

  @Test
  public void toInt_whenHexIsNull_shouldReturn0() {
    assertThat(HexUtil.toInt(null)).isZero();
  }

  @Test
  public void toInt_whenHexIsEmpty_shouldReturn0() {
    assertThat(HexUtil.toInt("")).isZero();
  }

  @Test
  public void toInt_whenHexLengthIsGreaterThan8_shouldTruncateLeft() {
    assertThat(HexUtil.toInt("123456789A")).isEqualTo(0x3456789A);
  }

  @Test
  public void toInt_whenHexIsValid_shouldBeSuccessful() {
    assertThat(HexUtil.toInt("1")).isEqualTo(0x1);
    assertThat(HexUtil.toInt("FE")).isEqualTo(0xFE);
    assertThat(HexUtil.toInt("FEF7")).isEqualTo(0xFEF7);
    assertThat(HexUtil.toInt("FEF712")).isEqualTo(0xFEF712);
    assertThat(HexUtil.toInt("FEF71234")).isEqualTo(0xFEF71234);
    assertThat(HexUtil.toInt("ABCDEF")).isEqualTo(0xABCDEF);
    assertThat(HexUtil.toInt("abcdef")).isEqualTo(0xABCDEF);
  }

  @Test
  public void toLong_whenHexIsNull_shouldReturn0() {
    assertThat(HexUtil.toLong(null)).isZero();
  }

  @Test
  public void toLong_whenHexIsEmpty_shouldReturn0() {
    assertThat(HexUtil.toLong("")).isZero();
  }

  @Test
  public void toLong_whenHexLengthIsGreaterThan16_shouldTruncateRight() {
    assertThat(HexUtil.toLong("123456789ABCDEF012")).isEqualTo(0x3456789ABCDEF012L);
  }

  @Test
  public void toLong_whenHexIsValid_shouldBeSuccessful() {
    assertThat(HexUtil.toLong("1")).isEqualTo(0x1L);
    assertThat(HexUtil.toLong("FE")).isEqualTo(0xFEL);
    assertThat(HexUtil.toLong("FEF7")).isEqualTo(0xFEF7L);
    assertThat(HexUtil.toLong("FEF712")).isEqualTo(0xFEF712L);
    assertThat(HexUtil.toLong("FEF71234")).isEqualTo(0xFEF71234L);
    assertThat(HexUtil.toLong("FEF7123456")).isEqualTo(0xFEF7123456L);
    assertThat(HexUtil.toLong("FEF712345678")).isEqualTo(0xFEF712345678L);
    assertThat(HexUtil.toLong("FEF7123456789A")).isEqualTo(0xFEF7123456789AL);
    assertThat(HexUtil.toLong("FEF7123456789ABC")).isEqualTo(0xFEF7123456789ABCL);
    assertThat(HexUtil.toLong("ABCDEF")).isEqualTo(0xABCDEFL);
    assertThat(HexUtil.toLong("abcdef")).isEqualTo(0xABCDEFL);
  }

  @Test
  public void toHex_null() {
    assertThat(HexUtil.toHex(null)).isEmpty();
  }

  @Test
  public void toHex_byte_array() {
    assertThat(HexUtil.toHex(new byte[] {(byte) 0xFE})).isEqualTo("FE");
  }

  @Test
  public void toHex_byte() {
    assertThat(HexUtil.toHex((byte) 0xFE)).isEqualTo("FE");
  }

  @Test
  public void toHex_short() {
    assertThat(HexUtil.toHex((short) 0xFE)).isEqualTo("FE");
    assertThat(HexUtil.toHex((short) 0xFE34)).isEqualTo("FE34");
  }

  @Test
  public void toHex_int() {
    assertThat(HexUtil.toHex(0xFE)).isEqualTo("FE");
    assertThat(HexUtil.toHex(0xFE34)).isEqualTo("FE34");
    assertThat(HexUtil.toHex(0xFE3456)).isEqualTo("FE3456");
    assertThat(HexUtil.toHex(0xFE345678)).isEqualTo("FE345678");
  }

  @Test
  public void toHex_long() {
    assertThat(HexUtil.toHex(0xFEL)).isEqualTo("FE");
    assertThat(HexUtil.toHex(0xFE34L)).isEqualTo("FE34");
    assertThat(HexUtil.toHex(0xFE3456L)).isEqualTo("FE3456");
    assertThat(HexUtil.toHex(0xFE345678L)).isEqualTo("FE345678");
    assertThat(HexUtil.toHex(0xFE3456789AL)).isEqualTo("FE3456789A");
    assertThat(HexUtil.toHex(0xFE3456789ABCL)).isEqualTo("FE3456789ABC");
    assertThat(HexUtil.toHex(0xFE3456789ABCDEL)).isEqualTo("FE3456789ABCDE");
    assertThat(HexUtil.toHex(0xFE3456789ABCDEF0L)).isEqualTo("FE3456789ABCDEF0");
  }
}
