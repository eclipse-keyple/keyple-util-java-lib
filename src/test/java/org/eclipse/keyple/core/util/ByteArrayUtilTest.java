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
  private static final byte[] BYTEARRAY_LEN_1 = new byte[] {(byte) 0x12};
  private static final byte[] BYTEARRAY_LEN_2 = new byte[] {(byte) 0x12, (byte) 0x34};
  private static final byte[] BYTEARRAY_LEN_3 = new byte[] {(byte) 0x12, (byte) 0x34, (byte) 0x56};
  private static final byte[] BYTEARRAY_LEN_4 =
      new byte[] {(byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78};

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

  @Test(expected = IllegalArgumentException.class)
  public void hexToByte_whenHexIsNull_shouldThrowIAE() {
    ByteArrayUtil.hexToByte(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void hexToByte_whenHexIsEmpty_shouldThrowIAE() {
    ByteArrayUtil.hexToByte("");
  }

  @Test(expected = IllegalArgumentException.class)
  public void hexToByte_whenHexLengthIsOdd_shouldThrowIAE() {
    ByteArrayUtil.hexToByte("1");
  }

  @Test(expected = IllegalArgumentException.class)
  public void hexToByte_whenHexLengthIsGreaterThan2_shouldThrowIAE() {
    ByteArrayUtil.hexToByte("1234");
  }

  @Test
  public void hexToByte_whenHexIsValid_shouldBeSuccessful() {
    assertThat(ByteArrayUtil.hexToByte("AB")).isEqualTo((byte) 0xAB);
    assertThat(ByteArrayUtil.hexToByte("CD")).isEqualTo((byte) 0xCD);
    assertThat(ByteArrayUtil.hexToByte("EF")).isEqualTo((byte) 0xEF);
    assertThat(ByteArrayUtil.hexToByte("ab")).isEqualTo((byte) 0xAB);
    assertThat(ByteArrayUtil.hexToByte("cd")).isEqualTo((byte) 0xCD);
    assertThat(ByteArrayUtil.hexToByte("ef")).isEqualTo((byte) 0xEF);
  }

  @Test(expected = IllegalArgumentException.class)
  public void hexToShort_whenHexIsNull_shouldThrowIAE() {
    ByteArrayUtil.hexToShort(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void hexToShort_whenHexIsEmpty_shouldThrowIAE() {
    ByteArrayUtil.hexToShort("");
  }

  @Test(expected = IllegalArgumentException.class)
  public void hexToShort_whenHexLengthIsOdd_shouldThrowIAE() {
    ByteArrayUtil.hexToShort("1");
  }

  @Test(expected = IllegalArgumentException.class)
  public void hexToShort_whenHexLengthIsGreaterThan4_shouldThrowIAE() {
    ByteArrayUtil.hexToShort("123456");
  }

  @Test
  public void hexToShort_whenHexIsValid_shouldBeSuccessful() {
    assertThat(ByteArrayUtil.hexToShort("ABCD")).isEqualTo((short) 0xABCD);
    assertThat(ByteArrayUtil.hexToShort("EF")).isEqualTo((short) 0xEF);
    assertThat(ByteArrayUtil.hexToShort("abcd")).isEqualTo((short) 0xABCD);
    assertThat(ByteArrayUtil.hexToShort("ef")).isEqualTo((short) 0xEF);
  }

  @Test(expected = IllegalArgumentException.class)
  public void hexToInt_whenHexIsNull_shouldThrowIAE() {
    ByteArrayUtil.hexToInt(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void hexToInt_whenHexIsEmpty_shouldThrowIAE() {
    ByteArrayUtil.hexToInt("");
  }

  @Test(expected = IllegalArgumentException.class)
  public void hexToInt_whenHexLengthIsOdd_shouldThrowIAE() {
    ByteArrayUtil.hexToInt("1");
  }

  @Test(expected = IllegalArgumentException.class)
  public void hexToInt_whenHexLengthIsGreaterThan8_shouldThrowIAE() {
    ByteArrayUtil.hexToInt("123456789A");
  }

  @Test
  public void hexToInt_whenHexIsValid_shouldBeSuccessful() {
    assertThat(ByteArrayUtil.hexToInt("FE")).isEqualTo(0xFE);
    assertThat(ByteArrayUtil.hexToInt("FEF7")).isEqualTo(0xFEF7);
    assertThat(ByteArrayUtil.hexToInt("FEF712")).isEqualTo(0xFEF712);
    assertThat(ByteArrayUtil.hexToInt("FEF71234")).isEqualTo(0xFEF71234);
    assertThat(ByteArrayUtil.hexToInt("ABCDEF")).isEqualTo(0xABCDEF);
    assertThat(ByteArrayUtil.hexToInt("abcdef")).isEqualTo(0xABCDEF);
  }

  @Test(expected = IllegalArgumentException.class)
  public void hexToLong_whenHexIsNull_shouldThrowIAE() {
    ByteArrayUtil.hexToLong(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void hexToLong_whenHexIsEmpty_shouldThrowIAE() {
    ByteArrayUtil.hexToLong("");
  }

  @Test(expected = IllegalArgumentException.class)
  public void hexToLong_whenHexLengthIsOdd_shouldThrowIAE() {
    ByteArrayUtil.hexToLong("1");
  }

  @Test(expected = IllegalArgumentException.class)
  public void hexToLong_whenHexLengthIsGreaterThan16_shouldThrowIAE() {
    ByteArrayUtil.hexToLong("123456789ABCDEF012");
  }

  @Test
  public void hexToLong_whenHexIsValid_shouldBeSuccessful() {
    assertThat(ByteArrayUtil.hexToLong("FE")).isEqualTo(0xFEL);
    assertThat(ByteArrayUtil.hexToLong("FEF7")).isEqualTo(0xFEF7L);
    assertThat(ByteArrayUtil.hexToLong("FEF712")).isEqualTo(0xFEF712L);
    assertThat(ByteArrayUtil.hexToLong("FEF71234")).isEqualTo(0xFEF71234L);
    assertThat(ByteArrayUtil.hexToLong("FEF7123456")).isEqualTo(0xFEF7123456L);
    assertThat(ByteArrayUtil.hexToLong("FEF712345678")).isEqualTo(0xFEF712345678L);
    assertThat(ByteArrayUtil.hexToLong("FEF7123456789A")).isEqualTo(0xFEF7123456789AL);
    assertThat(ByteArrayUtil.hexToLong("FEF7123456789ABC")).isEqualTo(0xFEF7123456789ABCL);
    assertThat(ByteArrayUtil.hexToLong("ABCDEF")).isEqualTo(0xABCDEFL);
    assertThat(ByteArrayUtil.hexToLong("abcdef")).isEqualTo(0xABCDEFL);
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

  @Test
  public void toHex_byte() {
    assertThat(ByteArrayUtil.toHex((byte) 0xFE)).isEqualTo("FE");
  }

  @Test
  public void toHex_short() {
    assertThat(ByteArrayUtil.toHex((short) 0xFE)).isEqualTo("FE");
    assertThat(ByteArrayUtil.toHex((short) 0xFE34)).isEqualTo("FE34");
  }

  @Test
  public void toHex_int() {
    assertThat(ByteArrayUtil.toHex(0xFE)).isEqualTo("FE");
    assertThat(ByteArrayUtil.toHex(0xFE34)).isEqualTo("FE34");
    assertThat(ByteArrayUtil.toHex(0xFE3456)).isEqualTo("FE3456");
    assertThat(ByteArrayUtil.toHex(0xFE345678)).isEqualTo("FE345678");
  }

  @Test
  public void toHex_long() {
    assertThat(ByteArrayUtil.toHex(0xFEL)).isEqualTo("FE");
    assertThat(ByteArrayUtil.toHex(0xFE34L)).isEqualTo("FE34");
    assertThat(ByteArrayUtil.toHex(0xFE3456L)).isEqualTo("FE3456");
    assertThat(ByteArrayUtil.toHex(0xFE345678L)).isEqualTo("FE345678");
    assertThat(ByteArrayUtil.toHex(0xFE3456789AL)).isEqualTo("FE3456789A");
    assertThat(ByteArrayUtil.toHex(0xFE3456789ABCL)).isEqualTo("FE3456789ABC");
    assertThat(ByteArrayUtil.toHex(0xFE3456789ABCDEL)).isEqualTo("FE3456789ABCDE");
    assertThat(ByteArrayUtil.toHex(0xFE3456789ABCDEF0L)).isEqualTo("FE3456789ABCDEF0");
  }

  @Test(expected = IllegalArgumentException.class)
  public void twoBytesToInt_null() {
    int value = ByteArrayUtil.twoBytesToInt(null, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void twoBytesToInt_negative_offset() {
    int value = ByteArrayUtil.twoBytesToInt(BYTEARRAY_LEN_16, -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void twoBytesToInt_too_short_buffer_1() {
    int value = ByteArrayUtil.twoBytesToInt(BYTEARRAY_LEN_1, 0);
  }

  @Test(expected = IllegalArgumentException.class)
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

  @Test(expected = IllegalArgumentException.class)
  public void threeBytesToInt_null() {
    int value = ByteArrayUtil.threeBytesToInt(null, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void threeBytesToInt_negative_offset() {
    int value = ByteArrayUtil.threeBytesToInt(BYTEARRAY_LEN_16, -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void threeBytesToInt_too_short_buffer_1() {
    int value = ByteArrayUtil.threeBytesToInt(BYTEARRAY_LEN_2, 0);
  }

  @Test(expected = IllegalArgumentException.class)
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

  @Test(expected = IllegalArgumentException.class)
  public void fourBytesToInt_null() {
    int value = ByteArrayUtil.fourBytesToInt(null, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void fourBytesToInt_negative_offset() {
    int value = ByteArrayUtil.fourBytesToInt(BYTEARRAY_LEN_16, -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void fourBytesToInt_too_short_buffer_1() {
    int value = ByteArrayUtil.fourBytesToInt(BYTEARRAY_LEN_3, 0);
  }

  @Test(expected = IllegalArgumentException.class)
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

  @Test(expected = NullPointerException.class)
  public void extract_whenSrcIsNull_shouldThrowNPE() {
    ByteArrayUtil.extract(null, 0, 1);
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void extract_whenBitOffsetIsOutOfRange_shouldThrowAIOOBE() {
    ByteArrayUtil.extract(new byte[1], 16, 1);
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void extract_whenBitOffsetIsOutOfRange_shouldThrowAIOOBE2() {
    ByteArrayUtil.extract(new byte[1], 9, 1);
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void extract_whenBitOffsetIsNegative_shouldThrowAIOOBE() {
    ByteArrayUtil.extract(new byte[1], -8, 1);
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void extract_whenBitOffsetIsNegative_shouldThrowAIOOBE2() {
    ByteArrayUtil.extract(new byte[1], -1, 1);
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void extract_whenNbBytesIsOutOfRange_shouldThrowAIOOBE() {
    ByteArrayUtil.extract(new byte[1], 0, 2);
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void extract_whenNbBytesIsOutOfRange_shouldThrowAIOOBE2() {
    ByteArrayUtil.extract(new byte[1], 1, 2);
  }

  @Test(expected = NegativeArraySizeException.class)
  public void extract_whenNbBytesIsNegative_shouldThrowAIOOBE() {
    ByteArrayUtil.extract(new byte[1], 0, -1);
  }

  @Test(expected = NegativeArraySizeException.class)
  public void extract_whenNbBytesIsNegative_shouldThrowAIOOBE2() {
    ByteArrayUtil.extract(new byte[1], 1, -1);
  }

  @Test
  public void extract_whenBitOffsetIsMultipleOf8_shouldBeSuccess() {
    byte[] src = new byte[] {(byte) 0xF1, (byte) 0xF2, (byte) 0xF3};
    assertThat(ByteArrayUtil.extract(src, 8, 1)).containsExactly(0xF2);
    assertThat(ByteArrayUtil.extract(src, 8, 2)).containsExactly(0xF2, 0xF3);
  }

  @Test
  public void extract_whenBitOffsetIsNotMultipleOf8_shouldBeSuccess() {
    byte[] src = new byte[] {(byte) 0xF1, (byte) 0xF2, (byte) 0xF3};
    assertThat(ByteArrayUtil.extract(src, 6, 1)).containsExactly(0x7C);
    assertThat(ByteArrayUtil.extract(src, 6, 2)).containsExactly(0x7C, 0xBC);
    assertThat(ByteArrayUtil.extract(src, 3, 1)).containsExactly(0x8F);
  }
}
