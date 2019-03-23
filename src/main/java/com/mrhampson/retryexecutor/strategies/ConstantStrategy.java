package com.mrhampson.retryexecutor.strategies;

import com.mrhampson.retryexecutor.annotations.RetryWithFixedDelay;
import com.mrhampson.retryexecutor.exceptions.MaxTriesExceededException;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A {@link RetryStrategy} which will rerun tasks with a constant delay
 * @author Marshall Hampson
 */
public class ConstantStrategy extends AbstractStrategy {
  private final long constantDelayMillis;
  private final int maxTries;

  /**
   * Creates a new {@link ConstantStrategy}
   * @param delay the delay
   * @param delayUnit the delay unit
   */
  public ConstantStrategy(long delay, TimeUnit delayUnit, int maxTries) {
    super(maxTries);
    Objects.requireNonNull(delayUnit);
    if (maxTries < 1) {
      throw new IllegalArgumentException("maxTries must be greater than 1");
    }
    this.constantDelayMillis = delayUnit.toMillis(delay);
    this.maxTries = maxTries;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long getMillisDelayBeforeNextTry() throws MaxTriesExceededException {
    this.updateTryCounter();
    return this.constantDelayMillis;
  }

  /**
   * Creates a new {@link ConstantStrategy} from the annotation
   * @param annotation the annotation
   * @return the {@link ConstantStrategy}
   */
  public static ConstantStrategy fromAnnotation(RetryWithFixedDelay annotation) {
    Objects.requireNonNull(annotation);
    return new ConstantStrategy(annotation.delay(), annotation.delayUnit(), annotation.maxTries());
  }
}
