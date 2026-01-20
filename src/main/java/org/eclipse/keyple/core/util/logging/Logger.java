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

/**
 * Defines a logging interface with methods for logging at various levels such as trace, debug,
 * info, warn, and error. Provides methods to check if a specific logging level is enabled, allowing
 * conditional logging based on the current configuration.
 *
 * @since 2.5.0
 */
public interface Logger {

  /**
   * Returns whether trace logging is enabled.
   *
   * @return true if trace is enabled, false otherwise
   * @since 2.5.0
   */
  boolean isTraceEnabled();

  /**
   * Returns whether debug logging is enabled.
   *
   * @return true if debug is enabled, false otherwise
   * @since 2.5.0
   */
  boolean isDebugEnabled();

  /**
   * Returns whether info logging is enabled.
   *
   * @return true if info is enabled, false otherwise
   * @since 2.5.0
   */
  boolean isInfoEnabled();

  /**
   * Returns whether warn logging is enabled.
   *
   * @return true if warn is enabled, false otherwise
   * @since 2.5.0
   */
  boolean isWarnEnabled();

  /**
   * Returns whether error logging is enabled.
   *
   * @return true if error is enabled, false otherwise
   * @since 2.5.0
   */
  boolean isErrorEnabled();

  /**
   * Logs a message at the trace level.
   *
   * @param message the message to log
   * @param args the arguments to format the message
   * @since 2.5.0
   */
  void trace(String message, Object... args);

  /**
   * Logs a message at the debug level.
   *
   * @param message the message to log
   * @param args the arguments to format the message
   * @since 2.5.0
   */
  void debug(String message, Object... args);

  /**
   * Logs a message at the info level.
   *
   * @param message the message to log
   * @param args the arguments to format the message
   * @since 2.5.0
   */
  void info(String message, Object... args);

  /**
   * Logs a message at the warn level.
   *
   * @param message the message to log
   * @param args the arguments to format the message
   * @since 2.5.0
   */
  void warn(String message, Object... args);

  /**
   * Logs a message at the error level.
   *
   * @param message the message to log
   * @param args the arguments to format the message
   * @since 2.5.0
   */
  void error(String message, Object... args);

  /**
   * Logs a message and a throwable at the warn level.
   *
   * @param message the message to log
   * @param throwable the throwable to log
   * @param args the arguments to format the message
   * @since 2.5.0
   */
  void warn(String message, Throwable throwable, Object... args);

  /**
   * Logs a message and a throwable at the error level.
   *
   * @param message the message to log
   * @param throwable the throwable to log
   * @param args the arguments to format the message
   * @since 2.5.0
   */
  void error(String message, Throwable throwable, Object... args);
}
