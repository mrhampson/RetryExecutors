
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

import com.mrhampson.retryexecutor.annotations.RetryWithCustomDelay;
import com.mrhampson.retryexecutor.annotations.RetryWithExponentialDelay;
import com.mrhampson.retryexecutor.annotations.RetryWithFixedDelay;

import java.lang.annotation.Annotation;
import java.util.Objects;

/**
 * Class serves as a mapping from the annotations to the corresponding {@link com.mrhampson.retryexecutor.strategies.RetryStrategy}
 * @author Marshall Hampson
 */
public class AnnotationToRetryStrategyFactory {

  private AnnotationToRetryStrategyFactory() {

  }

  /**
   * Creates a {@link RetryStrategy} from the available annotations
   * @param annotation the annotation
   * @return the {@link RetryStrategy} initialized from the annotation
   */
  public static RetryStrategy fromAnnotation(Annotation annotation) {
    if (annotation instanceof RetryWithExponentialDelay) {
      return ExponentialStrategy.fromAnnotation((RetryWithExponentialDelay)annotation);
    } else if (annotation instanceof RetryWithFixedDelay) {
      return ConstantStrategy.fromAnnotation((RetryWithFixedDelay)annotation);
    } else if (annotation instanceof RetryWithCustomDelay) {
      return instantiateFromCustomStrategy((RetryWithCustomDelay)annotation);
    }
    else {
      throw new IllegalArgumentException("Unsupported annotation");
    }
  }

  /**
   * Handles instantiating a {@link RetryStrategy} provided by an {@link RetryWithCustomDelay}
   * @param customAnnotation the annotation
   * @return a {@link RetryStrategy} if it could be instantiated
   */
  private static RetryStrategy instantiateFromCustomStrategy(RetryWithCustomDelay customAnnotation) {
    Objects.requireNonNull(customAnnotation);
    Class<? extends RetryStrategy> retryStrategyClass = customAnnotation.retryStrategy();
    if (!RetryStrategy.class.isAssignableFrom(retryStrategyClass)) {
      throw new IllegalArgumentException("RetryWithCustomDelay must provide a class that implements RetryStrategy");
    }
    try {
      return retryStrategyClass.getDeclaredConstructor().newInstance();
    }
    catch (NoSuchMethodException e) {
      throw new IllegalArgumentException("Provided class must have an accessible no-arg constructor");
    }
    catch (Exception e) {
      throw new IllegalArgumentException("An error occurred instantiating the strategy", e);
    }
  }
}
