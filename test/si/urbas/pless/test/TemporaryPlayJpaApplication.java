package si.urbas.pless.test;

import play.test.FakeApplication;
import play.test.Helpers;

import java.util.HashMap;

import static play.test.Helpers.*;
import static si.urbas.pless.test.TestJpaApplication.APP_CONFIG_JPA_DEFAULT;

public class TemporaryPlayJpaApplication implements AutoCloseable {
  private final HashMap<String, String> applicationOptions = new HashMap<>();
  private final FakeApplication fakeApplication;

  public TemporaryPlayJpaApplication(String testPersistenceUnit) {
    configureInMemoryTestDatabase(testPersistenceUnit);
    fakeApplication = fakeApplication(applicationOptions);
    Helpers.start(fakeApplication);
  }

  @Override
  public void close() {
    stop(fakeApplication);
  }

  private void configureInMemoryTestDatabase(String testPersistenceUnit) {
    applicationOptions.putAll(inMemoryDatabase());
    applicationOptions.put(APP_CONFIG_JPA_DEFAULT, testPersistenceUnit);
  }
}
