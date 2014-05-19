package si.urbas.pless.test;

import si.urbas.pless.util.Body;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class TestApplication implements AutoCloseable {

  protected static ReentrantLock globalTestApplicationLock = new ReentrantLock();
  protected List<AutoCloseable> temporaryServices = new ArrayList<>();

  public TestApplication() {
    globalTestApplicationLock.lock();
  }

  @Override
  public void close() {
    closeTemporaryServices();
    globalTestApplicationLock.unlock();
  }

  private void closeTemporaryServices() {
    for (int i = temporaryServices.size() - 1; i >= 0; --i) {
      try {
        temporaryServices.get(i).close();
      } catch (Exception ex) {
        throw new RuntimeException("Could not close the test application.", ex);
      }
    }
    temporaryServices.clear();
  }

  public void addTemporaryService(AutoCloseable temporaryService) {
    temporaryServices.add(temporaryService);
  }

  protected void doInitialisation(Body initialisationMethod) {
    try {
      initialisationMethod.invoke();
    } catch (Exception e) {
      close();
      throw new RuntimeException("Could not properly set up the test application. Please check your test configuration.", e);
    }
  }
}