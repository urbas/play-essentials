package com.pless.util;

import java.util.concurrent.locks.ReentrantLock;

public class ScopedTestFactory implements Factory<Object>, AutoCloseable {

  private static Factory<?> wrappedMockFactory;
  private static ReentrantLock singletonLock = new ReentrantLock();

  public ScopedTestFactory() {
  }
  
  public ScopedTestFactory(Factory<?> wrappedFactory) {
    singletonLock.lock();
    wrappedMockFactory = wrappedFactory;
  }

  @Override
  public Object createInstance(ConfigurationSource configurationSource) {
    return wrappedMockFactory.createInstance(configurationSource);
  }
  
  public static Factory<?> getWrappedMockFactory() {
    return wrappedMockFactory;
  }

  @Override
  public void close() throws Exception {
    ScopedTestFactory.wrappedMockFactory = null;
    singletonLock.unlock();
  }

}
