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

import java.util.Arrays;

/**
 * Utility class around hex strings.
 *
 * @since 2.1.0
 */
public final class HexUtil {

  /** byte to hex string conversion table */
  private static final String[] byteToHex;

  /** hex digit to nibble conversion table */
  private static final byte[] hexToNibble;

  static {
    byteToHex =
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

    hexToNibble = new byte[256];
    Arrays.fill(hexToNibble, (byte) -1);
    hexToNibble['0'] = 0x0;
    hexToNibble['1'] = 0x1;
    hexToNibble['2'] = 0x2;
    hexToNibble['3'] = 0x3;
    hexToNibble['4'] = 0x4;
    hexToNibble['5'] = 0x5;
    hexToNibble['6'] = 0x6;
    hexToNibble['7'] = 0x7;
    hexToNibble['8'] = 0x8;
    hexToNibble['9'] = 0x9;
    hexToNibble['A'] = 0xA;
    hexToNibble['a'] = 0xA;
    hexToNibble['B'] = 0xB;
    hexToNibble['b'] = 0xB;
    hexToNibble['C'] = 0xC;
    hexToNibble['c'] = 0xC;
    hexToNibble['D'] = 0xD;
    hexToNibble['d'] = 0xD;
    hexToNibble['E'] = 0xE;
    hexToNibble['e'] = 0xE;
    hexToNibble['F'] = 0xF;
    hexToNibble['f'] = 0xF;
  }

  private HexUtil() {}

  /**
   * Checks if a string is formed by an even number of hexadecimal digits.
   *
   * <ul>
   *   <li>{@code "1234AB"} will match.
   *   <li>{@code "1234AB2"}, {@code "12 34AB"} or {@code "x1234AB"} won't match.
   * </ul>
   *
   * @param hex The string to check.
   * @return True if the string matches the expected hexadecimal representation, false otherwise.
   * @since 2.1.0
   */
  public static boolean isValid(String hex) {
    if (hex == null || hex.length() == 0 || hex.length() % 2 != 0) {
      return false;
    }
    for (int i = 0; i < hex.length(); i++) {
      if (hexToNibble[hex.charAt(i)] < 0) {
        return false;
      }
    }
    return true;
  }

  /**
   * Converts a hexadecimal string to a byte array.
   *
   * <p>Caution: the result may be erroneous if the string does not contain only hexadecimal
   * characters.
   *
   * @param hex The hexadecimal string to convert.
   * @return An empty byte array if the input string is null or empty.
   * @throws StringIndexOutOfBoundsException If the input string is made of an odd number of
   *     characters.
   * @since 2.1.0
   */
  public static byte[] toByteArray(String hex) {
    if (hex == null) {
      return new byte[0];
    }
    byte[] tab = new byte[hex.length() / 2];
    for (int i = 0; i < hex.length(); i += 2) {
      tab[i / 2] =
          (byte) ((hexToNibble[hex.charAt(i)] << 4) + (hexToNibble[hex.charAt(i + 1)] & 0xFF));
    }
    return tab;
  }

  /**
   * Converts a hexadecimal string to a "byte".
   *
   * <p>Note: if the hexadecimal string contains more than two characters, then only the last two
   * characters will be taken into account. In this case, please note that the conversion processing
   * will be less performant.
   *
   * <p>Caution: the result may be erroneous if the string does not contain only hexadecimal
   * characters.
   *
   * @param hex The hexadecimal string to convert.
   * @return 0 if the input string is null or empty.
   * @since 2.1.0
   */
  public static byte toByte(String hex) {
    if (hex == null) {
      return 0;
    }
    byte val = 0;
    for (int i = 0; i < hex.length(); i++) {
      val <<= 4;
      val |= (hexToNibble[hex.charAt(i)] & 0xFF);
    }
    return val;
  }

  /**
   * Converts a hexadecimal string to a "short".
   *
   * <p>Note: if the hexadecimal string contains more than four characters, then only the last four
   * characters will be taken into account. In this case, please note that the conversion processing
   * will be less performant.
   *
   * <p>Caution: the result may be erroneous if the string does not contain only hexadecimal
   * characters.
   *
   * @param hex The hexadecimal string to convert.
   * @return 0 if the input string is null or empty.
   * @since 2.1.0
   */
  public static short toShort(String hex) {
    if (hex == null) {
      return 0;
    }
    short val = 0;
    for (int i = 0; i < hex.length(); i++) {
      val <<= 4;
      val |= (hexToNibble[hex.charAt(i)] & 0xFF);
    }
    return val;
  }

  /**
   * Converts a hexadecimal string to an "integer".
   *
   * <p>Note: if the hexadecimal string contains more than eight characters, then only the last
   * eight characters will be taken into account. In this case, please note that the conversion
   * processing will be less performant.
   *
   * <p>Caution: the result may be erroneous if the string does not contain only hexadecimal
   * characters.
   *
   * @param hex The hexadecimal string to convert.
   * @return 0 if the input string is null or empty.
   * @since 2.1.0
   */
  public static int toInt(String hex) {
    if (hex == null) {
      return 0;
    }
    int val = 0;
    for (int i = 0; i < hex.length(); i++) {
      val <<= 4;
      val |= (hexToNibble[hex.charAt(i)] & 0xFF);
    }
    return val;
  }

  /**
   * Converts a hexadecimal string to a "long".
   *
   * <p>Note: if the hexadecimal string contains more than sixteen characters, then only the last
   * sixteen characters will be taken into account. In this case, please note that the conversion
   * processing will be less performant.
   *
   * <p>Caution: the result may be erroneous if the string does not contain only hexadecimal
   * characters.
   *
   * @param hex The hexadecimal string to convert.
   * @return 0 if the input string is null or empty.
   * @since 2.1.0
   */
  public static long toLong(String hex) {
    if (hex == null) {
      return 0;
    }
    long val = 0;
    for (int i = 0; i < hex.length(); i++) {
      val <<= 4;
      val |= (hexToNibble[hex.charAt(i)] & 0xFF);
    }
    return val;
  }

