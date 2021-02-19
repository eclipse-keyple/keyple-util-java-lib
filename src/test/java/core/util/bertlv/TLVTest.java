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
package core.util.bertlv;

import static org.eclipse.keyple.core.util.bertlv.Tag.TagType.CONSTRUCTED;
import static org.eclipse.keyple.core.util.bertlv.Tag.TagType.PRIMITIVE;

import org.eclipse.keyple.core.util.ByteArrayUtil;
import org.eclipse.keyple.core.util.bertlv.TLV;
import org.eclipse.keyple.core.util.bertlv.Tag;
import org.junit.Assert;
import org.junit.Test;

public class TLVTest {

  @Test
  public void parse() {
    Tag tag1 = new Tag(0x04, Tag.CONTEXT, PRIMITIVE, 1);
    Tag tag2 = new Tag(0x04, Tag.CONTEXT, CONSTRUCTED, 1);
    TLV tlv = new TLV(ByteArrayUtil.fromHex("84050011223344"));
    // 1st parsing
    Assert.assertTrue(tlv.parse(tag1, 0));
    // 2nd same parsing
    Assert.assertTrue(tlv.parse(tag1, 0));
    // search another tag
    Assert.assertFalse(tlv.parse(tag2, 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void parse_tag_null() {
    TLV tlv = new TLV(ByteArrayUtil.fromHex("84050011223344"));
    tlv.parse(null, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parse_too_large_index() {
    Tag tag = new Tag(0x04, Tag.CONTEXT, PRIMITIVE, 1);
    TLV tlv = new TLV(ByteArrayUtil.fromHex("84050011223344"));
    tlv.parse(null, 20);
  }

  @Test
  public void getValue() {
    Tag tag1 = new Tag(0x04, Tag.CONTEXT, PRIMITIVE, 1);
    TLV tlv = new TLV(ByteArrayUtil.fromHex("84050011223344"));
    Assert.assertTrue(tlv.parse(tag1, 0));
    Assert.assertArrayEquals(ByteArrayUtil.fromHex("0011223344"), tlv.getValue());

    // length octets variant
    tlv = new TLV(ByteArrayUtil.fromHex("8481050011223344"));
    Assert.assertTrue(tlv.parse(tag1, 0));
    Assert.assertArrayEquals(ByteArrayUtil.fromHex("0011223344"), tlv.getValue());
  }

  @Test
  public void getPosition() {
    Tag tag1 = new Tag(0x04, Tag.CONTEXT, PRIMITIVE, 1);
    // two TLV
    TLV tlv = new TLV(ByteArrayUtil.fromHex("8405001122334484055566778899"));
    Assert.assertTrue(tlv.parse(tag1, 0));
    // test position before getValue
    Assert.assertEquals(2, tlv.getPosition());
    Assert.assertArrayEquals(ByteArrayUtil.fromHex("0011223344"), tlv.getValue());
    // test position after getValue
    Assert.assertEquals(7, tlv.getPosition());
    Assert.assertTrue(tlv.parse(tag1, tlv.getPosition()));
    // test position before getValue
    Assert.assertEquals(9, tlv.getPosition());
    Assert.assertArrayEquals(ByteArrayUtil.fromHex("5566778899"), tlv.getValue());
    // test position after getValue
    Assert.assertEquals(14, tlv.getPosition());
  }
}
