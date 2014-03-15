package com.urmest.authentication;

import static com.urmest.users.UserControllerTest.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.urmest.users.User;

public class AuthenticationSessionTest {

  private static final int JOHN_SMITH_USER_ID = 123;

  private final AuthenticationToken authenticationToken = new AuthenticationToken(createJohnSmithUser());
  private AuthenticationSession authenticationSession;

  private TestServerSessionStorage serverSessionStorage;

  @Before
  public void setUp() {
    serverSessionStorage = new TestServerSessionStorage();
    authenticationSession = new AuthenticationSession(new TestClientSessionStorage(), serverSessionStorage, new SessionIdGenerator());
  }

  @Test
  public void isLoggedIn_MUST_return_false_WHEN_session_is_not_started() throws Exception {
    assertFalse(authenticationSession.isLoggedIn());
  }

  @Test(expected = IllegalStateException.class)
  public void getLoggedInUserId_MUST_throw_an_exception_WHEN_session_is_not_started() throws Exception {
    authenticationSession.getLoggedInUserId();
  }

  @Test(expected = IllegalArgumentException.class)
  public void logIn_MUST_throw_an_exception_WHEN_no_authentication_token_is_present() throws Exception {
    authenticationSession.logIn(null);
  }

  @Test
  public void isLoggedIn_MUST_return_true_AFTER_a_successful_logIn() throws Exception {
    authenticationSession.logIn(authenticationToken);
    assertTrue(authenticationSession.isLoggedIn());
  }
  
  @Test
  public void isLoggedIn_MUST_return_false_AFTER_session_expiration() throws Exception {
    authenticationSession.logIn(authenticationToken);
    serverSessionStorage.expireAllEntries();
    assertFalse(authenticationSession.isLoggedIn());
  }

  private User createJohnSmithUser() {
    SaltedHashedPassword password = new SaltedHashedPassword(JOHN_SMITH_PASSWORD);
    return new User(JOHN_SMITH, JOHN_SMITH_EMAIL, password)
      .withId(JOHN_SMITH_USER_ID);
  }
}
