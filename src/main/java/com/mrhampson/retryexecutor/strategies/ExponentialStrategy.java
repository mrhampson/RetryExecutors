package com.mrhampson.retryexecutor.strategies;

import com.mrhampson.retryexecutor.annotations.RetryWithExponentialDelay;
import com.mrhampson.retryexecutor.exceptions.MaxTriesExceededException;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * An {@link RetryStrategy} that delays the task longer each time it fails
 * @author Marshall Hampson
 */
public class ExponentialStrategy extends AbstractStrategy {

  private final long initialDelayMillis;
  private final int multiplier;
  private final int maxTries;

  /**
   * Creates new {@link ExponentialStrategy}
   * @param multiplier the multiplier that multiplies the delay time between each task
   * @param maxTries stop after this many tries
   */
  public ExponentialStrategy(long initialDelay, TimeUnit initialDelayUnit, int multiplier, int maxTries) {
    super(maxTries);
    Objects.requireNonNull(initialDelay);
    Objects.requireNonNull(initialDelayUnit);
    if (multiplier < 1) {
      throw new IllegalArgumentException("multiplier must be 1 or greater");
    }
    if (maxTries < 1) {
      throw new IllegalArgumentException("maxTries must be 1 or greater");
    }
    this.initialDelayMillis = initialDelayUnit.toMillis(initialDelay);
    this.multiplier = multiplier;
    this.maxTries = maxTries;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long getMillisDelayBeforeNextTry() throws MaxTriesExceededException {
    int nextTry = this.updateTryCounter();
    return nextTry * this.multiplier * this.initialDelayMillis;
  }

  /**
   * Creates a new {@link ExponentialStrategy} from an annotation
   * @param annotation the annotation
   * @return the strategy from the annotation
   */
  public static ExponentialStrategy fromAnnotation(RetryWithExponentialDelay annotation) {
    Objects.requireNonNull(annotation);
    return new ExponentialStrategy(annotation.initialDelay(), annotation.initialDelayUnit(), annotation.multiplier(),
            annotation.maxTries());
  }
}
