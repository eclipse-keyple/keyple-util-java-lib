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

public class ByteArrayUtilTest {

  private static final String HEXSTRING_ODD = "0102030";
  private static final String HEXSTRING_BAD = "010203ABGH8+";
  private static final String HEXSTRING_GOOD = "1234567890ABCDEFFEDCBA0987654321";
  private static final byte[] BYTEARRAY_LEN_1 = new byte[] {(byte) 0x12};
  private static final byte[] BYTEARRAY_LEN_2 = new byte[] {(byte) 0x12, (byte) 0x34};
  private static final byte[] BYTEARRAY_LEN_3 = new byte[] {(byte) 0x12, (byte) 0x34, (byte) 0x56};
  private static final byte[] BYTEARRAY_LEN_4 =
      new byte[] {(byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78};
  private static final byte[] BYTEARRAY_LEN_16 =
      new byte[] {
        (byte) 0x12,
        (byte) 0x34,
        (byte) 0x56,
        (byte) 0x78,
        (byte) 0x90,
        (byte) 0xAB,
        (byte) 0xCD,
        (byte) 0xEF,
        (byte) 0xFE,
        (byte) 0xDC,
        (byte) 0xBA,
        (byte) 0x09,
        (byte) 0x87,
        (byte) 0x65,
        (byte) 0x43,
        (byte) 0x21
      };

  @Test(expected = NullPointerException.class)
  public void extractBytes_byteArray_whenSrcIsNull_shouldThrowNPE() {
    ByteArrayUtil.extractBytes(null, 0, 1);
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void extractBytes_byteArray_whenBitOffsetIsOutOfRange_shouldThrowAIOOBE() {
    ByteArrayUtil.extractBytes(new byte[1], 16, 1);
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void extractBytes_byteArray_whenBitOffsetIsOutOfRange_shouldThrowAIOOBE2() {
    ByteArrayUtil.extractBytes(new byte[1], 9, 1);
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void extractBytes_byteArray_whenBitOffsetIsNegative_shouldThrowAIOOBE() {
    ByteArrayUtil.extractBytes(new byte[1], -8, 1);
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void extractBytes_byteArray_whenBitOffsetIsNegative_shouldThrowAIOOBE2() {
    ByteArrayUtil.extractBytes(new byte[1], -1, 1);
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void extractBytes_byteArray_whenNbBytesIsOutOfRange_shouldThrowAIOOBE() {
    ByteArrayUtil.extractBytes(new byte[1], 0, 2);
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void extractBytes_byteArray_whenNbBytesIsOutOfRange_shouldThrowAIOOBE2() {
    ByteArrayUtil.extractBytes(new byte[1], 1, 2);
  }

  @Test(expected = NegativeArraySizeException.class)
  public void extractBytes_byteArray_whenNbBytesIsNegative_shouldThrowAIOOBE() {
    ByteArrayUtil.extractBytes(new byte[1], 0, -1);
  }

  @Test(expected = NegativeArraySizeException.class)
  public void extractBytes_byteArray_whenNbBytesIsNegative_shouldThrowAIOOBE2() {
    ByteArrayUtil.extractBytes(new byte[1], 1, -1);
  }

  @Test
  public void extractBytes_byteArray_whenBitOffsetIsMultipleOf8_shouldBeSuccessful() {
    byte[] src = new byte[] {(byte) 0xF1, (byte) 0xF2, (byte) 0xF3};
    assertThat(ByteArrayUtil.extractBytes(src, 8, 1)).containsExactly(0xF2);
    assertThat(ByteArrayUtil.extractBytes(src, 8, 2)).containsExactly(0xF2, 0xF3);
  }

  @Test
  public void extractBytes_byteArray_whenBitOffsetIsNotMultipleOf8_shouldBeSuccessful() {
    byte[] src = new byte[] {(byte) 0xF1, (byte) 0xF2, (byte) 0xF3};
    assertThat(ByteArrayUtil.extractBytes(src, 6, 1)).containsExactly(0x7C);
    assertThat(ByteArrayUtil.extractBytes(src, 6, 2)).containsExactly(0x7C, 0xBC);
    assertThat(ByteArrayUtil.extractBytes(src, 3, 1)).containsExactly(0x8F);
  }

  @Test(expected = NegativeArraySizeException.class)
  public void extractBytes_number_AndNbBytesIsNegative_shouldThrowNASE() {
    ByteArrayUtil.extractBytes(0, -1);
  }

  @Test
  public void extractBytes_number_AndNbBytesIs0_shouldReturnAnEmptyArray() {
    assertThat(ByteArrayUtil.extractBytes(0xFF223344, 0)).isEmpty();
  }

  @Test
  public void extractBytes_number_AndNbBytesIs1to8_shouldExtractLastBytes() {
    // short
    short shortNumber = (short) 0xFF22;
    assertThat(ByteArrayUtil.extractBytes(shortNumber, 1)).containsExactly(0x22);
    assertThat(ByteArrayUtil.extractBytes(shortNumber, 2)).containsExactly(0xFF, 0x22);
    // integer
    int intNumber = 0xFF223344;
    assertThat(ByteArrayUtil.extractBytes(intNumber, 1)).containsExactly(0x44);
    assertThat(ByteArrayUtil.extractBytes(intNumber, 2)).containsExactly(0x33, 0x44);
    assertThat(ByteArrayUtil.extractBytes(intNumber, 3)).containsExactly(0x22, 0x33, 0x44);
    assertThat(ByteArrayUtil.extractBytes(intNumber, 4)).containsExactly(0xFF, 0x22, 0x33, 0x44);
    // long
    long longNumber = 0xFF22334455667788L;
    assertThat(ByteArrayUtil.extractBytes(longNumber, 1)).containsExactly(0x88);
    assertThat(ByteArrayUtil.extractBytes(longNumber, 2)).containsExactly(0x77, 0x88);
    assertThat(ByteArrayUtil.extractBytes(longNumber, 3)).containsExactly(0x66, 0x77, 0x88);
    assertThat(ByteArrayUtil.extractBytes(longNumber, 4)).containsExactly(0x55, 0x66, 0x77, 0x88);
    assertThat(ByteArrayUtil.extractBytes(longNumber, 5))
        .containsExactly(0x44, 0x55, 0x66, 0x77, 0x88);
    assertThat(ByteArrayUtil.extractBytes(longNumber, 6))
        .containsExactly(0x33, 0x44, 0x55, 0x66, 0x77, 0x88);
    assertThat(ByteArrayUtil.extractBytes(longNumber, 7))
        .containsExactly(0x22, 0x33, 0x44, 0x55, 0x66, 0x77, 0x88);
    assertThat(ByteArrayUtil.extractBytes(longNumber, 8))
        .containsExactly(0xFF, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77, 0x88);
  }

  @Test(expected = NullPointerException.class)
  public void extractShort_whenSrcIsNull_shouldThrowNPE() {
    ByteArrayUtil.extractShort(null, 0);
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void extractShort_whenOffsetIsNegative_shouldThrowAIOOBE() {
    ByteArrayUtil.extractShort(new byte[2], -1);
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void extractShort_whenOffsetIsGreaterThanSrcLengthMinus2_shouldThrowAIOOBE() {
    ByteArrayUtil.extractShort(new byte[2], 1);
  }

  @Test
  public void extractShort_whenInputIsOk_shouldBeSuccessful() {
    byte[] src =
        new byte[] {(byte) 0xF1, (byte) 0xF2, (byte) 0xF3, (byte) 0xF4, (byte) 0xF5, (byte) 0xF6};
    assertThat(ByteArrayUtil.extractShort(src, 1)).isEqualTo((short) 0xF2F3);
    assertThat(ByteArrayUtil.extractShort(src, 1)).isEqualTo((short) 0xF2F3);
  }

  @Test(expected = NullPointerException.class)
  public void extractInt_whenSrcIsNull_shouldThrowNPE() {
    ByteArrayUtil.extractInt(null, 0, 1, true);
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void extractInt_whenOffsetIsNegative_shouldThrowAIOOBE() {
    ByteArrayUtil.extractInt(new byte[1], -1, 1, true);
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void extractInt_whenOffsetIsGreaterThanSrcLengthMinusNbBytes_shouldThrowAIOOBE() {
    ByteArrayUtil.extractInt(new byte[1], 1, 1, true);
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void extractInt_whenNbBytesIsTooBig_shouldThrowAIOOBE() {
    ByteArrayUtil.extractInt(new byte[1], 0, 2, true);
  }

  @Test
  public void extractInt_whenInputIsOk_shouldBeSuccessful() {
    byte[] src =
        new byte[] {(byte) 0xF1, (byte) 0xF2, (byte) 0xF3, (byte) 0xF4, (byte) 0xF5, (byte) 0xF6};
    assertThat(ByteArrayUtil.extractInt(src, 1, 1, true)).isEqualTo(0xFFFFFFF2);
    assertThat(ByteArrayUtil.extractInt(src, 1, 1, false)).isEqualTo(0xF2);
    assertThat(ByteArrayUtil.extractInt(src, 1, 2, true)).isEqualTo(0xFFFFF2F3);
    assertThat(ByteArrayUtil.extractInt(src, 1, 2, false)).isEqualTo(0xF2F3);
    assertThat(ByteArrayUtil.extractInt(src, 1, 3, true)).isEqualTo(0xFFF2F3F4);
    assertThat(ByteArrayUtil.extractInt(src, 1, 3, false)).isEqualTo(0xF2F3F4);
    assertThat(ByteArrayUtil.extractInt(src, 1, 4, true)).isEqualTo(0xF2F3F4F5);
    assertThat(ByteArrayUtil.extractInt(src, 1, 4, false)).isEqualTo(0xF2F3F4F5);
  }

  @Test(expected = NullPointerException.class)
  public void extractLong_whenSrcIsNull_shouldThrowNPE() {
    ByteArrayUtil.extractLong(null, 0, 1, true);
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void extractLong_whenOffsetIsNegative_shouldThrowAIOOBE() {
    ByteArrayUtil.extractLong(new byte[1], -1, 1, true);
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void extractLong_whenOffsetIsGreaterThanSrcLengthMinusNbBytes_shouldThrowAIOOBE() {
    ByteArrayUtil.extractLong(new byte[1], 1, 1, true);
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void extractLong_whenNbBytesIsTooBig_shouldThrowAIOOBE() {
    ByteArrayUtil.extractLong(new byte[1], 0, 2, true);
  }

  @Test
  public void extractLong_whenInputIsOk_shouldBeSuccessful() {
    byte[] src =
        new byte[] {
          (byte) 0xF1,
          (byte) 0xF2,
          (byte) 0xF3,
          (byte) 0xF4,
          (byte) 0xF5,
          (byte) 0xF6,
          (byte) 0xF7,
          (byte) 0xF8,
          (byte) 0xF9,
          (byte) 0xFA
        };
    assertThat(ByteArrayUtil.extractLong(src, 1, 1, true)).isEqualTo(0xFFFFFFFFFFFFFFF2L);
    assertThat(ByteArrayUtil.extractLong(src, 1, 1, false)).isEqualTo(0xF2L);
    assertThat(ByteArrayUtil.extractLong(src, 1, 2, true)).isEqualTo(0xFFFFFFFFFFFFF2F3L);
    assertThat(ByteArrayUtil.extractLong(src, 1, 2, false)).isEqualTo(0xF2F3L);
    assertThat(ByteArrayUtil.extractLong(src, 1, 3, true)).isEqualTo(0xFFFFFFFFFFF2F3F4L);
    assertThat(ByteArrayUtil.extractLong(src, 1, 3, false)).isEqualTo(0xF2F3F4L);
    assertThat(ByteArrayUtil.extractLong(src, 1, 4, true)).isEqualTo(0xFFFFFFFFF2F3F4F5L);
    assertThat(ByteArrayUtil.extractLong(src, 1, 4, false)).isEqualTo(0xF2F3F4F5L);
    assertThat(ByteArrayUtil.extractLong(src, 1, 5, true)).isEqualTo(0xFFFFFFF2F3F4F5F6L);
    assertThat(ByteArrayUtil.extractLong(src, 1, 5, false)).isEqualTo(0xF2F3F4F5F6L);
    assertThat(ByteArrayUtil.extractLong(src, 1, 6, true)).isEqualTo(0xFFFFF2F3F4F5F6F7L);
    assertThat(ByteArrayUtil.extractLong(src, 1, 6, false)).isEqualTo(0xF2F3F4F5F6F7L);
    assertThat(ByteArrayUtil.extractLong(src, 1, 7, true)).isEqualTo(0xFFF2F3F4F5F6F7F8L);
    assertThat(ByteArrayUtil.extractLong(src, 1, 7, false)).isEqualTo(0xF2F3F4F5F6F7F8L);
    assertThat(ByteArrayUtil.extractLong(src, 1, 8, true)).isEqualTo(0xF2F3F4F5F6F7F8F9L);
    assertThat(ByteArrayUtil.extractLong(src, 1, 8, false)).isEqualTo(0xF2F3F4F5F6F7F8F9L);
  }

  @Test(expected = NullPointerException.class)
  public void copyBytes_whenDestIsNull_shouldThrowNPE() {
    ByteArrayUtil.copyBytes(0, null, 0, 0);
  }

  @Test
  public void copyBytes_whenDestIsEmpty_shouldThrow() {
    ByteArrayUtil.copyBytes(0, new byte[0], 0, 0);
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void copyBytes_whenOffsetIsNegative_shouldThrow() {
    ByteArrayUtil.copyBytes(0, new byte[1], -1, 0);
  }

  @Test(expected = NegativeArraySizeException.class)
  public void copyBytes_whenNbBytesIsNegative_shouldThrow() {
    ByteArrayUtil.copyBytes(0, new byte[1], 0, -1);
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void copyBytes_whenOffsetIsOutOfRange_shouldThrowAIOOBE() {
    ByteArrayUtil.copyBytes(0, new byte[1], 1, 1);
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void copyBytes_whenNbBytesIsOutOfRange_shouldThrow() {
    ByteArrayUtil.copyBytes(0, new byte[1], 0, 2);
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void copyBytes_whenOffsetAndNbBytesIsOutOfRange_shouldThrow() {
    ByteArrayUtil.copyBytes(0, new byte[2], 1, 2);
  }

  @Test
  public void copyBytes_whenNbBytesIs0_shouldDoNothing() {
    byte[] dest =
        new byte[] {(byte) 0xF1, (byte) 0xF2, (byte) 0xF3, (byte) 0xF4, (byte) 0xF5, (byte) 0xF6};
    ByteArrayUtil.copyBytes(0, dest, 0, 0);
    assertThat(dest[0]).isEqualTo((byte) 0xF1);
  }

  @Test
  public void copyBytes_whenSrcIsByte_shouldBeSuccess() {
    byte src = 0x11;
    byte[] dest =
        new byte[] {(byte) 0xF1, (byte) 0xF2, (byte) 0xF3, (byte) 0xF4, (byte) 0xF5, (byte) 0xF6};
    ByteArrayUtil.copyBytes(src, dest, 1, 1);
    assertThat(dest)
        .containsExactly(
            (byte) 0xF1, (byte) 0x11, (byte) 0xF3, (byte) 0xF4, (byte) 0xF5, (byte) 0xF6);
  }

  @Test
  public void copyBytes_whenSrcIsShort_shouldBeSuccess() {
    short src = 0x1122;
    byte[] dest =
        new byte[] {(byte) 0xF1, (byte) 0xF2, (byte) 0xF3, (byte) 0xF4, (byte) 0xF5, (byte) 0xF6};
    ByteArrayUtil.copyBytes(src, dest, 1, 1);
    assertThat(dest)
        .containsExactly(
            (byte) 0xF1, (byte) 0x22, (byte) 0xF3, (byte) 0xF4, (byte) 0xF5, (byte) 0xF6);
    ByteArrayUtil.copyBytes(src, dest, 3, 2);
    assertThat(dest)
        .containsExactly(
            (byte) 0xF1, (byte) 0x22, (byte) 0xF3, (byte) 0x11, (byte) 0x22, (byte) 0xF6);
  }

  @Test
  public void copyBytes_whenSrcIsInteger_shouldBeSuccess() {
    int src = 0x11223344;
    byte[] dest =
        new byte[] {(byte) 0xF1, (byte) 0xF2, (byte) 0xF3, (byte) 0xF4, (byte) 0xF5, (byte) 0xF6};
    ByteArrayUtil.copyBytes(src, dest, 1, 1);
    assertThat(dest)
        .containsExactly(
            (byte) 0xF1, (byte) 0x44, (byte) 0xF3, (byte) 0xF4, (byte) 0xF5, (byte) 0xF6);
    ByteArrayUtil.copyBytes(src, dest, 1, 2);
    assertThat(dest)
        .containsExactly(
            (byte) 0xF1, (byte) 0x33, (byte) 0x44, (byte) 0xF4, (byte) 0xF5, (byte) 0xF6);
    ByteArrayUtil.copyBytes(src, dest, 1, 3);
    assertThat(dest)
        .containsExactly(
            (byte) 0xF1, (byte) 0x22, (byte) 0x33, (byte) 0x44, (byte) 0xF5, (byte) 0xF6);
    ByteArrayUtil.copyBytes(src, dest, 1, 4);
    assertThat(dest)
        .containsExactly(
            (byte) 0xF1, (byte) 0x11, (byte) 0x22, (byte) 0x33, (byte) 0x44, (byte) 0xF6);
  }

  @Test
  public void copyBytes_whenSrcIsLong_shouldBeSuccess() {
    long src = 0x1122334455667788L;
    byte[] dest =
        new byte[] {
          (byte) 0xF1,
          (byte) 0xF2,
          (byte) 0xF3,
          (byte) 0xF4,
          (byte) 0xF5,
          (byte) 0xF6,
          (byte) 0xF7,
          (byte) 0xF8
        };
    ByteArrayUtil.copyBytes(src, dest, 0, 1);
    assertThat(dest)
        .containsExactly(
            (byte) 0x88,
            (byte) 0xF2,
            (byte) 0xF3,
            (byte) 0xF4,
            (byte) 0xF5,
            (byte) 0xF6,
            (byte) 0xF7,
            (byte) 0xF8);
    ByteArrayUtil.copyBytes(src, dest, 0, 2);
    assertThat(dest)
        .containsExactly(
            (byte) 0x77,
            (byte) 0X88,
            (byte) 0xF3,
            (byte) 0xF4,
            (byte) 0xF5,
            (byte) 0xF6,
            (byte) 0xF7,
            (byte) 0xF8);
    ByteArrayUtil.copyBytes(src, dest, 0, 3);
    assertThat(dest)
        .containsExactly(
            (byte) 0x66,
            (byte) 0X77,
            (byte) 0X88,
            (byte) 0xF4,
            (byte) 0xF5,
            (byte) 0xF6,
            (byte) 0xF7,
            (byte) 0xF8);
    ByteArrayUtil.copyBytes(src, dest, 0, 4);
    assertThat(dest)
        .containsExactly(
            (byte) 0x55,
            (byte) 0X66,
            (byte) 0X77,
            (byte) 0X88,
            (byte) 0xF5,
            (byte) 0xF6,
            (byte) 0xF7,
            (byte) 0xF8);
    ByteArrayUtil.copyBytes(src, dest, 0, 5);
    assertThat(dest)
        .containsExactly(
            (byte) 0x44,
            (byte) 0x55,
            (byte) 0X66,
            (byte) 0X77,
            (byte) 0X88,
            (byte) 0xF6,
            (byte) 0xF7,
            (byte) 0xF8);
    ByteArrayUtil.copyBytes(src, dest, 0, 6);
    assertThat(dest)
        .containsExactly(
            (byte) 0x33,
            (byte) 0x44,
            (byte) 0x55,
            (byte) 0X66,
            (byte) 0X77,
            (byte) 0X88,
            (byte) 0xF7,
            (byte) 0xF8);
    ByteArrayUtil.copyBytes(src, dest, 0, 7);
    assertThat(dest)
        .containsExactly(
            (byte) 0x22,
            (byte) 0x33,
            (byte) 0x44,
            (byte) 0x55,
            (byte) 0X66,
            (byte) 0X77,
            (byte) 0X88,
            (byte) 0xF8);
    ByteArrayUtil.copyBytes(src, dest, 0, 8);
    assertThat(dest)
        .containsExactly(
            (byte) 0x11,
            (byte) 0x22,
            (byte) 0x33,
            (byte) 0x44,
            (byte) 0x55,
            (byte) 0X66,
            (byte) 0X77,
            (byte) 0x88);
  }

  @Test
  public void isValidHexString_null() {
    assertThat(ByteArrayUtil.isValidHexString(null)).isFalse();
  }

  @Test
  public void isValidHexString_valid() {
    assertThat(ByteArrayUtil.isValidHexString("0123456789ABCDEF")).isTrue();
  }

  @Test
  public void isValidHexString_invalid() {
    assertThat(ByteArrayUtil.isValidHexString("0123456789ABCDE")).isFalse();
    assertThat(ByteArrayUtil.isValidHexString("01 23456789ABCDEF")).isFalse();
    assertThat(ByteArrayUtil.isValidHexString("0123456789ABCDEG")).isFalse();
  }

  @Test(expected = IllegalArgumentException.class)
  public void fromHex_null() {
    byte[] bytes = ByteArrayUtil.fromHex(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void fromHex_empty() {
    byte[] bytes = ByteArrayUtil.fromHex("");
    assertThat(bytes).isEmpty();
  }

  @Test(expected = IllegalArgumentException.class)
  public void fromHex_odd_length() {
    byte[] bytes = ByteArrayUtil.fromHex(HEXSTRING_ODD);
  }

  @Test
  public void fromHex_bad_hex() {
    // no verification is being carried out at the moment.
    byte[] bytes = ByteArrayUtil.fromHex(HEXSTRING_BAD);
    // just check that the conversion is wrong
    String hex = ByteArrayUtil.toHex(bytes);
    assertThat(hex).isNotEqualTo(HEXSTRING_BAD);
  }

  @Test
  public void fromHex_good_hex() {
    // no verification is being carried out at the moment.
    byte[] bytes = ByteArrayUtil.fromHex(HEXSTRING_GOOD);
    assertThat(bytes).isEqualTo(BYTEARRAY_LEN_16);
  }

  @Test
  public void toHex_null() {
    String hex = ByteArrayUtil.toHex(null);
    assertThat(hex).isEmpty();
  }

  @Test
  public void toHex_empty() {
    byte[] bytes = new byte[0];
    String hex = ByteArrayUtil.toHex(bytes);
    assertThat(hex).isEmpty();
  }

  @Test
  public void toHex_bytearray_good() {
    String hex = ByteArrayUtil.toHex(BYTEARRAY_LEN_16);
    assertThat(hex).isEqualTo(HEXSTRING_GOOD);
  }

  @Test(expected = NullPointerException.class)
  public void twoBytesToInt_null() {
    int value = ByteArrayUtil.twoBytesToInt(null, 0);
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void twoBytesToInt_negative_offset() {
    int value = ByteArrayUtil.twoBytesToInt(BYTEARRAY_LEN_16, -1);
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void twoBytesToInt_too_short_buffer_1() {
    int value = ByteArrayUtil.twoBytesToInt(BYTEARRAY_LEN_1, 0);
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void twoBytesToInt_too_short_buffer_2() {
    int value = ByteArrayUtil.twoBytesToInt(BYTEARRAY_LEN_3, 2);
  }

  @Test
  public void twoBytesToInt_buffer_ok_1() {
    int value = ByteArrayUtil.twoBytesToInt(BYTEARRAY_LEN_3, 0);
    assertThat(value).isEqualTo(0x1234);
  }

  @Test
  public void twoBytesToInt_buffer_ok_2() {
    int value = ByteArrayUtil.twoBytesToInt(BYTEARRAY_LEN_16, 0);
    assertThat(value).isEqualTo(0x1234);
  }

  @Test
  public void twoBytesToInt_buffer_ok_3() {
    int value = ByteArrayUtil.twoBytesToInt(BYTEARRAY_LEN_16, 1);
    assertThat(value).isEqualTo(0x3456);
  }

  @Test
  public void twoBytesToInt_buffer_ok_4() {
    int value = ByteArrayUtil.twoBytesToInt(BYTEARRAY_LEN_16, 4);
    assertThat(value).isEqualTo(0x90AB);
  }

  @Test
  public void twoBytesToInt_buffer_ok_5() {
    int value = ByteArrayUtil.twoBytesToInt(BYTEARRAY_LEN_16, 14);
    assertThat(value).isEqualTo(0x4321);
  }

  @Test
  public void twoBytesSignedToInt_buffer_ok_1() {
    int value = ByteArrayUtil.twoBytesSignedToInt(BYTEARRAY_LEN_16, 0);
    assertThat(value).isEqualTo(0x1234);
  }

  @Test
  public void twoBytesSignedToInt_buffer_ok_2() {
    int value = ByteArrayUtil.twoBytesSignedToInt(BYTEARRAY_LEN_16, 4);
    // 0x90AB{2 bytes} = -28501
    assertThat(value).isEqualTo(-28501);
  }

  @Test(expected = NullPointerException.class)
  public void threeBytesToInt_null() {
    int value = ByteArrayUtil.threeBytesToInt(null, 0);
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void threeBytesToInt_negative_offset() {
    int value = ByteArrayUtil.threeBytesToInt(BYTEARRAY_LEN_16, -1);
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void threeBytesToInt_too_short_buffer_1() {
    int value = ByteArrayUtil.threeBytesToInt(BYTEARRAY_LEN_2, 0);
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void threeBytesToInt_too_short_buffer_2() {
    int value = ByteArrayUtil.threeBytesToInt(BYTEARRAY_LEN_3, 1);
  }

  @Test
  public void threeBytesToInt_buffer_ok_1() {
    int value = ByteArrayUtil.threeBytesToInt(BYTEARRAY_LEN_3, 0);
    assertThat(value).isEqualTo(0x123456);
  }

  @Test
  public void threeBytesToInt_buffer_ok_2() {
    int value = ByteArrayUtil.threeBytesToInt(BYTEARRAY_LEN_16, 0);
    assertThat(value).isEqualTo(0x123456);
  }

  @Test
  public void threeBytesToInt_buffer_ok_3() {
    int value = ByteArrayUtil.threeBytesToInt(BYTEARRAY_LEN_16, 1);
    assertThat(value).isEqualTo(0x345678);
  }

  @Test
  public void threeBytesToInt_buffer_ok_4() {
    int value = ByteArrayUtil.threeBytesToInt(BYTEARRAY_LEN_16, 4);
    assertThat(value).isEqualTo(0x90ABCD);
  }

  @Test
  public void threeBytesToInt_buffer_ok_5() {
    int value = ByteArrayUtil.threeBytesToInt(BYTEARRAY_LEN_16, 13);
    assertThat(value).isEqualTo(0x654321);
  }

  @Test
  public void threeBytesSignedToInt_buffer_ok_1() {
    int value = ByteArrayUtil.threeBytesSignedToInt(BYTEARRAY_LEN_16, 0);
    assertThat(value).isEqualTo(0x123456);
  }

  @Test
  public void threeBytesSignedToInt_buffer_ok_2() {
    int value = ByteArrayUtil.threeBytesSignedToInt(BYTEARRAY_LEN_16, 4);
    // 0xFF90ABCD{4 bytes} = -7296051
    assertThat(value).isEqualTo(-7296051);
  }

  @Test(expected = NullPointerException.class)
  public void fourBytesToInt_null() {
    int value = ByteArrayUtil.fourBytesToInt(null, 0);
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void fourBytesToInt_negative_offset() {
    int value = ByteArrayUtil.fourBytesToInt(BYTEARRAY_LEN_16, -1);
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void fourBytesToInt_too_short_buffer_1() {
    int value = ByteArrayUtil.fourBytesToInt(BYTEARRAY_LEN_3, 0);
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void fourBytesToInt_too_short_buffer_2() {
    int value = ByteArrayUtil.fourBytesToInt(BYTEARRAY_LEN_4, 1);
  }

  @Test
  public void fourBytesToInt_buffer_ok_1() {
    int value = ByteArrayUtil.fourBytesToInt(BYTEARRAY_LEN_4, 0);
    assertThat(value).isEqualTo(0x12345678);
  }

  @Test
  public void fourBytesToInt_buffer_ok_2() {
    int value = ByteArrayUtil.fourBytesToInt(BYTEARRAY_LEN_16, 0);
    assertThat(value).isEqualTo(0x12345678);
  }

  @Test
  public void fourBytesToInt_buffer_ok_3() {
    int value = ByteArrayUtil.fourBytesToInt(BYTEARRAY_LEN_16, 1);
    assertThat(value).isEqualTo(0x34567890);
  }

  @Test
  public void fourBytesToInt_buffer_ok_4() {
    int value = ByteArrayUtil.fourBytesToInt(BYTEARRAY_LEN_16, 4);
    assertThat(value).isEqualTo(0x90ABCDEF);
  }

  @Test
  public void fourBytesToInt_buffer_ok_5() {
    int value = ByteArrayUtil.fourBytesToInt(BYTEARRAY_LEN_16, 12);
    assertThat(value).isEqualTo(0x87654321);
  }
}
