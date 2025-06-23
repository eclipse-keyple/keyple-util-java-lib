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
package org.eclipse.keyple.core.util.json;

/**
 * POJO used to transport the content of an {@link Exception} into a string.
 *
 * @since 2.0.0
 */
public class BodyError {

  private final String code;
  private final Exception exception;

  /**
   * Builds an instance from an exception reference.
   *
   * @param exception The exception.
   * @since 2.0.0
   */
  public BodyError(Exception exception) {
    this.exception = exception;
    this.code = exception.getClass().getName();
  }

  /**
   * Gets the exception class name.
   *
   * @return a not null value.
   * @since 2.0.0
   */
  public String getCode() {
    return code;
  }

  /**
   * Gets the associated exception.
   *
   * @return a not null reference.
   * @since 2.0.0
   */
  public Exception getException() {
    return exception;
  }
}
