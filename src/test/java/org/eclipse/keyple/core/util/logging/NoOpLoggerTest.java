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
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.Test;

public class NoOpLoggerTest {

  @Test
  public void levelsShouldBeDisabled() {
    NoOpLogger logger = new NoOpLogger();
    assertThat(logger.isTraceEnabled()).isFalse();
    assertThat(logger.isDebugEnabled()).isFalse();
    assertThat(logger.isInfoEnabled()).isFalse();
    assertThat(logger.isWarnEnabled()).isFalse();
    assertThat(logger.isErrorEnabled()).isFalse();
  }

  @Test
  public void methodsShouldNotThrowException() {
    NoOpLogger logger = new NoOpLogger();
    assertThatCode(
            () -> {
              logger.trace("message", "arg");
              logger.debug("message", "arg");
              logger.info("message", "arg");
              logger.warn("message", "arg");
              logger.error("message", "arg");
              logger.warn("message", new Exception(), "arg");
              logger.error("message", new Exception(), "arg");
            })
        .doesNotThrowAnyException();
  }
}
