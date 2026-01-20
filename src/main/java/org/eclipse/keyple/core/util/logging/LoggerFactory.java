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

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import org.eclipse.keyple.core.util.logging.spi.LoggerProvider;

/**
 * A factory class for creating and managing {@link Logger} instances.
 *
 * <p>This class provides a centralized mechanism to retrieve loggers for specific classes and
 * allows dynamic configuration of the underlying {@link LoggerProvider} implementation. By default,
 * it initializes a {@link LoggerProvider} using the {@link ServiceLoader} mechanism, but it also
 * provides a method to programmatically set a custom provider.
 *
 * <p>The default implementation uses temporary a {@link Slf4jLoggerProvider} if no suitable {@link
 * LoggerProvider} is found, ensuring that logging operations perform no actions.
 *
 * <p>This class is thread-safe and ensures only one {@link LoggerProvider} is active at any given
 * time.
 *
 * @since 2.5.0
 */
public final class LoggerFactory {

  private static volatile LoggerProvider provider;
  private static final AtomicBoolean warningEmitted = new AtomicBoolean(false);

  private LoggerFactory() {}

  static {
    provider = loadProvider();
  }

  private static LoggerProvider loadProvider() {

    ServiceLoader<LoggerProvider> loader = ServiceLoader.load(LoggerProvider.class);

    List<LoggerProvider> providers = new ArrayList<>();
    loader.forEach(providers::add);

    if (providers.isEmpty()) {
      // return new NoOpLoggerProvider(); FIXME uncomment when major version is bumped
      return new Slf4jLoggerProvider(); // FIXME remove when major version is bumped
    }

    if (providers.size() > 1) {
      String providersList =
          providers.stream().map(p -> p.getClass().getName()).collect(Collectors.joining(", "));
      throw new IllegalStateException(
          "Multiple Keyple LoggerProviders found: "
              + providersList
              + ". Please keep only one logging implementation on the classpath.");
    }

    return providers.get(0);
  }

  /**
   * Sets the {@link LoggerProvider} to be used by the {@link LoggerFactory}.
   *
   * <p>This method allows overriding the default logger provider with a custom implementation. It
   * is typically used to integrate a specific logging framework or behavior.
   *
   * @param provider the {@link LoggerProvider} instance to set; must not be null
   * @since 2.5.0
   */
  public static void setProvider(LoggerProvider provider) {
    LoggerFactory.provider = provider;
  }

  /**
   * Retrieves a {@link Logger} instance associated with the specified class. The logger can be used
   * for logging messages at various levels, enabling class-specific logging behavior.
   *
   * @param clazz the class for which the logger is to be retrieved; must not be null
   * @return the {@link Logger} instance associated with the specified class
   * @since 2.5.0
   */
  public static Logger getLogger(Class<?> clazz) {
    emitNoLoggerWarningIfNeeded();
    return provider.getLogger(clazz.getName());
  }

  private static void emitNoLoggerWarningIfNeeded() {
    if ((provider instanceof Slf4jLoggerProvider || provider instanceof NoOpLoggerProvider)
        && warningEmitted.compareAndSet(false, true)) {
      // Direct use of System.err ONLY for this warning because no logging system is available.
      System.err.println(
          "[Keyple][WARN] No LoggerProvider found on classpath. "
              // + "Logging is disabled (NoOpLogger in use). " FIXME uncomment when major version is
              // bumped
              + "Logging is set to Slf4j version 1.7.32 (Slf4jLogger in use). " // FIXME remove when
              // major version is bumped
              + "Add one of the keyple-logging-xxx-jvm-lib dependencies to enable logging, "
              + "or provide a custom implementation using LoggerFactory.setProvider().");
    }
  }
}
