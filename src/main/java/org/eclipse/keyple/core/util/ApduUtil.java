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
 * Util to build APDUs and check case 4.
 *
 * @since 2.0.0
 */
public final class ApduUtil {

  /**
   * private<br>
   * Constructor
   */
  private ApduUtil() {}

  /**
   * Builds an APDU request from its elements as defined by the ISO 7816 standard.
   *
   * @param cla The class byte.
   * @param ins The instruction byte.
   * @param p1 The parameter 1.
   * @param p2 The parameter 2.
   * @param dataIn The data field of the command (optional). If empty, then LC will be set to 0.
   * @param le The maximum number of bytes expected in the data field of the response to the command
   *     (optional).
   * @return A byte array containing the resulting apdu command data.
   * @since 2.0.0
   */
  public static byte[] build(byte cla, byte ins, byte p1, byte p2, byte[] dataIn, Byte le) {
    byte[] apduCommand;

    /* Buffer allocation */
    apduCommand = allocateBuffer(dataIn, le);

    /* Build APDU buffer from provided arguments */
    apduCommand[0] = cla;
    apduCommand[1] = ins;
    apduCommand[2] = p1;
    apduCommand[3] = p2;

    /* ISO7618 case determination and Le management */
    if (dataIn != null) {
      /* append Lc and ingoing data */
      apduCommand[4] = (byte) dataIn.length;
      System.arraycopy(dataIn, 0, apduCommand, 5, dataIn.length);
      if (le != null) {
        apduCommand[apduCommand.length - 1] = le;
      } else {
        /* case3: ingoing data only, no Le */
      }
    } else {
      if (le != null) {
        /* case2: outgoing data only */
        apduCommand[4] = le;
      } else {
        /* case1: no ingoing, no outgoing data, P3/Le = 0 */
        apduCommand[4] = (byte) 0x00;
      }
    }
    return apduCommand;
  }

  /**
   * (private)<br>
   * Returns a byte array having the expected length according the APDU construction rules.
   *
   * @param data Data array (could be null).
   * @param le Expected outgoing length (could be null).
   * @return A new byte array.
   */
  private static byte[] allocateBuffer(byte[] data, Byte le) {
    int length = 4; // header
    if (data == null && le == null) {
      // case 1: 5-byte apdu, le=0
      length += 1; // Le
    } else {
      if (data != null) {
        length += data.length + 1; // Lc + data
      }
      if (le != null) {
        length += 1; // Le
      }
    }
    return new byte[length];
  }

  /**
   * Indicates if the provided byte array contains a case4 APDU command.
   *
   * <p>The ISO7816 case for data in a command-response pair is determined from the provided
   * arguments:
   *
   * <ul>
   *   <li><code>dataIn &nbsp;= null, le &nbsp;= null</code>&nbsp;&nbsp;&rarr;&nbsp;&nbsp;case 1: no
   *       command data, no response data expected.
   *   <li><code>dataIn &nbsp;= null, le != null</code>&nbsp;&nbsp;&rarr;&nbsp;&nbsp;case 2: no
   *       command data, expected response data.
   *   <li><code>dataIn != null, le &nbsp;= null</code>&nbsp;&nbsp;&rarr;&nbsp;&nbsp;case 3: command
   *       data, no response data expected.
   *   <li><code>dataIn != null, le &nbsp;= 0&nbsp;&nbsp;&nbsp;</code>
   *       &nbsp;&nbsp;&rarr;&nbsp;&nbsp;case 4: command data, expected response data.
   * </ul>
   *
   * Only the indication for case 4 is retained in the end.<br>
   * In this case (incoming and outgoing data for the card), Le is set to 0, letting the lower layer
   * (see API plugin) take care of recovering the exact length of the outgoing data.
   *
   * @param apduCommand The apduCommand to check.
   * @return true the APDU command is case 4.
   * @since 2.0.0
   */
  public static boolean isCase4(byte[] apduCommand) {
    if (apduCommand != null && apduCommand.length > 4) {
      return apduCommand[4] == apduCommand.length - 6;
    }
    return false;
  }
}
