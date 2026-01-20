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
 * A no-operation implementation of the {@link Logger} interface.
 *
 * <p>This implementation is used as a placeholder or default logger when logging is not required or
 * needed. All logging methods in this class are effectively no-ops, meaning they do nothing.
 * Additionally, all checks for logging level enablement always return {@code false}.
 *
 * <p>This class is designed to avoid unnecessary overhead while providing a valid {@link Logger}
 * implementation.
 *
 * @since 2.5.0
 */
final class NoOpLogger implements Logger {

  /**
   * {@inheritDoc}
   *
   * @since 2.5.0
   */
  @Override
  public boolean isTraceEnabled() {
    return false;
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.5.0
   */
  @Override
  public boolean isDebugEnabled() {
    return false;
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.5.0
   */
  @Override
  public boolean isInfoEnabled() {
    return false;
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.5.0
   */
  @Override
  public boolean isWarnEnabled() {
    return false;
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.5.0
   */
  @Override
  public boolean isErrorEnabled() {
    return false;
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.5.0
   */
  @Override
  public void trace(String message, Object... args) {}

  /**
   * {@inheritDoc}
   *
   * @since 2.5.0
   */
  @Override
  public void debug(String message, Object... args) {}

  /**
   * {@inheritDoc}
   *
   * @since 2.5.0
   */
  @Override
  public void info(String message, Object... args) {}

  /**
   * {@inheritDoc}
   *
   * @since 2.5.0
   */
  @Override
  public void warn(String message, Object... args) {}

  /**
   * {@inheritDoc}
   *
   * @since 2.5.0
   */
  @Override
  public void error(String message, Object... args) {}

  /**
   * {@inheritDoc}
   *
   * @since 2.5.0
   */
  @Override
  public void warn(String message, Throwable throwable, Object... args) {}

  /**
   * {@inheritDoc}
   *
   * @since 2.5.0
   */
  @Override
  public void error(String message, Throwable throwable, Object... args) {}
}
