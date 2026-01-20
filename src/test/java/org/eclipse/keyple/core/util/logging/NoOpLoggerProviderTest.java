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

import org.junit.Test;

public class NoOpLoggerProviderTest {

  @Test
  public void shouldReturnNoOpLogger() {
    NoOpLoggerProvider provider = new NoOpLoggerProvider();
    assertThat(provider.getLogger("test.logger")).isInstanceOf(NoOpLogger.class);
  }
}
