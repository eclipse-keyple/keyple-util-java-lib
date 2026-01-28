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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.eclipse.keyple.core.util.logging.spi.LoggerProvider;
import org.junit.Test;

public class LoggerFactoryTest {

  @Test
  public void getLogger_shouldReturnLoggerFromProvider() {
    // Given
    LoggerProvider mockProvider = mock(LoggerProvider.class);
    Logger mockLogger = mock(Logger.class);
    when(mockProvider.getLogger(anyString())).thenReturn(mockLogger);

    // When
    LoggerFactory.setProvider(mockProvider);
    Logger logger = LoggerFactory.getLogger(LoggerFactoryTest.class);

    // Then
    assertThat(logger).isSameAs(mockLogger);
    verify(mockProvider).getLogger(LoggerFactoryTest.class.getName());
  }

  @Test
  public void setProvider_shouldAllowOverride() {
    // Given
    LoggerProvider mockProvider = mock(LoggerProvider.class);

    // When
    LoggerFactory.setProvider(mockProvider);
    LoggerFactory.getLogger(String.class);

    // Then
    verify(mockProvider).getLogger(String.class.getName());
  }
}
