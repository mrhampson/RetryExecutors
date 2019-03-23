package com.mrhampson.retryexecutor.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * Annotation for runnables to retry with constant delays
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RetryWithFixedDelay {
  long delay();
  TimeUnit delayUnit();
  int maxTries();
}

