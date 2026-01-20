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

import org.junit.Before;
import org.junit.Test;

public class Slf4jLoggerTest {

  private org.slf4j.Logger mockDelegate;
  private Slf4jLogger slf4jLogger;

  @Before
  public void setUp() {
    mockDelegate = mock(org.slf4j.Logger.class);
    slf4jLogger = new Slf4jLogger(mockDelegate);
  }

  @Test
  public void shouldDelegateEnabledChecks() {
    when(mockDelegate.isTraceEnabled()).thenReturn(true);
    assertThat(slf4jLogger.isTraceEnabled()).isTrue();

    when(mockDelegate.isDebugEnabled()).thenReturn(true);
    assertThat(slf4jLogger.isDebugEnabled()).isTrue();

    when(mockDelegate.isInfoEnabled()).thenReturn(true);
    assertThat(slf4jLogger.isInfoEnabled()).isTrue();

    when(mockDelegate.isWarnEnabled()).thenReturn(true);
    assertThat(slf4jLogger.isWarnEnabled()).isTrue();

    when(mockDelegate.isErrorEnabled()).thenReturn(true);
    assertThat(slf4jLogger.isErrorEnabled()).isTrue();
  }

  @Test
  public void shouldDelegateLogMethods() {
    Object[] args = {"arg1"};

    slf4jLogger.trace("msg", args);
    verify(mockDelegate).trace("msg", args);

    slf4jLogger.debug("msg", args);
    verify(mockDelegate).debug("msg", args);

    slf4jLogger.info("msg", args);
    verify(mockDelegate).info("msg", args);

    slf4jLogger.warn("msg", args);
    verify(mockDelegate).warn("msg", args);

    slf4jLogger.error("msg", args);
    verify(mockDelegate).error("msg", args);
  }

  @Test
  public void shouldDelegateLogWithThrowableMethods() {
    Throwable t = new RuntimeException();
    Object[] args = {"arg1"};

    slf4jLogger.warn("msg", t, args);
    verify(mockDelegate).warn("msg", args, t);

    slf4jLogger.error("msg", t, args);
    verify(mockDelegate).error("msg", args, t);
  }
}
