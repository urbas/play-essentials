package com.pless.util;

import java.util.concurrent.locks.ReentrantLock;

public class ScopedTestConfiguration implements AutoCloseable {

  private static ConfigurationSource oldPlayConfigurationSource;
  private static ReentrantLock singletonLock = new ReentrantLock();

  public ScopedTestConfiguration() {
  }
  
  public ScopedTestConfiguration(ConfigurationSource configuration) {
    singletonLock.lock();
    oldPlayConfigurationSource = PlayConfigurationSource.getConfigurationSource();
    PlayConfigurationSource.setPlayConfigurationSource(configuration);
  }
  
  public static ConfigurationSource getWrappedMockFactory() {
    return oldPlayConfigurationSource;
  }

  @Override
  public void close() throws Exception {
    PlayConfigurationSource.setPlayConfigurationSource(oldPlayConfigurationSource);
    oldPlayConfigurationSource = null;
    singletonLock.unlock();
  }

}
