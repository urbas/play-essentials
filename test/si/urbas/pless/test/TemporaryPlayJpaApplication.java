package si.urbas.pless.test;

import play.test.FakeApplication;
import play.test.Helpers;

import java.util.HashMap;

import static play.test.Helpers.*;
import static si.urbas.pless.test.TestJpaApplication.APP_CONFIG_JPA_DEFAULT;
import static si.urbas.pless.test.TestJpaApplication.TEST_PERSISTENCE_UNIT;

public class TemporaryPlayJpaApplication implements AutoCloseable {
  private final HashMap<String, String> applicationOptions = new HashMap<>();
  private final FakeApplication fakeApplication;

  public TemporaryPlayJpaApplication() {
    configureInMemoryTestDatabase();
    fakeApplication = fakeApplication(applicationOptions);
    Helpers.start(fakeApplication);
  }

  @Override
  public void close() {
    stop(fakeApplication);
  }

  private void configureInMemoryTestDatabase() {
    applicationOptions.putAll(inMemoryDatabase());
    applicationOptions.put(APP_CONFIG_JPA_DEFAULT, TEST_PERSISTENCE_UNIT);
  }
}
