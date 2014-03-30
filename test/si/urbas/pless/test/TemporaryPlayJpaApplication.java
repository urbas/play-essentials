package si.urbas.pless.test;

import static si.urbas.pless.test.TestEntityManagerFactory.APP_CONFIG_JPA_DEFAULT;
import static si.urbas.pless.test.TestEntityManagerFactory.TEST_PERSISTENCE_UNIT;
import static play.test.Helpers.*;

import java.util.HashMap;

import play.test.FakeApplication;
import play.test.Helpers;

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
