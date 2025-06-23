/* **************************************************************************************
 * Copyright (c) 2021 Calypso Networks Association https://calypsonet.org/
 *
 * See the NOTICE file(s) distributed with this work for additional information
 * regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License 2.0 which is available at http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 ************************************************************************************** */
package org.eclipse.keyple.core.util;

import java.util.Collection;

/**
 * Exposes useful methods for testing method call parameters and raising a IllegalArgumentException
 * unchecked exception.
 *
 * @since 2.0.0
 */
public final class Assert {

  private static final String ARGUMENT = "Argument [";
  private static final String CONDITION = "Condition [";
  private static final String HAS_A_VALUE = "] has a value [";
  private static final String LESS_THAN = "] less than [";
  private static final String GREATER_THAN = "] greater than [";
  private static final String IS_NULL = "] is null.";
  private static final String IS_EMPTY = "] is empty.";
  private static final String IS_FALSE = "] is false.";
  private static final String IS_NOT_HEX = "] is not a hex string.";
  private static final String NOT_EQUAL_TO = "] not equal to [";
  private static final String CLOSING_BRACKET = "].";

  /** Singleton pattern */
  private static final Assert INSTANCE = new Assert();

  /** Private Constructor */
  private Assert() {}

  /**
   * Gets the unique instance.
   *
   * @return the instance
   */
  public static Assert getInstance() {
    return INSTANCE;
  }

  /**
   * Assert that the input object is not null.
   *
   * @param obj the object to check
   * @param name the object name
   * @return the current instance
   * @throws IllegalArgumentException if object is null
   * @since 2.0.0
   */
  public Assert notNull(Object obj, String name) {
    if (obj == null) {
      throw new IllegalArgumentException(ARGUMENT + name + IS_NULL);
    }
    return INSTANCE;
  }

  /**
   * Assert that the input string is not null and not empty.
   *
   * @param obj the object to check
   * @param name the object name
   * @return the current instance
   * @throws IllegalArgumentException if object is null or empty
   * @since 2.0.0
   */
  public Assert notEmpty(String obj, String name) {
    if (obj == null) {
      throw new IllegalArgumentException(ARGUMENT + name + IS_NULL);
    }
    if (obj.isEmpty()) {
      throw new IllegalArgumentException(ARGUMENT + name + IS_EMPTY);
    }
    return INSTANCE;
  }

  /**
   * Assert that a collection of objects is not null and not empty.
   *
   * @param obj the object to check
   * @param name the object name
   * @return the current instance
   * @throws IllegalArgumentException if object is null or empty
   * @since 2.0.0
   */
  public Assert notEmpty(Collection<?> obj, String name) {
    if (obj == null) {
      throw new IllegalArgumentException(ARGUMENT + name + IS_NULL);
    }
    if (obj.isEmpty()) {
      throw new IllegalArgumentException(ARGUMENT + name + IS_EMPTY);
    }
    return INSTANCE;
  }

  /**
   * Assert that a byte array is not null and not empty.
   *
   * @param obj the object to check
   * @param name the object name
   * @return the current instance
   * @throws IllegalArgumentException if object is null or empty
   * @since 2.0.0
   */
  public Assert notEmpty(byte[] obj, String name) {
    if (obj == null) {
      throw new IllegalArgumentException(ARGUMENT + name + IS_NULL);
    }
    if (obj.length == 0) {
      throw new IllegalArgumentException(ARGUMENT + name + IS_EMPTY);
    }
    return INSTANCE;
  }

  /**
   * Assert that a condition is true.
   *
   * @param condition the condition to check
   * @param name the object name
   * @return the current instance
   * @throws IllegalArgumentException if condition is null or false
   * @since 2.0.0
   */
  public Assert isTrue(Boolean condition, String name) {
    if (condition == null) {
      throw new IllegalArgumentException(CONDITION + name + IS_NULL);
    }
    if (!condition) {
      throw new IllegalArgumentException(CONDITION + name + IS_FALSE);
    }
    return INSTANCE;
  }

  /**
   * Assert that an integer is not null and is greater than or equal to minValue.
   *
   * @param number the number to check
   * @param minValue the min accepted value
   * @param name the object name
   * @return the current instance
   * @throws IllegalArgumentException if number is null or has a value less than minValue.
   * @since 2.0.0
   */
  public Assert greaterOrEqual(Integer number, int minValue, String name) {
    if (number == null) {
      throw new IllegalArgumentException(ARGUMENT + name + IS_NULL);
    }
    if (number < minValue) {
      throw new IllegalArgumentException(
          ARGUMENT + name + HAS_A_VALUE + number + LESS_THAN + minValue + CLOSING_BRACKET);
    }
    return INSTANCE;
  }

  /**
   * Assert that an integer is equal to value.
   *
   * @param number the number to check
   * @param value the expected value
   * @param name the object name
   * @return the current instance
   * @throws IllegalArgumentException if number is null or has a value less than minValue.
   * @since 2.0.0
   */
  public Assert isEqual(Integer number, int value, String name) {
    if (number == null) {
      throw new IllegalArgumentException(ARGUMENT + name + IS_NULL);
    }
    if (number != value) {
      throw new IllegalArgumentException(
          ARGUMENT + name + HAS_A_VALUE + number + NOT_EQUAL_TO + value + CLOSING_BRACKET);
    }
    return INSTANCE;
  }

  /**
   * Assert that an integer is not null and is in the range minValue, maxValue.
   *
   * @param number the number to check
   * @param minValue the min accepted value
   * @param maxValue the max accepted value
   * @param name the object name
   * @return the current instance
   * @throws IllegalArgumentException if number is null or is out of range.
   * @since 2.0.0
   */
  public Assert isInRange(Integer number, int minValue, int maxValue, String name) {
    if (number == null) {
      throw new IllegalArgumentException(ARGUMENT + name + IS_NULL);
    }
    if (number < minValue) {
      throw new IllegalArgumentException(
          ARGUMENT + name + HAS_A_VALUE + number + LESS_THAN + minValue + CLOSING_BRACKET);
    }
    if (number > maxValue) {
      throw new IllegalArgumentException(
          ARGUMENT + name + HAS_A_VALUE + number + GREATER_THAN + maxValue + CLOSING_BRACKET);
    }
    return INSTANCE;
  }

  /**
   * Assert that a string has a valid hexadecimal format.
   *
   * @param hex The string to check.
   * @param name The object name.
   * @return The current instance.
   * @throws IllegalArgumentException If the provided string is null, empty or has not a valid
   *     hexadecimal format.
   * @since 2.1.0
   */
  public Assert isHexString(String hex, String name) {
    if (!HexUtil.isValid(hex)) {
      throw new IllegalArgumentException(ARGUMENT + name + IS_NOT_HEX);
    }
    return INSTANCE;
  }
}
