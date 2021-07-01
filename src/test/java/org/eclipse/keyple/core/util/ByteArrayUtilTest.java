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
}
