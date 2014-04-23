package si.urbas.pless.test;

import si.urbas.pless.test.util.PlessTest;

/**
 * Creates a completely mock Pless application.
 * <p/>
 * All the services are Mockito mocks or spies. You can stub and verify on their methods as you wish.
 * <p/>
 * Notable features:
 * <ul>
 * <li>
 * the user repository is a full implementation of the {@link si.urbas.pless.users.UserRepository}
 * specification. It remembers users etc.
 * </li>
 * </ul>
 */
public class MockedAppTest extends PlessTest {

  @Override
  protected TestApplication createTestApplication() {
    return new MockedApplication();
  }
}
