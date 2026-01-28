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
 * A no-operation implementation of the {@link LoggerProvider} interface.
 *
 * <p>This implementation returns a {@link NoOpLogger} instance for any logger name. The {@link
 * NoOpLogger} provides a logger that performs no actions for all logging methods, ensuring minimal
 * overhead when logging is not required.
 *
 * <p>This class is typically used as a default or placeholder implementation of {@link
 * LoggerProvider} where explicit logging is not needed.
 *
 * @since 2.5.0
 */
final class NoOpLoggerProvider implements LoggerProvider {

  /**
   * {@inheritDoc}
   *
   * @since 2.5.0
   */
  @Override
  public Logger getLogger(String className) {
    return new NoOpLogger();
  }
}
