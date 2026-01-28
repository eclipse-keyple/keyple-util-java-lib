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
package org.eclipse.keyple.core.util.logging.spi;

import org.eclipse.keyple.core.util.logging.Logger;

/**
 * Provides an abstraction for obtaining {@link Logger} instances. This interface centralizes the
 * creation and retrieval of loggers by name, allowing different implementations to support various
 * logging frameworks or custom logging behavior.
 *
 * <p>Implementations of this interface should ensure thread-safety and provide a consistent logger
 * instance for a given name.
 *
 * @since 2.5.0
 */
public interface LoggerProvider {

  /**
   * Retrieves a {@link Logger} instance associated with the specified class name. The returned
   * logger can be used for logging messages at different levels.
   *
   * @param className the name of the logger to retrieve
   * @return the {@link Logger} instance associated with the specified class name
   * @since 2.5.0
   */
  Logger getLogger(String className);
}