  /**
   * Converts a byte array to a hexadecimal string.
   *
   * @param tab The byte array to convert.
   * @return A string with a size equal to (2 * size of the input array).
   * @since 2.1.0
   */
  public static String toHex(byte[] tab) {
    if (tab == null) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    for (byte b : tab) {
      sb.append(byteToHex[b & 0xFF]);
    }
    return sb.toString();
  }

  /**
   * Converts a "byte" to a hexadecimal string.
   *
   * @param val The byte to convert.
   * @return A string containing 2 characters.
   * @since 2.1.0
   */
  public static String toHex(byte val) {
    return byteToHex[val & 0xFF];
  }

  /**
   * Converts a "short" to a hexadecimal string.
   *
   * <p>Note: the returned string has an even length and is left truncated if necessary to keep only
   * the significant characters.
   *
   * @param val The short to convert.
   * @return A string containing 2 or 4 characters.
   * @since 2.1.0
   */
  public static String toHex(short val) {
    if ((val & 0xFF00) == 0) {
      return byteToHex[val & 0xFF];
    }
    return byteToHex[val >> 8 & 0xFF] + byteToHex[val & 0xFF];
  }

  /**
   * Converts an "integer" to a hexadecimal string.
   *
   * <p>Note: the returned string has an even length and is left truncated if necessary to keep only
   * the significant characters.
   *
   * @param val The integer to convert.
   * @return A string containing 2, 4, 6 or 8 characters.
   * @since 2.1.0
   */
  public static String toHex(int val) {
    if ((val & 0xFFFFFF00) == 0) {
      return byteToHex[val & 0xFF];
    } else if ((val & 0xFFFF0000) == 0) {
      return byteToHex[val >> 8 & 0xFF] + byteToHex[val & 0xFF];
    } else if ((val & 0xFF000000) == 0) {
      return byteToHex[val >> 16 & 0xFF] + byteToHex[val >> 8 & 0xFF] + byteToHex[val & 0xFF];
    }
    return byteToHex[val >> 24 & 0xFF]
        + byteToHex[val >> 16 & 0xFF]
        + byteToHex[val >> 8 & 0xFF]
        + byteToHex[val & 0xFF];
  }

  /**
   * Converts a "long" to a hexadecimal string.
   *
   * <p>Note: the returned string has an even length and is left truncated if necessary to keep only
   * the significant characters.
   *
   * @param val The long to convert.
   * @return A string containing 2, 4, 6, 8, 10, 12, 14 or 16 characters.
   * @since 2.1.0
   */
  public static String toHex(long val) {
    if ((val & 0xFFFFFFFFFFFFFF00L) == 0) {
      return byteToHex[(int) (val & 0xFF)];
    } else if ((val & 0xFFFFFFFFFFFF0000L) == 0) {
      return byteToHex[(int) (val >> 8 & 0xFF)] + byteToHex[(int) (val & 0xFF)];
    } else if ((val & 0xFFFFFFFFFF000000L) == 0) {
      return byteToHex[(int) (val >> 16 & 0xFF)]
          + byteToHex[(int) (val >> 8 & 0xFF)]
          + byteToHex[(int) (val & 0xFF)];
    } else if ((val & 0xFFFFFFFF00000000L) == 0) {
      return byteToHex[(int) (val >> 24 & 0xFF)]
          + byteToHex[(int) (val >> 16 & 0xFF)]
          + byteToHex[(int) (val >> 8 & 0xFF)]
          + byteToHex[(int) (val & 0xFF)];
    } else if ((val & 0xFFFFFF0000000000L) == 0) {
      return byteToHex[(int) (val >> 32 & 0xFF)]
          + byteToHex[(int) (val >> 24 & 0xFF)]
          + byteToHex[(int) (val >> 16 & 0xFF)]
          + byteToHex[(int) (val >> 8 & 0xFF)]
          + byteToHex[(int) (val & 0xFF)];
    } else if ((val & 0xFFFF000000000000L) == 0) {
      return byteToHex[(int) (val >> 40 & 0xFF)]
          + byteToHex[(int) (val >> 32 & 0xFF)]
          + byteToHex[(int) (val >> 24 & 0xFF)]
          + byteToHex[(int) (val >> 16 & 0xFF)]
          + byteToHex[(int) (val >> 8 & 0xFF)]
          + byteToHex[(int) (val & 0xFF)];
    } else if ((val & 0xFF00000000000000L) == 0) {
      return byteToHex[(int) (val >> 48 & 0xFF)]
          + byteToHex[(int) (val >> 40 & 0xFF)]
          + byteToHex[(int) (val >> 32 & 0xFF)]
          + byteToHex[(int) (val >> 24 & 0xFF)]
          + byteToHex[(int) (val >> 16 & 0xFF)]
          + byteToHex[(int) (val >> 8 & 0xFF)]
          + byteToHex[(int) (val & 0xFF)];
    }
    return byteToHex[(int) (val >> 56 & 0xFF)]
        + byteToHex[(int) (val >> 48 & 0xFF)]
        + byteToHex[(int) (val >> 40 & 0xFF)]
        + byteToHex[(int) (val >> 32 & 0xFF)]
        + byteToHex[(int) (val >> 24 & 0xFF)]
        + byteToHex[(int) (val >> 16 & 0xFF)]
        + byteToHex[(int) (val >> 8 & 0xFF)]
        + byteToHex[(int) (val & 0xFF)];
  }
}
