package si.urbas.pless.test;

import java.util.HashMap;
import java.util.Map;

import static play.test.Helpers.inMemoryDatabase;
import static si.urbas.pless.test.JpaApplication.APP_CONFIG_JPA_DEFAULT;

public class TemporaryPlayJpaApplication extends TemporaryPlayApplication {

  public TemporaryPlayJpaApplication(HashMap<String, String> applicationOptions, String testPersistenceUnit) {
    super(configureInMemoryTestDatabase(applicationOptions, testPersistenceUnit));
  }

  private static Map<String, String> configureInMemoryTestDatabase(Map<String, String> applicationOptions, String testPersistenceUnit) {
    applicationOptions.putAll(inMemoryDatabase());
    applicationOptions.put(APP_CONFIG_JPA_DEFAULT, testPersistenceUnit);
    return applicationOptions;
  }
}
