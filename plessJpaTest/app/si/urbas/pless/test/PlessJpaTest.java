package si.urbas.pless.test;

import si.urbas.pless.sessions.ClientSessionStorage;
import si.urbas.pless.test.util.PlessTest;

public class PlessJpaTest extends PlessTest {
  public static final String DEFAULT_TEST_PERSISTENCE_UNIT = "testPersistenceUnit";

  @Override
  protected TestApplication createTestApplication() {
    return new JpaApplication(getTestPersistenceUnit(), getClientSessionStorage());
  }

  protected String getTestPersistenceUnit() {
    return DEFAULT_TEST_PERSISTENCE_UNIT;
  }

  protected ClientSessionStorage getClientSessionStorage() {
    return null;
  }

}
