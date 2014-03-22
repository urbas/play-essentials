package com.pless.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import com.pless.util.TemporaryGlobalConfiguration;

public class PlessTestApplication implements AutoCloseable {

  private static ReentrantLock globalTestApplicationLock = new ReentrantLock();
  public static final String SENDER_EXAMPLE_COM = "sender@example.com";
  private List<AutoCloseable> temporaryServices = new ArrayList<>();

  public PlessTestApplication() {
    globalTestApplicationLock.lock();
    temporaryServices.add(new TemporaryGlobalConfiguration());
    temporaryServices.add(new TemporaryEmailProvider());
    temporaryServices.add(new TemporaryUserRepository());
    temporaryServices.add(new TemporaryServerSessionStorage());
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

}
