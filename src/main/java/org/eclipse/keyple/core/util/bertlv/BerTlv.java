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
package org.eclipse.keyple.core.util.bertlv;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper class to decode BER-TLV encoded data.
 *
 * <p>The TLV decoding has the following limitations:
 *
 * <ul>
 *   <li>The size of the tags ID should not be larger than 3.
 *   <li>The length fields must not exceed 3 bytes.
 * </ul>
 *
 * @since 2.0
 */
public class BerTlv {

  /** (private) */
  private BerTlv() {}

  /**
   * Parse the provided TLV structure and place all or only primitive tags found in a map. The key
   * is an integer representing the tag Id (e.g. 0x84 for the DF name tag), the value is the tag
   * value as an array of bytes.
   *
   * @param tlvStructure The input TLV structure.
   * @param primitiveOnly True if only primitives tags are to be placed in the map.
   * @return A not null map.
   * @throws IllegalArgumentException If the parsing of the provided structure failed.
   * @since 2.0
   */
  public static Map<Integer, byte[]> parseSimple(byte[] tlvStructure, boolean primitiveOnly) {
    try {
      return parseBuffer(tlvStructure, primitiveOnly);
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalArgumentException("Invalid TLV structure.");
    }
  }

  /**
   * (private)<br>
   * Parse the provided TLV structure from the provided offset and place all or only primitive tags
   * found in a map.
   *
   * @param tlvStructure The input TLV structure.
   * @param primitiveOnly True if only primitives tags are to be placed in the map.
   * @return A not null map.
   */
  private static Map<Integer, byte[]> parseBuffer(byte[] tlvStructure, boolean primitiveOnly) {
    int offset = 0;
    Map<Integer, byte[]> tlvs = new HashMap<Integer, byte[]>();
    do {
      int tagSize = getTagSize(tlvStructure, offset);
      byte[] tagBytes = Arrays.copyOfRange(tlvStructure, offset, offset + tagSize);
      int tag = getTag(tlvStructure, offset, tagSize);
      int lengthSize = getLengthSize(tlvStructure, offset + tagSize);
      int valueSize = getLength(tlvStructure, offset + tagSize, lengthSize);
      byte[] value =
          Arrays.copyOfRange(
              tlvStructure,
              offset + tagSize + lengthSize,
              offset + tagSize + lengthSize + valueSize);
      offset += tagSize + lengthSize + valueSize;
      if ((tagBytes[0] & 0x20) != 0) {
        // tag is constructed
        if (!primitiveOnly) {
          tlvs.put(tag, value);
        }
        tlvs.putAll(parseSimple(value, primitiveOnly));
      } else {
        // tag is primitive
        tlvs.put(tag, value);
      }
    } while (offset < tlvStructure.length);
    return tlvs;
  }

  /**
   * (private)<br>
   * Gets the tag field size.
   *
   * @param tlvStructure The input TLV structure.
   * @param offset The starting offset in the structure.
   * @return An int.
   * @throws IllegalArgumentException If the tag field is invalid.
   * @throws IndexOutOfBoundsException If offset is out of range for the provided tlvStructure.
   */
  private static int getTagSize(byte[] tlvStructure, int offset) {
    if ((tlvStructure[offset] & 0x1F) == 0x1F) {
      if ((tlvStructure[offset + 1] & 0x80) == 0) {
        return 2;
      } else {
        if ((tlvStructure[offset + 2] & 0x80) != 0) {
          throw new IllegalArgumentException("Invalid tag.");
        }
      }
      return 3;
    } else {
      return 1;
    }
  }

  /**
   * (private)<br>
   * Gets, as an integer, the tag of the provided size present at the designated location.
   *
   * @param tlvStructure The input TLV structure.
   * @param offset The starting offset in the structure.
   * @param size The tag size.
   * @return An int representing the tag value.
   * @throws IllegalArgumentException If the size is wrong.
   * @throws IndexOutOfBoundsException If offset is out of range for the provided tlvStructure.
   */
  private static int getTag(byte[] tlvStructure, int offset, int size) {
    switch (size) {
      case 1:
        return tlvStructure[offset] & 0xFF;
      case 2:
        return ((tlvStructure[offset] & 0xFF) << 8) + (tlvStructure[offset + 1] & 0xFF);
      case 3:
        return ((tlvStructure[offset] & 0xFF) << 16)
            + ((tlvStructure[offset + 1] & 0xFF) << 8)
            + (tlvStructure[offset + 2] & 0xFF);
      default:
        throw new IllegalArgumentException("Bad tag size.");
    }
  }

  /**
   * (private)<br>
   * Gets the length field size.
   *
   * @param tlvStructure The input TLV structure.
   * @param offset The starting offset in the structure.
   * @return An int between 1 and 3.
   * @throws IllegalArgumentException If the length field is invalid.
   * @throws IndexOutOfBoundsException If offset is out of range for the provided tlvStructure.
   */
  private static int getLengthSize(byte[] tlvStructure, int offset) {
    int firstByteLength = tlvStructure[offset] & 0xff;
    switch (firstByteLength) {
      case 0x82:
        return 3;
      case 0x81:
        return 2;
      default:
        if (firstByteLength >= 0x80) {
          throw new IllegalArgumentException("Invalid length.");
        }
        return 1;
    }
  }

  /**
   * (private)<br>
   * Gets, as an integer, the length of the provided size present at the designated location.
   *
   * @param tlvStructure The input TLV structure.
   * @param offset The starting offset in the structure.
   * @param size The tag size.
   * @return An int representing the length value.
   * @throws IllegalArgumentException If the size is wrong.
   * @throws IndexOutOfBoundsException If offset is out of range for the provided tlvStructure.
   */
  private static int getLength(byte[] tlvStructure, int offset, int size) {
    switch (size) {
      case 1:
        return tlvStructure[offset] & 0x7F;
      case 2:
        return tlvStructure[offset + 1] & 0xFF;
      case 3:
        return ((tlvStructure[offset + 1] & 0xFF) << 8) + (tlvStructure[offset + 2] & 0xFF);
      default:
        throw new IllegalArgumentException("Bad length size.");
    }
  }
}
