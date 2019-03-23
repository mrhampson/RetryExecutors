package com.mrhampson.retryexecutor.strategies;

import com.mrhampson.retryexecutor.exceptions.MaxTriesExceededException;

/**
 * An interface all retry strategies must implement
 * @author Marshall Hampson
 */
public interface RetryStrategy {

  /**
   * Determines the delay until the task should run again after failure
   * @return the delay in millis until the task should run again
   * @throws MaxTriesExceededException if the task should never run again
   */
  long getMillisDelayBeforeNextTry() throws MaxTriesExceededException;
}
