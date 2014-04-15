package si.urbas.pless.authentication;

import org.junit.Before;
import org.junit.Test;
import si.urbas.pless.users.JpaPlessUser;
import si.urbas.pless.users.PlessUser;
import si.urbas.pless.users.UserControllerTest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class AuthenticationServiceTest {

  private static final long JOHN_SMITH_USER_ID = 123L;
  private final PlessUser user = createJohnSmithUser();
  private AuthenticationService authenticationSession;
  private HashMapServerSessionStorage serverSessionStorage;
  private PlessUser activatedUser;
  private TestClientSessionStorage clientSessionStorage;

  @Before
  public void setUp() {
    activatedUser = spy(user);
    doReturn(true).when(activatedUser).isActivated();
    serverSessionStorage = new HashMapServerSessionStorage();
    clientSessionStorage = new TestClientSessionStorage();
    authenticationSession = new AuthenticationService(
      clientSessionStorage,
      serverSessionStorage,
      new SessionIdGenerator());
  }

  @Test
  public void isLoggedIn_MUST_return_false_WHEN_session_is_not_started() throws Exception {
    assertFalse(authenticationSession.isLoggedIn());
  }

  @Test
  public void getLoggedInUserEmail_MUST_return_null_WHEN_session_is_not_started() throws Exception {
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
  public void logOut_MUST_remove_all_entries_from_the_server_session_storage() throws Exception {
    authenticationSession.logIn(activatedUser);
    authenticationSession.logOut();
    assertTrue(serverSessionStorage.isEmpty());
  }

  @Test
  public void logOut_MUST_remove_all_entries_from_the_client_session_storage() throws Exception {
    authenticationSession.logIn(activatedUser);
    authenticationSession.logOut();
    assertTrue(clientSessionStorage.isEmpty());
  }

  @Test
  public void getLoggedInUserEmail_MUST_return_the_id_with_which_the_session_was_started() throws Exception {
    authenticationSession.logIn(activatedUser);
    assertEquals(activatedUser.getEmail(), authenticationSession.getLoggedInUserEmail());
  }

  private PlessUser createJohnSmithUser() {
    return new JpaPlessUser(UserControllerTest.JOHN_SMITH_EMAIL, UserControllerTest.JOHN_SMITH_PASSWORD)
      .withId(JOHN_SMITH_USER_ID);
  }
}
