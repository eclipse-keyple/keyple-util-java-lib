/* **************************************************************************************
 * Copyright (c) 2026 Calypso Networks Association https://calypsonet.org/
 *
 * See the NOTICE file(s) distributed with this work for additional information
 * regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License 2.0 which is available at http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 ************************************************************************************** */
package org.eclipse.keypleother;

import org.eclipse.keyple.core.util.json.BodyErrorJsonDeserializerTest;

/**
 * Exception located in a package which is not allowed for deserialization, although its name starts
 * like an allowed one.
 */
public class OtherException extends RuntimeException {

  static {
    BodyErrorJsonDeserializerTest.otherExceptionLoaded = true;
  }

  public OtherException(String message) {
    super(message);
    BodyErrorJsonDeserializerTest.otherExceptionCreated = true;
  }
}
