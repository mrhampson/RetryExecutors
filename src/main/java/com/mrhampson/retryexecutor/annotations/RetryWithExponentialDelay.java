package com.mrhampson.retryexecutor.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * Annotation to retry runnables with an exponential delay
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RetryWithExponentialDelay {
  int multiplier();
  long initialDelay();
  TimeUnit initialDelayUnit();
  int maxTries();
}
