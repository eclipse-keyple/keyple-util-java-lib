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

import java.util.Arrays;

/**
 * Utility class around byte arrays
 *
 * @since 2.0.0
 */
public final class ByteArrayUtil {

  /** byte to hex string conversion table */
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

  /** hex digit to nibble conversion */
  private static final byte[] hexToNibble = new byte[256];

  static {
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

  private ByteArrayUtil() {}

  /**
   * Checks if the provided string is formed by an even number of hexadecimal digits. <br>
   *
   * <ul>
   *   <li>{@code "1234AB"} will match.
   *   <li>{@code "1234AB2"}, {@code "12 34AB"} or {@code "x1234AB"} won't match.
   * </ul>
   *
   * @param hex A string.
   * @return true if the string matches the expected hexadecimal representation, false otherwise.
   */
  public static boolean isValidHexString(String hex) {
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
   * Normalizes the input hex string by padding on the left by a zero if necessary.
   *
   * @param hex The hex string to normalize.
   * @return A not null string.
   * @throws NullPointerException If the input string is null.
   * @since 2.0.0
   */
  public static String normalizeHexString(String hex) {
    if (hex.length() % 2 != 0) {
      return "0" + hex;
    }
    return hex;
  }

  /**
   * Converts the provided hexadecimal string into a byte array.
   *
   * <p>No checks are performed on the input string, except for nullity, zero length and length
   * parity.
   *
   * @param hex The hexadecimal string to convert.
   * @return A not empty byte array.
   * @throws IllegalArgumentException If the provided string is null, empty or made of an odd number
   *     of characters.
   * @see #isValidHexString(String)
   * @since 2.0.0
   */
  public static byte[] fromHex(String hex) {
    Assert.getInstance().notEmpty(hex, "hex").isEqual(hex.length() % 2, 0, "hex");
    byte[] tab = new byte[hex.length() / 2];
    for (int i = 0; i < hex.length(); i += 2) {
      tab[i / 2] =
          (byte) ((hexToNibble[hex.charAt(i)] << 4) + (hexToNibble[hex.charAt(i + 1)] & 0xFF));
    }
    return tab;
  }

  /**
   * Converts the provided hexadecimal string into a byte.
   *
   * <p>No checks are performed on the input string, except for nullity, zero length and length
   * parity.
   *
   * @param hex The hexadecimal string to convert.
   * @return The value.
   * @throws IllegalArgumentException If the provided string is null, empty or made of an odd number
   *     of characters.
   * @see #isValidHexString(String)
   * @since 2.1.0
   */
  public static byte hexToByte(String hex) {
    Assert.getInstance().notEmpty(hex, "hex").isEqual(hex.length(), 2, "hex");
    return (byte) ((hexToNibble[hex.charAt(0)] << 4) + (hexToNibble[hex.charAt(1)] & 0xFF));
  }

  /**
   * Converts the provided hexadecimal string into a short.
   *
   * <p>No checks are performed on the input string, except for nullity, zero length and length
   * parity.
   *
   * @param hex The hexadecimal string to convert.
   * @return The value.
   * @throws IllegalArgumentException If the provided string is null, empty or made of an odd number
   *     of characters.
   * @see #isValidHexString(String)
   * @since 2.1.0
   */
  public static short hexToShort(String hex) {
    Assert.getInstance()
        .notEmpty(hex, "hex")
        .isEqual(hex.length() % 2, 0, "hex")
        .isTrue(hex.length() <= 4, "hex");
    short val = 0;
    for (int i = 0; i < hex.length(); i++) {
      val <<= 4;
      val |= (hexToNibble[hex.charAt(i)] & 0xFF);
    }
    return val;
  }

  /**
   * Converts the provided hexadecimal string into an integer.
   *
   * <p>No checks are performed on the input string, except for nullity, zero length and length
   * parity.
   *
   * @param hex The hexadecimal string to convert.
   * @return The value.
   * @throws IllegalArgumentException If the provided string is null, empty or made of an odd number
   *     of characters.
   * @see #isValidHexString(String)
   * @since 2.1.0
   */
  public static int hexToInt(String hex) {
    Assert.getInstance()
        .notEmpty(hex, "hex")
        .isEqual(hex.length() % 2, 0, "hex")
        .isTrue(hex.length() <= 8, "hex");
    int val = 0;
    for (int i = 0; i < hex.length(); i++) {
      val <<= 4;
      val |= (hexToNibble[hex.charAt(i)] & 0xFF);
    }
    return val;
  }

  /**
   * Converts the provided hexadecimal string into a long.
   *
   * <p>No checks are performed on the input string, except for nullity, zero length and length
   * parity.
   *
   * @param hex The hexadecimal string to convert.
   * @return The value.
   * @throws IllegalArgumentException If the provided string is null, empty or made of an odd number
   *     of characters.
   * @see #isValidHexString(String)
   * @since 2.1.0
   */
  public static long hexToLong(String hex) {
    Assert.getInstance()
        .notEmpty(hex, "hex")
        .isEqual(hex.length() % 2, 0, "hex")
        .isTrue(hex.length() <= 16, "hex");
    long val = 0;
    for (int i = 0; i < hex.length(); i++) {
      val <<= 4;
      val |= (hexToNibble[hex.charAt(i)] & 0xFF);
    }
    return val;
  }

  /**
   * Converts the provided byte array into a hexadecimal string.
   *
   * @param tab The byte array to convert.
   * @return An empty string if the byte array is null or empty.
   * @since 2.0.0
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
   * Converts the provided byte into a hexadecimal string.
   *
   * @param val The value to convert.
   * @return A not empty string.
   * @since 2.1.0
   */
  public static String toHex(byte val) {
    return byteToHex[val & 0xFF];
  }

  /**
   * Converts the provided short into a hexadecimal string.
   *
   * @param val The value to convert.
   * @return A not empty string.
   * @since 2.1.0
   */
  public static String toHex(short val) {
    if ((val & 0xFF00) == 0) {
      return byteToHex[val & 0xFF];
    }
    return byteToHex[val >> 8 & 0xFF] + byteToHex[val & 0xFF];
  }

  /**
   * Converts the provided integer into a hexadecimal string.
   *
   * @param val The value to convert.
   * @return A not empty string.
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
   * Converts the provided long into a hexadecimal string.
   *
   * @param val The value to convert.
   * @return A not empty string.
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

  /** (private) */
  private static void checkBytesToIntConversionParams(int size, byte[] bytes, int offset) {
    Assert.getInstance()
        .notNull(bytes, "bytes")
        .greaterOrEqual(bytes.length, offset + size, "length")
        .greaterOrEqual(offset, 0, "offset");
  }

  /**
   * Converts 2 bytes located at the offset provided in the byte array into an <b>unsigned</b>
   * integer.
   *
   * <p>The 2 bytes are assumed to be in the of the most significant byte first order (aka 'network
   * order' or 'big-endian' or 'MSB').
   *
   * @param bytes A byte array.
   * @param offset The position of the 2 bytes in the array.
   * @return A positive int.
   * @throws IllegalArgumentException If the buffer has a bad length or the offset is negative.
   * @since 2.0.0
   */
  public static int twoBytesToInt(byte[] bytes, int offset) {
    checkBytesToIntConversionParams(2, bytes, offset);
    return (bytes[offset] & 0xFF) << 8 | (bytes[offset + 1] & 0xFF);
  }

  /**
   * Converts 2 bytes located at the offset provided in the byte array into an <b>signed</b>
   * integer.
   *
   * <p>The 2 bytes are assumed to be in the of the most significant byte first order (aka 'network
   * order' or 'big-endian' or 'MSB').
   *
   * <p>The number is also considered as signed. That is, if the MSB (first left bit) is 1, then the
   * number is negative and the conversion is done accordingly with the usual binary arithmetic.
   *
   * @param bytes A byte array.
   * @param offset The position of the 2 bytes in the array.
   * @return A negative or positive int.
   * @throws IllegalArgumentException If the buffer has a bad length or the offset is negative.
   * @since 2.0.0
   */
  public static int twoBytesSignedToInt(byte[] bytes, int offset) {
    checkBytesToIntConversionParams(2, bytes, offset);
    if (bytes[offset] >= 0) {
      /* positive number */
      return (bytes[offset] & 0xFF) << 8 | (bytes[offset + 1] & 0xFF);
    } else {
      /* negative number */
      return 0xFFFF0000 | (bytes[offset] & 0xFF) << 8 | (bytes[offset + 1] & 0xFF);
    }
  }

  /**
   * Converts 3 bytes located at the offset provided in the byte array into an <b>unsigned</b>
   * integer.
   *
   * <p>The 3 bytes are assumed to be in the of the most significant byte first order (aka 'network
   * order' or 'big-endian' or 'MSB').
   *
   * @param bytes A byte array.
   * @param offset The position of the 3 bytes in the array.
   * @return A positive int.
   * @throws IllegalArgumentException if the buffer has a bad length
   * @since 2.0.0
   */
  public static int threeBytesToInt(byte[] bytes, int offset) {
    checkBytesToIntConversionParams(3, bytes, offset);
    return (bytes[offset] & 0xFF) << 16
        | (bytes[offset + 1] & 0xFF) << 8
        | (bytes[offset + 2] & 0xFF);
  }

  /**
   * Converts 3 bytes located at the offset provided in the byte array into an <b>signed</b>
   * integer.
   *
   * <p>The 3 bytes are assumed to be in the of the most significant byte first order (aka 'network
   * order' or 'big-endian' or 'MSB').
   *
   * <p>The number is also considered as signed. That is, if the MSB (first left bit) is 1, then the
   * number is negative and the conversion is done accordingly with the usual binary arithmetic.
   *
   * @param bytes A byte array.
   * @param offset The position of the 3 bytes in the array.
   * @return A positive int.
   * @throws IllegalArgumentException if the buffer has a bad length
   * @since 2.0.0
   */
  public static int threeBytesSignedToInt(byte[] bytes, int offset) {
    checkBytesToIntConversionParams(3, bytes, offset);
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
   * Converts 4 bytes located at the offset provided in the byte array into an <b>unsigned</b>
   * integer.
   *
   * <p>The 4 bytes are assumed to be in the of the most significant byte first order (aka 'network
   * order' or 'big-endian' or 'MSB').
   *
   * @param bytes A byte array.
   * @param offset The position of the 4 bytes in the array.
   * @return A positive int.
   * @throws IllegalArgumentException if the buffer has a bad length
   * @since 2.0.0
   */
  public static int fourBytesToInt(byte[] bytes, int offset) {
    checkBytesToIntConversionParams(4, bytes, offset);
    return (bytes[offset] & 0xFF) << 24
        | (bytes[offset + 1] & 0xFF) << 16
        | (bytes[offset + 2] & 0xFF) << 8
        | (bytes[offset + 3] & 0xFF);
  }

  /**
   * Extracts "nbBytes" bytes from the "bitOffset" index in bits of the provided byte array.
   *
   * @param src The byte array.
   * @param bitOffset The offset (in bits).
   * @param nbBytes The number of bytes to extract.
   * @return A not null byte array.
   * @since 2.1.0
   */
  public static byte[] extract(byte[] src, int bitOffset, int nbBytes) {
    final byte[] dest = new byte[nbBytes];
    final int byteOffset = bitOffset / 8;
    bitOffset %= 8;
    if (bitOffset == 0) {
      System.arraycopy(src, byteOffset, dest, 0, nbBytes);
    } else {
      final int rightShift = 8 - bitOffset;
      for (int i = 0, j = byteOffset; j < byteOffset + nbBytes; i++, j++) {
        dest[i] = (byte) ((src[j] << bitOffset) | ((src[j + 1] & 0xFF) >> rightShift));
      }
    }
    return dest;
  }
}
