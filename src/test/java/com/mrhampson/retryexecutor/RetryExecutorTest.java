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

package com.mrhampson.retryexecutor;

import com.mrhampson.retryexecutor.annotations.RetryWithCustomDelay;
import com.mrhampson.retryexecutor.annotations.RetryWithExponentialDelay;
import com.mrhampson.retryexecutor.annotations.RetryWithFixedDelay;
import com.mrhampson.retryexecutor.exceptions.MaxTriesExceededException;
import com.mrhampson.retryexecutor.strategies.ConstantStrategy;
import com.mrhampson.retryexecutor.strategies.RetryStrategy;
import org.junit.Test;

import java.util.concurrent.*;

import static org.junit.Assert.*;

public class RetryExecutorTest {

  @Test
  public void testExecuteFixedDelay() {
    testExecutorWithRunnable(new ConstantTestRunnable());
  }

  @Test
  public void testExecuteExponentialDelay() {
    testExecutorWithRunnable(new ExponentialTestRunnable());
  }

  @Test
  public void testCustomDelay() {
    testExecutorWithRunnable(new CustomStrategyRunnable());
  }

  private void testExecutorWithRunnable(HelloWorldRunnable testRunnable) {
    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    RetryExecutor retryExecutor = new RetryExecutor(executorService);
    retryExecutor.execute(testRunnable);
    long countRemaing = testRunnable.getLatch().getCount();
    while (countRemaing > 0) {
      try {
        Thread.sleep(1_000);
        countRemaing = testRunnable.getLatch().getCount();
      }
      catch (InterruptedException e) {

      }
    }
  }

  @RetryWithFixedDelay(delay = 1, delayUnit = TimeUnit.SECONDS, maxTries = 10)
  class ConstantTestRunnable extends HelloWorldRunnable {
    public ConstantTestRunnable() {
      super(10);
    }
  }

  @RetryWithExponentialDelay(multiplier = 2, initialDelay = 1, initialDelayUnit = TimeUnit.SECONDS, maxTries = 5)
  class ExponentialTestRunnable extends HelloWorldRunnable {
    public ExponentialTestRunnable() {
      super(5);
    }
  }

  @RetryWithCustomDelay(retryStrategy = CustomStrategy.class)
  class CustomStrategyRunnable extends HelloWorldRunnable {
    public CustomStrategyRunnable() {
      super(5);
    }
  }

  public static final class CustomStrategy implements RetryStrategy {
    private final RetryStrategy strategy = new ConstantStrategy(1, TimeUnit.SECONDS, 5);

    public CustomStrategy() { }

    @Override
    public long getMillisDelayBeforeNextTry() throws MaxTriesExceededException {
      return this.strategy.getMillisDelayBeforeNextTry();
    }
  }

  class HelloWorldRunnable implements Runnable {
    private final CountDownLatch latch;

    public HelloWorldRunnable(int maxTimeToRun) {
      this.latch = new CountDownLatch(maxTimeToRun);
    }

    @Override
    public void run() {
      System.out.println("Hello World!");
      latch.countDown();
      throw new RuntimeException("Thrown on purpose");
    }

    public CountDownLatch getLatch() {
      return latch;
    }
  }

}