/* **************************************************************************************
 * Copyright (c) 2021 Calypso Networks Association https://www.calypsonet-asso.org/
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

import java.util.regex.Pattern;

/**
 * Utils around byte arrays
 *
 * @since 2.0
 */
public final class ByteArrayUtil {
  /* byte to hex string conversion table */
  private static final String[] byteToHex =
      new String[] {
        "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "0A", "0B", "0C", "0D", "0E",
        "0F", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "1A", "1B", "1C", "1D",
        "1E", "1F", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "2A", "2B", "2C",
        "2D", "2E", "2F", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "3A", "3B",
        "3C", "3D", "3E", "3F", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "4A",
        "4B", "4C", "4D", "4E", "4F", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59",
        "5A", "5B", "5C", "5D", "5E", "5F", "60", "61", "62", "63", "64", "65", "66", "67", "68",
        "69", "6A", "6B", "6C", "6D", "6E", "6F", "70", "71", "72", "73", "74", "75", "76", "77",
        "78", "79", "7A", "7B", "7C", "7D", "7E", "7F", "80", "81", "82", "83", "84", "85", "86",
        "87", "88", "89", "8A", "8B", "8C", "8D", "8E", "8F", "90", "91", "92", "93", "94", "95",
        "96", "97", "98", "99", "9A", "9B", "9C", "9D", "9E", "9F", "A0", "A1", "A2", "A3", "A4",
        "A5", "A6", "A7", "A8", "A9", "AA", "AB", "AC", "AD", "AE", "AF", "B0", "B1", "B2", "B3",
        "B4", "B5", "B6", "B7", "B8", "B9", "BA", "BB", "BC", "BD", "BE", "BF", "C0", "C1", "C2",
        "C3", "C4", "C5", "C6", "C7", "C8", "C9", "CA", "CB", "CC", "CD", "CE", "CF", "D0", "D1",
        "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9", "DA", "DB", "DC", "DD", "DE", "DF", "E0",
        "E1", "E2", "E3", "E4", "E5", "E6", "E7", "E8", "E9", "EA", "EB", "EC", "ED", "EE", "EF",
        "F0", "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9", "FA", "FB", "FC", "FD", "FE",
        "FF"
      };

  /**
   * Chars we will ignore when loading a sample HEX string. It allows to copy/paste the specs APDU
   */
  private static final Pattern HEX_IGNORED_CHARS = Pattern.compile(" |h");

  private ByteArrayUtil() {
    throw new IllegalStateException("ByteArray utility class");
  }

  /**
   * Create a byte array from an hexa string. This method allows spaces and "h".
   *
   * @param hex Hexa string
   * @return byte array
   * @since 2.0
   */
  public static byte[] fromHex(String hex) {
    hex = HEX_IGNORED_CHARS.matcher(hex).replaceAll("").toUpperCase();

    if (hex.length() % 2 != 0) {
      throw new IllegalArgumentException("Odd numbered hex array");
    }

    byte[] byteArray = new byte[hex.length() / 2];
    for (int i = 0; i < hex.length(); i += 2) {
      byteArray[i / 2] =
          (byte)
              ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
    }

    return byteArray;
  }

  /**
   * Represents the byte array in a hexadecimal string.
   *
   * @param byteArray byte array to represent to hex
   * @return Hex representation of the byte array
   * @since 2.0
   */
  public static String toHex(byte[] byteArray) {
    if (byteArray == null) {
      return "";
    }
    StringBuilder hexStringBuilder = new StringBuilder();
    for (int i = 0; i < byteArray.length; i++) {
      hexStringBuilder.append(byteToHex[byteArray[i] & 0xFF]);
    }
    return hexStringBuilder.toString();
  }

  /**
   * Convert two bytes (unsigned) from a byte array into an integer.
   *
   * <p>The two bytes are expected to be in the MSB first order (aka network order).
   *
   * <p>Throw an exception if the buffer is null or not long enough to contain 2 bytes.
   *
   * @param bytes byte array
   * @param offset offset from which the 2 bytes are
   * @return the resulting int
   * @throws IllegalArgumentException if the buffer has a bad length
   * @since 2.0
   */
  public static int twoBytesToInt(byte[] bytes, int offset) {
    if (bytes == null || bytes.length < offset + 2 || offset < 0) {
      throw new IllegalArgumentException("Bad data for converting 2-byte integers.");
    }
    return (bytes[offset] & 0xFF) << 8 | (bytes[offset + 1] & 0xFF);
  }

