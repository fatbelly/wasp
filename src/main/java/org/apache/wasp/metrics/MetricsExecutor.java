/**
 * Copyright The Apache Software Foundation
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.apache.wasp.metrics;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class to handle the ScheduledExecutorService{@link ScheduledExecutorService}
 */
public class MetricsExecutor {


  public static ScheduledExecutorService getExecutor() {
    return ExecutorSingleton.INSTANCE.scheduler;
  }

  public static void stop() {
    if (!getExecutor().isShutdown()) {
      getExecutor().shutdown();
    }
  }

  private enum ExecutorSingleton {
    INSTANCE;

    private final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(
        1, new ThreadPoolExecutorThreadFactory("Wasp-Metrics2-"));
  }

  private static class ThreadPoolExecutorThreadFactory implements ThreadFactory {
    private final String name;
    private final AtomicInteger threadNumber = new AtomicInteger(1);

    private ThreadPoolExecutorThreadFactory(String name) {
      this.name = name;
    }

    @Override
    public Thread newThread(Runnable runnable) {
      Thread t = new Thread(runnable, name + threadNumber.getAndIncrement());
      t.setDaemon(true);
      return t;
    }
  }
}
