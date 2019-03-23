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
