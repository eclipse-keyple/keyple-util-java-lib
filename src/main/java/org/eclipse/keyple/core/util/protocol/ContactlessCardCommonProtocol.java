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
package org.eclipse.keyple.core.util.protocol;

/**
 * This enum contains a non-exhaustive list of contactless smartcard communication protocols.
 *
 * @since 2.0.0
 * @deprecated This list of protocol names is arbitrary and is not used by Keyple services. The
 *     protocol identifiers can be freely defined by the business application during polling
 *     configuration and card selection.
 */
@Deprecated
public enum ContactlessCardCommonProtocol {

  // contactless standard
  ISO_14443_4,

  // contactless NFC compliant
  NFC_A_ISO_14443_3A,
  NFC_B_ISO_14443_3B,
  NFC_F_JIS_6319_4,
  NFC_V_ISO_15693,

  // other contactless proprietary protocols
  INNOVATRON_B_PRIME_CARD
}
