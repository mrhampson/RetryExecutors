package com.mrhampson.retryexecutor;

import com.mrhampson.retryexecutor.strategies.AnnotationToRetryStrategyFactory;
import com.mrhampson.retryexecutor.strategies.RetryStrategy;

import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * RetryExecutor
 * An executor that will retry tasks when they throw an exception according to a particular {@link RetryStrategy}
 * @author Marshall Hampson
 */
public class RetryExecutor implements Executor {

  private final ScheduledExecutorService delegateExecutor;

  /**
   * Creates a new {@link RetryExecutor}
   * @param scheduledExecutorService the scheduledExecutorService this executor will run tasks on
   */
  public RetryExecutor(ScheduledExecutorService scheduledExecutorService) {
    Objects.requireNonNull(scheduledExecutorService);
    this.delegateExecutor = scheduledExecutorService;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void execute(Runnable command) {
    Objects.requireNonNull(command);
    Annotation[] annotations = command.getClass().getAnnotations();
    RetryStrategy retryStrategy = getFirstStrategyFromAnnotations(annotations);
    if (retryStrategy != null) {
      this.execute(command, retryStrategy);
    } else {
      this.delegateExecutor.execute(command);
    }
  }

  /**
   * Executres a task according to a particular {@link RetryStrategy}
   * @param command the command to run
   * @param retryStrategy the strategy used to reschedule upon exception
   */
  public void execute(Runnable command, RetryStrategy retryStrategy) {
    Objects.requireNonNull(command);
    Objects.requireNonNull(retryStrategy);
    this.delegateExecutor.execute(new RunnableWrapper(command, retryStrategy));
  }

  /**
   * Gets the first {@link RetryStrategy} we can construct from the annotations
   * @param annotations the annotations
   * @return a {@link RetryStrategy} if one could be built otherwise null
   */
  private static RetryStrategy getFirstStrategyFromAnnotations(Annotation[] annotations) {
    Objects.requireNonNull(annotations);
    for (Annotation annotation : annotations) {
      RetryStrategy retryStrategy = null;
      try {
        retryStrategy = AnnotationToRetryStrategyFactory.fromAnnotation(annotation);
      }
      catch (IllegalArgumentException ignored) {

      }
      return retryStrategy;
    }
    return null;
  }

  /**
   * An internal wrapper class that handles executing the runnable
   */
  private final class RunnableWrapper implements Runnable {
    private final Runnable runnable;
    private final RetryStrategy retryStrategy;

    /**
     * Creates a new {@link RunnableWrapper}
     * @param runnable the runnable to run
     * @param retryStrategy the strategy used to reschedule upon exception
     */
    private RunnableWrapper(Runnable runnable, RetryStrategy retryStrategy) {
      this.runnable = runnable;
      this.retryStrategy = retryStrategy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
      try {
        this.runnable.run();
      }
      catch (Exception e) {
        long nextTryDelay = this.retryStrategy.getMillisDelayBeforeNextTry();
        RetryExecutor.this.delegateExecutor.schedule(this, nextTryDelay, TimeUnit.MILLISECONDS);
      }
    }
  }
}
