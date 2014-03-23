package com.pless.test;

import static play.test.Helpers.*;

import java.util.HashMap;

import play.test.FakeApplication;
import play.test.Helpers;

public class TemporaryPlayJpaApplication implements AutoCloseable {
  private static final String TEST_PERSISTENCE_UNIT = "testPersistenceUnit";
  private static final String APP_CONFIG_JPA_DEFAULT = "jpa.default";
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
