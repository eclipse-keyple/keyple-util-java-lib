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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

import java.util.Map;
import org.eclipse.keyple.core.util.ByteArrayUtil;
import org.junit.Test;

public class BerTlvTest {

  private final String TLV1 =
      "6F238409315449432E49434131A516BF0C13C708000000001122334453070A3C2005141001";
  private final String TLV2 =
      "6F23A516BF0C1353070A3C2005141001C70800000000112233448409315449432E49434131";

  @Test
  public void parse_whenStructureIsValidAndPrimitiveOnlyIsFalse_shouldProvideAllTags() {
    Map<Integer, byte[]> tlvs = BerTlv.parseSimple(ByteArrayUtil.fromHex(TLV1), false);
    assertThat(tlvs)
        .containsOnly(
            entry(
                0x6F,
                ByteArrayUtil.fromHex(
                    "8409315449432E49434131A516BF0C13C708000000001122334453070A3C2005141001")),
            entry(0x84, ByteArrayUtil.fromHex("315449432E49434131")),
            entry(0xA5, ByteArrayUtil.fromHex("BF0C13C708000000001122334453070A3C2005141001")),
            entry(0xBF0C, ByteArrayUtil.fromHex("C708000000001122334453070A3C2005141001")),
            entry(0x53, ByteArrayUtil.fromHex("0A3C2005141001")),
            entry(0xC7, ByteArrayUtil.fromHex("0000000011223344")));
  }

  @Test
  public void parse_whenStructureIsValidAndPrimitiveOnlyIsTrue_shouldProvideOnlyPrimitiveTags() {
    Map<Integer, byte[]> tlvs = BerTlv.parseSimple(ByteArrayUtil.fromHex(TLV1), true);
    assertThat(tlvs)
        .containsOnly(
            entry(0x84, ByteArrayUtil.fromHex("315449432E49434131")),
            entry(0x53, ByteArrayUtil.fromHex("0A3C2005141001")),
            entry(0xC7, ByteArrayUtil.fromHex("0000000011223344")));
  }

  @Test
  public void parse_whenTagsOrderChange_shouldProvideTheSameTags() {
    Map<Integer, byte[]> tlvs1 = BerTlv.parseSimple(ByteArrayUtil.fromHex(TLV1), true);
    Map<Integer, byte[]> tlvs2 = BerTlv.parseSimple(ByteArrayUtil.fromHex(TLV2), true);
    assertThat(tlvs1).containsExactlyEntriesOf(tlvs2);
  }

  @Test
  public void parse_whenTagsIdIs3Bytes_shouldProvideTheTag() {
    Map<Integer, byte[]> tlvs =
        BerTlv.parseSimple(
            ByteArrayUtil.fromHex(
                "6F258409315449432E49434131A518BF0C15DFEF2C08000000001122334453070A3C2005141001"),
            true);
    assertThat(tlvs)
        .containsOnly(
            entry(0x84, ByteArrayUtil.fromHex("315449432E49434131")),
            entry(0x53, ByteArrayUtil.fromHex("0A3C2005141001")),
            entry(0xDFEF2C, ByteArrayUtil.fromHex("0000000011223344")));
  }

  @Test(expected = IllegalArgumentException.class)
  public void parse_whenStructureIsInvalid_shouldIAE() {
    BerTlv.parseSimple(ByteArrayUtil.fromHex("6F23A5"), true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parse_whenLengthFieldIsInvalid_shouldIAE() {
    BerTlv.parseSimple(ByteArrayUtil.fromHex("6F83A5"), true);
  }

  @Test
  public void parse_whenLengthIsZero_shouldReturnEmptyValue() {
    Map<Integer, byte[]> tlvs = BerTlv.parseSimple(ByteArrayUtil.fromHex("8400"), false);
    assertThat(tlvs.get(0x84)).isEmpty();
  }

  @Test
  public void parse_whenLengthIsTwoBytes_shouldValue() {
    // length 250
    byte[] tlv = new byte[253];
    tlv[0] = (byte) 0x84;
    tlv[1] = (byte) 0x81;
    tlv[2] = (byte) 250;
    for (int i = 3; i < 253; i++) {
      tlv[i] = (byte) 0xA5;
    }
    Map<Integer, byte[]> tlvs = BerTlv.parseSimple(tlv, false);
    assertThat(tlvs.get(0x84)).hasSize(250);
    assertThat(tlvs.get(0x84)).containsOnly(0xA5);
  }

  @Test
  public void parse_whenLengthIsThreeBytes_shouldValue() {
    // length 260
    byte[] tlv = new byte[264];
    tlv[0] = (byte) 0x84;
    tlv[1] = (byte) 0x82;
    tlv[2] = (byte) 0x01;
    tlv[3] = (byte) 0x04;
    for (int i = 4; i < 264; i++) {
      tlv[i] = (byte) 0xA5;
    }
    Map<Integer, byte[]> tlvs = BerTlv.parseSimple(tlv, false);
    assertThat(tlvs.get(0x84)).hasSize(260);
    assertThat(tlvs.get(0x84)).containsOnly(0xA5);
  }

  @Test
  public void isConstructed_when1ByteTagIsConstructed_shouldReturnTrue() {
    assertThat(BerTlv.isConstructed(0x6F)).isTrue();
  }

  @Test
  public void isConstructed_when1ByteTagIsPrimitive_shouldReturnFalse() {
    assertThat(BerTlv.isConstructed(0x84)).isFalse();
  }

  @Test
  public void isConstructed_when2ByteTagIsConstructed_shouldReturnTrue() {
    assertThat(BerTlv.isConstructed(0xBC0C)).isTrue();
  }

  @Test
  public void isConstructed_when2ByteTagIsPrimitive_shouldReturnFalse() {
    assertThat(BerTlv.isConstructed(0x9F0C)).isFalse();
  }

  @Test
  public void isConstructed_when3ByteTagIsConstructed_shouldReturnTrue() {
    assertThat(BerTlv.isConstructed(0x6FEF2C)).isTrue();
  }

  @Test
  public void isConstructed_when3ByteTagIsPrimitive_shouldReturnFalse() {
    assertThat(BerTlv.isConstructed(0xDFEF2C)).isFalse();
  }

  @Test(expected = IllegalArgumentException.class)
  public void isConstructed_whenTagIsNegative_shouldIAE() {
    BerTlv.isConstructed(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void isConstructed_whenTagIsTooLarge_shouldIAE() {
    BerTlv.isConstructed(0x1000000);
  }
}
