[![Build Status](https://travis-ci.org/mrhampson/RetryExecutors.svg?branch=master)](https://travis-ci.org/mrhampson/RetryExecutors)

# RetryExecutors
Java `Executor` that makes it easy to have tasks retry if they fail

## Motivation
One use case for this is to deal with rate limited APIs were you might 
need to have your request back-off for a while until the limit is up

## Getting Started
1. Create a `RetryExecutor` 
`  RetryExecutor retryExecutor = new RetryExecutor(executorService);`
   It delegates to the `ScheduledExecutorService` you pass it to run its task
2. Create a `Runnable` and annotate with the desired `RetryStrategy`
   ```java
   @RetryWithExponentialDelay(multiplier = 2, initialDelay = 1, initialDelayUnit = TimeUnit.SECONDS, maxTries = 5)
   class MyRunnable implements Runnable {
   ```
3. Run the `Runnable` on the `RetryExecutor` and it will execute according to the policy noted in the annotation
   The `RetryExecutor` also lets you run an unannotated `Runnable`s as well
   
   
 ## Using in your project
 Can include this in your project using JitPack https://jitpack.io/docs/#building-with-jitpack
