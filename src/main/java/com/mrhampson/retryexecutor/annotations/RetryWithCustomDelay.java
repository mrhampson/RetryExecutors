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

package com.mrhampson.retryexecutor.annotations;

import com.mrhampson.retryexecutor.strategies.RetryStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to retry with a custom strategy
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RetryWithCustomDelay {
  /**
   * The class to be used for the strategy. Must implement {@link com.mrhampson.retryexecutor.strategies.RetryStrategy}
   * Must have a no-arg constructor
   * @return the {@link com.mrhampson.retryexecutor.strategies.RetryStrategy} class to use
   */
  Class<? extends RetryStrategy> retryStrategy();
}
