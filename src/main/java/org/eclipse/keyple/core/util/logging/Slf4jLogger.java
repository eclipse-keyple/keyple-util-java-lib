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
 * A temporary default implementation of the {@link Logger} interface that delegates all logging
 * operations to an instance of {@link org.slf4j.Logger}. This class acts as a wrapper around the
 * SLF4J logging framework, providing a consistent logging interface.
 *
 * <p>This implementation is intended to bridge logging functionality provided by SLF4J with the
 * {@link Logger} interface, enabling compatibility and flexibility in logging methods.
 *
 * <p>All methods in this class directly delegate to the corresponding methods in the SLF4J {@link
 * org.slf4j.Logger} instance provided during construction.
 *
 * @since 2.5.0
 */
final class Slf4jLogger implements Logger {

  private final org.slf4j.Logger delegate;

  /**
   * Constructs a new {@code Slf4jLogger} instance that delegates all logging operations to the
   * provided SLF4J {@link org.slf4j.Logger} instance.
   *
   * @param delegate The SLF4J {@link org.slf4j.Logger} instance to which all logging operations
   *     will be delegated. Must not be null.
   * @since 2.5.0
   */
  Slf4jLogger(org.slf4j.Logger delegate) {
    this.delegate = delegate;
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.5.0
   */
  @Override
  public boolean isTraceEnabled() {
    return delegate.isTraceEnabled();
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.5.0
   */
  @Override
  public boolean isDebugEnabled() {
    return delegate.isDebugEnabled();
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.5.0
   */
  @Override
  public boolean isInfoEnabled() {
    return delegate.isInfoEnabled();
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.5.0
   */
  @Override
  public boolean isWarnEnabled() {
    return delegate.isWarnEnabled();
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.5.0
   */
  @Override
  public boolean isErrorEnabled() {
    return delegate.isErrorEnabled();
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.5.0
   */
  @Override
  public void trace(String message, Object... args) {
    delegate.trace(message, args);
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.5.0
   */
  @Override
  public void debug(String message, Object... args) {
    delegate.debug(message, args);
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.5.0
   */
  @Override
  public void info(String message, Object... args) {
    delegate.info(message, args);
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.5.0
   */
  @Override
  public void warn(String message, Object... args) {
    delegate.warn(message, args);
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.5.0
   */
  @Override
  public void error(String message, Object... args) {
    delegate.error(message, args);
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.5.0
   */
  @Override
  public void warn(String message, Throwable throwable, Object... args) {
    delegate.warn(message, args, throwable);
  }

  /**
   * {@inheritDoc}
   *
   * @since 2.5.0
   */
  @Override
  public void error(String message, Throwable throwable, Object... args) {
    delegate.error(message, args, throwable);
  }
}
