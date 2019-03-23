package com.mrhampson.retryexecutor;

import com.mrhampson.retryexecutor.annotations.RetryWithExponentialDelay;
import com.mrhampson.retryexecutor.annotations.RetryWithFixedDelay;
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

  private void testExecutorWithRunnable(TestRunnable testRunnable) {
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
  class ConstantTestRunnable implements TestRunnable {
    CountDownLatch latch = new CountDownLatch(10);

    @Override
    public void run() {
      System.out.println("Hello World!");
      latch.countDown();
      throw new RuntimeException("Thrown on purpose");
    }

    @Override
    public CountDownLatch getLatch() {
      return latch;
    }
  }

  @RetryWithExponentialDelay(multiplier = 2, initialDelay = 1, initialDelayUnit = TimeUnit.SECONDS, maxTries = 5)
  class ExponentialTestRunnable implements TestRunnable {
    CountDownLatch latch = new CountDownLatch(5);

    @Override
    public void run() {
      System.out.println("Hello World!");
      latch.countDown();
      throw new RuntimeException("Thrown on purpose");
    }

    @Override
    public CountDownLatch getLatch() {
      return latch;
    }
  }

  interface TestRunnable extends Runnable {
    CountDownLatch getLatch();
  }
}