package si.urbas.pless.authentication;

import org.junit.Before;
import org.junit.Test;
import si.urbas.pless.sessions.SessionIdGenerator;
import si.urbas.pless.test.sessions.HashMapClientSessionStorage;
import si.urbas.pless.test.sessions.HashMapServerSessionStorage;
import si.urbas.pless.users.PlessUser;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static si.urbas.pless.authentication.AuthenticationService.authenticationService;
import static si.urbas.pless.test.util.ScopedConfiguration.withMockConfig;
import static si.urbas.pless.users.UserControllerTest.*;
import static si.urbas.pless.util.ConfigurationSource.configurationSource;

public class AuthenticationServiceTest {

  private static final long JOHN_SMITH_USER_ID = 123L;
  @SuppressWarnings("UnusedDeclaration")
  private static AuthenticationService.AuthenticationServiceSingleton authenticationServiceSingleton = new AuthenticationService.AuthenticationServiceSingleton();
  private final PlessUser user = createJohnSmithUser();
  private AuthenticationService authenticationSession;
  private HashMapServerSessionStorage serverSessionStorage;
  private PlessUser activatedUser;
  private HashMapClientSessionStorage clientSessionStorage;

  @Before
  public void setUp() {
    activatedUser = spy(user);
    doReturn(true).when(activatedUser).isActivated();
    serverSessionStorage = new HashMapServerSessionStorage();
    clientSessionStorage = new HashMapClientSessionStorage();
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

  @Test
  public void getLoggedInUserId_MUST_return_negative_1_WHEN_session_is_not_started() throws Exception {
    assertEquals(-1, authenticationSession.getLoggedInUserId());
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
  public void getLoggedInUserEmail_MUST_return_the_email_with_which_the_session_was_started() throws Exception {
    authenticationSession.logIn(activatedUser);
    assertEquals(activatedUser.getEmail(), authenticationSession.getLoggedInUserEmail());
  }

  @Test
  public void getLoggedInUserId_MUST_return_the_id_with_which_the_session_was_started() throws Exception {
    authenticationSession.logIn(activatedUser);
    assertEquals(activatedUser.getId(), authenticationSession.getLoggedInUserId());
  }

  @Test
  public void authenticationService_MUST_always_return_the_same_instance_WHEN_in_production_mode() throws Exception {
    withMockConfig(() -> {
      when(configurationSource().isProduction()).thenReturn(true);
      assertThat(
        authenticationService(),
        is(sameInstance(authenticationService()))
      );
    });
  }

  @Test
  public void authenticationService_MUST_always_return_a_new_instance_WHEN_not_in_production_mode() throws Exception {
    withMockConfig(() ->
        assertThat(
          authenticationService(),
          is(not(sameInstance(authenticationService())))
        )
    );
  }


  private PlessUser createJohnSmithUser() {
    return new PlessUser(JOHN_SMITH_USER_ID, JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
  }
}
