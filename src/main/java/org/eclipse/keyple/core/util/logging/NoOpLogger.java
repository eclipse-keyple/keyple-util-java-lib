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

final class NoOpLogger implements Logger {

  @Override
  public boolean isTraceEnabled() {
    return false;
  }

  @Override
  public boolean isDebugEnabled() {
    return false;
  }

  @Override
  public boolean isInfoEnabled() {
    return false;
  }

  @Override
  public boolean isWarnEnabled() {
    return false;
  }

  @Override
  public boolean isErrorEnabled() {
    return false;
  }

  @Override
  public void trace(String message, Object... args) {}

  @Override
  public void debug(String message, Object... args) {}

  @Override
  public void info(String message, Object... args) {}

  @Override
  public void warn(String message, Object... args) {}

  @Override
  public void error(String message, Object... args) {}

  @Override
  public void warn(String message, Throwable throwable, Object... args) {}

  @Override
  public void error(String message, Throwable throwable, Object... args) {}
}
