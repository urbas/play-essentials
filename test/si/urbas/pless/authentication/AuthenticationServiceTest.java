package si.urbas.pless.authentication;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import si.urbas.pless.users.*;

public class AuthenticationServiceTest {

  private static final String CRAFTED_SESSION_ID = "session id";
  private static final String NON_NUMERIC_VALUE = "non numeric value";
  private static final long JOHN_SMITH_USER_ID = 123L;
  private final User user = createJohnSmithUser();
  private AuthenticationService authenticationSession;
  private HashMapServerSessionStorage serverSessionStorage;
  private User activatedUser;

  @Before
  public void setUp() {
    activatedUser = spy(user);
    doReturn(true).when(activatedUser).isActivated();
    serverSessionStorage = new HashMapServerSessionStorage();
    authenticationSession = new AuthenticationService(
      new TestClientSessionStorage(),
      serverSessionStorage,
      new SessionIdGenerator());
  }

  @Test
  public void isLoggedIn_MUST_return_false_WHEN_session_is_not_started() throws Exception {
    assertFalse(authenticationSession.isLoggedIn());
  }

  @Test
  public void getLoggedInUserId_MUST_return_null_WHEN_session_is_not_started() throws Exception {
    assertNull(authenticationSession.getLoggedInUserEmail());
  }

  @Test(expected = IllegalArgumentException.class)
  public void logIn_MUST_throw_an_exception_WHEN_no_authentication_token_is_present() throws Exception {
    authenticationSession.logIn(null);
  }
  
  @Test(expected = IllegalStateException.class)
  public void logIn_MUST_throw_an_exception_WHEN_an_inactive_user_tries_to_log_in() throws Exception {
    authenticationSession.logIn(user);
  }

  @Test
  public void isLoggedIn_MUST_return_true_AFTER_a_successful_logIn() throws Exception {
    authenticationSession.logIn(activatedUser);
    assertTrue(authenticationSession.isLoggedIn());
  }

  @Test
  public void isLoggedIn_MUST_return_false_AFTER_session_expiration() throws Exception {
    authenticationSession.logIn(activatedUser);
    serverSessionStorage.expireAllEntries();
    assertFalse(authenticationSession.isLoggedIn());
  }

  @Test
  public void getLoggedInUserId_MUST_return_the_id_with_which_the_session_was_started() throws Exception {
    authenticationSession.logIn(activatedUser);
    assertEquals(activatedUser.getEmail(), authenticationSession.getLoggedInUserEmail());
  }

  private AuthenticationService prepareIllegalSessionIdScenario() {
    ServerSessionStorage serverSessionStorage = mock(ServerSessionStorage.class);
    SessionIdGenerator sessionIdGenerator = mock(SessionIdGenerator.class);
    when(serverSessionStorage.get(CRAFTED_SESSION_ID))
      .thenReturn(NON_NUMERIC_VALUE);
    when(sessionIdGenerator.createSessionId())
      .thenReturn(CRAFTED_SESSION_ID);
    final AuthenticationService authenticationSession = new AuthenticationService(new TestClientSessionStorage(), serverSessionStorage, sessionIdGenerator);
    return authenticationSession;
  }

  private User createJohnSmithUser() {
    return new JpaUser(UserControllerTest.JOHN_SMITH_EMAIL, UserControllerTest.JOHN_SMITH_PASSWORD)
      .withId(JOHN_SMITH_USER_ID);
  }
}
