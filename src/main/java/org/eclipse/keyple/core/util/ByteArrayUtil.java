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

/**
 * Utility class around byte arrays.
 *
 * @since 2.0.0
 */
public final class ByteArrayUtil {

  private ByteArrayUtil() {}

  /**
   * Extracts "nbBytes" bytes from the "bitOffset" index (<b>in bits</b>) from a byte array.
   *
   * @param src The source byte array.
   * @param bitOffset The offset (<b>in bits</b>).
   * @param nbBytes The number of bytes to extract.
   * @return A not null byte array.
   * @throws NullPointerException If "src" is null.
   * @throws ArrayIndexOutOfBoundsException If "bitOffset" or "nbBytes" is out of range.
   * @throws NegativeArraySizeException If "nbBytes" is negative.
   * @since 2.1.0
   */
  public static byte[] extractBytes(byte[] src, int bitOffset, int nbBytes) {
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

  /**
   * Extracts the least significant bytes (LSB) of a number into a byte array.
   *
   * <p>Caution: the result may be erroneous if the number of bytes to extract is greater than the
   * number of bytes associated to the input number (e.g. 2 bytes max for a "short", 4 bytes max for
   * an "integer" or 8 bytes max for a "long").
   *
   * @param src The source.
   * @param nbBytes The number of bytes to extract.
   * @return An empty array if "nbBytes" is equal to 0.
   * @throws NegativeArraySizeException If "nbBytes" is negative.
   * @since 2.3.0
   */
  public static byte[] extractBytes(long src, int nbBytes) {
    byte[] data = new byte[nbBytes];
    int shift = 0;
    int i = nbBytes - 1;
    while (i >= 0) {
      data[i] = (byte) ((src >> shift) & 0xFF);
      shift += 8;
      i--;
    }
    return data;
  }

  /**
   * Extracts a 2-byte "short" located at a specific "offset" in a source byte array.
   *
   * @param src The source byte array.
   * @param offset The offset (in bytes).
   * @return A short.
   * @throws NullPointerException If "src" is null.
   * @throws ArrayIndexOutOfBoundsException If "offset" is not in range [0..(src.length-2)]
   * @since 2.3.0
   */
  public static short extractShort(byte[] src, int offset) {
    return (short) ((short) ((src[offset] & 0xFF) << 8) | (src[offset + 1] & 0xFF));
  }

  /**
   * Converts "nbBytes" bytes located at the "offset" provided in a source byte array into an
   * "integer".
   *
   * <p>Caution: the result may be erroneous if "nbBytes" is not in range [1..4].
   *
   * @param src The source byte array.
   * @param offset The offset (in bytes).
   * @param nbBytes The number of bytes to extract.
   * @param isSigned True if the resulting integer is "signed" (relevant only if "nbBytes" is in
   *     range [1..3]).
   * @return An int.
   * @throws NullPointerException If "src" is null.
   * @throws ArrayIndexOutOfBoundsException If "offset" is not in range [0..(src.length-nbBytes)]
   * @since 2.1.0
   */
  public static int extractInt(byte[] src, int offset, int nbBytes, boolean isSigned) {
    int val = 0;
    if (isSigned) {
      val |= (src[offset++] << (8 * (--nbBytes)));
    } else {
      val |= ((src[offset++] & 0xFF) << (8 * (--nbBytes)));
    }
    while (nbBytes > 0) {
      val |= ((src[offset++] & 0xFF) << (8 * (--nbBytes)));
    }
    return val;
  }

  /**
   * Converts "nbBytes" bytes located at the "offset" provided in a source byte array into a "long".
   *
   * <p>Caution: the result may be erroneous if "nbBytes" is not in range [1..8].
   *
   * @param src The source byte array.
   * @param offset The offset (in bytes).
   * @param nbBytes The number of bytes to extract.
   * @param isSigned True if the resulting integer is "signed" (relevant only if "nbBytes" is in
   *     range [1..7]).
   * @return A long.
   * @throws NullPointerException If "src" is null.
   * @throws ArrayIndexOutOfBoundsException If "offset" is not in range [0..(src.length-nbBytes)]
   * @since 2.3.0
   */
  public static long extractLong(byte[] src, int offset, int nbBytes, boolean isSigned) {
    long val = 0L;
    if (isSigned) {
      val |= ((long) src[offset++] << (8 * (--nbBytes)));
    } else {
      val |= ((long) (src[offset++] & 0xFF) << (8 * (--nbBytes)));
    }
    while (nbBytes > 0) {
      val |= ((long) (src[offset++] & 0xFF) << (8 * (--nbBytes)));
    }
    return val;
  }

  /**
   * Copy the least significant bytes (LSB) of a number (byte, short, integer or long) into a byte
   * array at a specific offset.
   *
   * @param src The number.
   * @param dest The target byte array.
   * @param offset The offset (in bytes).
   * @param nbBytes The number of bytes to copy.
   * @throws NullPointerException If "dest" is null.
   * @throws NegativeArraySizeException If "nbBytes" is negative.
   * @throws ArrayIndexOutOfBoundsException If "offset" is not in range [0..(dest.length-nbBytes)]
   * @since 2.3.0
   */
  public static void copyBytes(long src, byte[] dest, int offset, int nbBytes) {
    System.arraycopy(extractBytes(src, nbBytes), 0, dest, offset, nbBytes);
  }

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
   * @since 2.0.0
   * @deprecated Use {@link HexUtil#isValid(String)} method instead.
   */
  @Deprecated
  public static boolean isValidHexString(String hex) {
    return HexUtil.isValid(hex);
  }

  /**
   * Normalizes the input hex string by padding on the left by a zero if necessary.
   *
   * @param hex The hex string to normalize.
   * @return A not null string.
   * @throws NullPointerException If the input string is null.
   * @since 2.0.0
   * @deprecated To be removed.
   */
  @Deprecated
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
   * @deprecated Use {@link HexUtil#toByteArray(String)} method instead.
   */
  @Deprecated
  public static byte[] fromHex(String hex) {
    Assert.getInstance().notEmpty(hex, "hex").isEqual(hex.length() % 2, 0, "hex size");
    return HexUtil.toByteArray(hex);
  }

  /**
   * Converts the provided byte array into a hexadecimal string.
   *
   * @param src The byte array to convert.
   * @return An empty string if the byte array is null or empty.
   * @since 2.0.0
   * @deprecated Use {@link HexUtil#toHex(byte[])} method instead.
   */
  @Deprecated
  public static String toHex(byte[] src) {
    if (src == null) {
      return "";
    }
    return HexUtil.toHex(src);
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
   * @throws NullPointerException If "bytes" is null.
   * @throws ArrayIndexOutOfBoundsException If "offset" is not in range [0..(bytes.length-2)]
   * @since 2.0.0
   * @deprecated Use {@link #extractInt(byte[], int, int, boolean)} method instead with "nbBytes =
   *     2" and "isSigned = false".
   */
  @Deprecated
  public static int twoBytesToInt(byte[] bytes, int offset) {
    return extractInt(bytes, offset, 2, false);
  }

  /**
   * Converts 2 bytes located at the offset provided in the byte array into a <b>signed</b> integer.
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
   * @throws NullPointerException If "bytes" is null.
   * @throws ArrayIndexOutOfBoundsException If "offset" is not in range [0..(bytes.length-2)]
   * @since 2.0.0
   * @deprecated Use {@link #extractInt(byte[], int, int, boolean)} method instead with "nbBytes =
   *     2" and "isSigned = true".
   */
  @Deprecated
  public static int twoBytesSignedToInt(byte[] bytes, int offset) {
    return extractInt(bytes, offset, 2, true);
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
   * @throws NullPointerException If "bytes" is null.
   * @throws ArrayIndexOutOfBoundsException If "offset" is not in range [0..(bytes.length-3)]
   * @since 2.0.0
   * @deprecated Use {@link #extractInt(byte[], int, int, boolean)} method instead with "nbBytes =
   *     3" and "isSigned = false".
   */
  @Deprecated
  public static int threeBytesToInt(byte[] bytes, int offset) {
    return extractInt(bytes, offset, 3, false);
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
   * @throws NullPointerException If "bytes" is null.
   * @throws ArrayIndexOutOfBoundsException If "offset" is not in range [0..(bytes.length-3)]
   * @since 2.0.0
   * @deprecated Use {@link #extractInt(byte[], int, int, boolean)} method instead with "nbBytes =
   *     3" and "isSigned = true".
   */
  @Deprecated
  public static int threeBytesSignedToInt(byte[] bytes, int offset) {
    return extractInt(bytes, offset, 3, true);
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
   * @throws NullPointerException If "bytes" is null.
   * @throws ArrayIndexOutOfBoundsException If "offset" is not in range [0..(bytes.length-4)]
   * @since 2.0.0
   * @deprecated Use {@link #extractInt(byte[], int, int, boolean)} method instead with "nbBytes =
   *     2" and "isSigned = true|false".
   */
  @Deprecated
  public static int fourBytesToInt(byte[] bytes, int offset) {
    return extractInt(bytes, offset, 4, true);
  }
}
