/*
 * Copyright (c) 2019 Marshall Hampson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 */
package com.mrhampson.retryexecutor.strategies;

import com.mrhampson.retryexecutor.exceptions.MaxTriesExceededException;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A base class {@link RetryStrategy} can optionally derive from to share the code to count task retries
 * @author Marshall Hampson
 */
public abstract class AbstractStrategy implements RetryStrategy {
  private final AtomicInteger tryCounter = new AtomicInteger(0);

  protected final int maxTries;

  /**
   * Creates a new {@link AbstractStrategy}
   * @param maxTries the max tries for this task
   */
  public AbstractStrategy(int maxTries) {
    if (maxTries < 1) {
      throw new IllegalArgumentException("maxTries must be greater than 1");
    }
    this.maxTries = maxTries;
  }

  /**
   * Updates the tryCounter and returns the new values
   * @return the updated try count
   * @throws MaxTriesExceededException if we've exceeded the number of tries
   */
  protected int updateTryCounter() {
    int tryCount = this.tryCounter.incrementAndGet();
    if (tryCount > this.maxTries) {
      throw new MaxTriesExceededException();
    }
    return tryCount;
  }
}
