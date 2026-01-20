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
package org.eclipse.keyple.core.util.logging;

import org.eclipse.keyple.core.util.logging.spi.LoggerProvider;

/**
 * Provides a temporary defaul implementation of the {@link LoggerProvider} interface that creates
 * loggers leveraging the SLF4J logging framework. Specifically, this class wraps SLF4J's {@link
 * org.slf4j.Logger}, returning instances of {@link Slf4jLogger}, which act as adapters between the
 * {@link LoggerProvider} interface and SLF4J.
 *
 * <p>This implementation is intended to standardize the creation of logger instances while
 * utilizing SLF4J for actual logging operations, ensuring compatibility with SLF4J-based logging
 * systems.
 *
 * <p>Thread-safe and suitable for use in multi-threaded applications, this class guarantees
 * consistent logger instances for a given class name.
 *
 * @since 2.5.0
 */
class Slf4jLoggerProvider implements LoggerProvider {

  /**
   * {@inheritDoc}
   *
   * @since 2.5.0
   */
  @Override
  public Logger getLogger(String className) {
    return new Slf4jLogger(org.slf4j.LoggerFactory.getLogger(className));
  }
}