  /**
   * Convert two bytes (signed) from a byte array into an integer.
   *
   * <p>The two bytes are expected to be in the MSB first order (aka network order).
   *
   * <p>The number is also considered as signed. That is, if the MSB (first left bit) is 1, then the
   * number is negative and the conversion is done accordingly with the usual binary arithmetic.
   *
   * <p>Throw an exception if the buffer is null or not long enough to contain 2 bytes.
   *
   * @param bytes byte array
   * @param offset offset from which the 2 bytes are
   * @return the resulting int
   * @throws IllegalArgumentException if the buffer has a bad length
   * @since 2.0
   */
  public static int twoBytesSignedToInt(byte[] bytes, int offset) {
    if (bytes == null || bytes.length < offset + 2 || offset < 0) {
      throw new IllegalArgumentException("Bad data for converting 2-byte integers.");
    }
    if (bytes[offset] >= 0) {
      /* positive number */
      return (bytes[offset] & 0xFF) << 8 | (bytes[offset + 1] & 0xFF);
    } else {
      /* negative number */
      return 0xFFFF0000 | (bytes[offset] & 0xFF) << 8 | (bytes[offset + 1] & 0xFF);
    }
  }

  /**
   * Convert three bytes (unsigned) from a byte array into an integer.
   *
   * <p>The three bytes are expected to be in the MSB first order (aka network order).
   *
   * <p>Throw an exception if the buffer is null or not long enough to contain all 3 bytes.
   *
   * @param bytes byte array
   * @param offset offset from which the 3 bytes are
   * @return the resulting int
   * @throws IllegalArgumentException if the buffer has a bad length
   * @since 2.0
   */
  public static int threeBytesToInt(byte[] bytes, int offset) {
    if (bytes == null || bytes.length < offset + 3 || offset < 0) {
      throw new IllegalArgumentException("Bad data for converting 3-byte integers.");
    }
    return (bytes[offset] & 0xFF) << 16
        | (bytes[offset + 1] & 0xFF) << 8
        | (bytes[offset + 2] & 0xFF);
  }

  /**
   * Convert three bytes (signed) from a byte array into an integer.
   *
   * <p>The three bytes are expected to be in the MSB first order (aka network order).
   *
   * <p>The number is also considered as signed. That is, if the MSB (first left bit) is 1, then the
   * number is negative and the conversion is done accordingly with the usual binary arithmetic.
   *
   * <p>Throw an exception if the buffer is null or not long enough to contain all 3 bytes.
   *
   * @param bytes byte array containing a 3-byte signed number
   * @param offset offset from which the 3 bytes are
   * @return the resulting int
   * @throws IllegalArgumentException if the buffer has a bad length
   * @since 2.0
   */
  public static int threeBytesSignedToInt(byte[] bytes, int offset) {
    if (bytes == null || bytes.length < offset + 3 || offset < 0) {
      throw new IllegalArgumentException("Bad data for converting 3-byte integers.");
    }
    if (bytes[offset] >= 0) {
      /* positive number */
      return (bytes[offset] & 0xFF) << 16
          | (bytes[offset + 1] & 0xFF) << 8
          | (bytes[offset + 2] & 0xFF);
    } else {
      /* negative number */
      return 0xFF000000
          | (bytes[offset] & 0xFF) << 16
          | (bytes[offset + 1] & 0xFF) << 8
          | (bytes[offset + 2] & 0xFF);
    }
  }

  /**
   * Convert four bytes from a byte array into an integer.
   *
   * <p>The four bytes are expected to be in the MSB first order (aka network order).
   *
   * <p>Throw an exception if the buffer is null or not long enough to contain 4 bytes.
   *
   * @param bytes byte array
   * @param offset offset from which the 4 bytes are
   * @return the resulting int
   * @throws IllegalArgumentException if the buffer has a bad length
   * @since 2.0
   */
  public static int fourBytesToInt(byte[] bytes, int offset) {
    if (bytes == null || bytes.length < offset + 4 || offset < 0) {
      throw new IllegalArgumentException("Bad data for converting 4-byte integers.");
    }
    return (bytes[offset] & 0xFF) << 24
        | (bytes[offset + 1] & 0xFF) << 16
        | (bytes[offset + 2] & 0xFF) << 8
        | (bytes[offset + 3] & 0xFF);
  }
}
