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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class to decode BER-TLV encoded data.
 *
 * <p>This class offers a tradeoff between complexity and efficiency adapted to the TLV structures
 * encountered in smart card data, it has the following limitations:
 *
 * <ul>
 *   <li>The tag ID fields must not exceed 3 bytes.
 *   <li>The length fields must not exceed 3 bytes.
 *   <li>Tags present several times in the same TLV structure require special attention (see {@link
 *       #parseSimple(byte[], boolean)}).
 * </ul>
 *
 * @since 2.0.0
 */
public class BerTlvUtil {

  /** (private) */
  private BerTlvUtil() {}

  /**
   * Parse the provided TLV structure and place all or only primitive tags found in a map. The key
   * is an integer representing the tag ID (e.g. 0x84 for the DF name tag), the value is the tag
   * value as an array of bytes.
   *
   * <p><b>Note:</b>This method of extracting tags is deliberately simplified.<br>
   * If the provided TLV structure contains several identical tags then only one will be reported in
   * the returned map.<br>
   * To overcome this limitation it is recommended to re-parse the constructed tags known to contain
   * other tags.
   *
   * @param tlvStructure The input TLV structure.
   * @param primitiveOnly True if only primitives tags are to be placed in the map.
   * @return A not null map.
   * @throws IllegalArgumentException If the parsing of the provided structure failed.
   * @since 2.0.0
   */
  public static Map<Integer, byte[]> parseSimple(byte[] tlvStructure, boolean primitiveOnly) {
    try {
      return parseBufferSimple(tlvStructure, primitiveOnly);
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalArgumentException("Invalid TLV structure.");
    }
  }

  /**
   * Parse the provided TLV structure and place all or only primitive tags found in a map. The key
   * is an integer representing the tag ID (e.g. 0x84 for the DF name tag), the value is the list of
   * tag values as a list of arrays of bytes.
   *
   * @param tlvStructure The input TLV structure.
   * @param primitiveOnly True if only primitives tags are to be placed in the map.
   * @return A not null map.
   * @throws IllegalArgumentException If the parsing of the provided structure failed.
   * @since 2.1.0
   */
  public static Map<Integer, List<byte[]>> parse(byte[] tlvStructure, boolean primitiveOnly) {
    try {
      return parseBuffer(tlvStructure, primitiveOnly);
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalArgumentException("Invalid TLV structure.");
    }
  }

  /**
   * Indicates if the provided tag ID corresponds to a constructed tag.
   *
   * @param tagId A positive int less than FFFFFFh.
   * @return True if the tag is constructed.
   * @throws IllegalArgumentException If the tag ID is out of range.
   * @since 2.0.0
   */
  public static boolean isConstructed(int tagId) {
    if (tagId < 0 || tagId > 0xFFFFFF) {
      throw new IllegalArgumentException("Tag Id out of range.");
    }
    if (tagId <= 0xFF) {
      return (tagId & 0x20) != 0;
    }
    if (tagId <= 0xFFFF) {
      return (tagId & 0x2000) != 0;
    }
    return (tagId & 0x200000) != 0;
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
  private static Map<Integer, byte[]> parseBufferSimple(
      byte[] tlvStructure, boolean primitiveOnly) {

    Map<Integer, byte[]> tlvs = new HashMap<Integer, byte[]>();
    int offset = 0;
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
   * Parse the provided TLV structure from the provided offset and place all or only primitive tags
   * found in a map.
   *
   * @param tlvStructure The input TLV structure.
   * @param primitiveOnly True if only primitives tags are to be placed in the map.
   * @return A not null map.
   */
  private static Map<Integer, List<byte[]>> parseBuffer(
      byte[] tlvStructure, boolean primitiveOnly) {

    Map<Integer, List<byte[]>> tlvs = new HashMap<Integer, List<byte[]>>();
    int offset = 0;
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
          List<byte[]> values = getOrInitTagValues(tlvs, tag);
          values.add(value);
        }
        Map<Integer, List<byte[]>> tlvs2 = parse(value, primitiveOnly);
        for (Map.Entry<Integer, List<byte[]>> entry : tlvs2.entrySet()) {
          List<byte[]> values = getOrInitTagValues(tlvs, entry.getKey());
          values.addAll(entry.getValue());
        }
      } else {
        // tag is primitive
        List<byte[]> values = getOrInitTagValues(tlvs, tag);
        values.add(value);
      }
    } while (offset < tlvStructure.length);
    return tlvs;
  }

  /**
   * (private)<br>
   * Gets a reference to the values of the existing tag in the map, or put the new tag in the map
   * with an empty list of values.
   *
   * @param tlvs The map.
   * @param tag The TAG.
   * @return A not null reference to the associated list of values.
   */
  private static List<byte[]> getOrInitTagValues(Map<Integer, List<byte[]>> tlvs, int tag) {
    List<byte[]> values = tlvs.get(tag);
    if (values == null) {
      values = new ArrayList<byte[]>();
      tlvs.put(tag, values);
    }
    return values;
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
