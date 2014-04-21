package si.urbas.pless.util;

import java.util.concurrent.locks.ReentrantLock;

public class ScopedTestFactory implements Factory<Object>, AutoCloseable {

  private static Factory<?> wrappedMockFactory;
  private static ReentrantLock singletonLock = new ReentrantLock();
  private static int constructorInvokations = 0;

  public ScopedTestFactory() {
    constructorInvokations++;
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
    constructorInvokations = 0;
    singletonLock.unlock();
  }

  public static int getConstructorInvokations() {
    return constructorInvokations;
  }
}
