package com.mrhampson.retryexecutor.strategies;

import com.mrhampson.retryexecutor.annotations.RetryWithExponentialDelay;
import com.mrhampson.retryexecutor.annotations.RetryWithFixedDelay;

import java.lang.annotation.Annotation;

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
    } else {
      throw new IllegalArgumentException("Unsupported annotation");
    }
  }
}
