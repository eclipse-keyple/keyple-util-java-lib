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
import java.util.stream.Collectors;
import org.eclipse.keyple.core.util.logging.spi.LoggerProvider;

public final class LoggerFactory {

  private static volatile LoggerProvider provider;

  private LoggerFactory() {}

  static {
    provider = loadProvider();
  }

  private static LoggerProvider loadProvider() {

    ServiceLoader<LoggerProvider> loader = ServiceLoader.load(LoggerProvider.class);

    List<LoggerProvider> providers = new ArrayList<>();
    loader.forEach(providers::add);

    if (providers.isEmpty()) {
      return new NoOpLoggerProvider();
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

  public static void setProvider(LoggerProvider provider) {
    LoggerFactory.provider = provider;
  }

  public static Logger getLogger(Class<?> clazz) {
    return provider.getLogger(clazz.getName());
  }
}
