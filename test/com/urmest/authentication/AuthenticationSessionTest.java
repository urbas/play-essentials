package com.urmest.authentication;

import static com.urmest.authentication.AuthenticationSession.*;
import static com.urmest.users.UserControllerTest.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.urmest.users.User;

import play.mvc.Http.Session;

public class AuthenticationSessionTest {
  private static final int JOHN_SMITH_USER_ID = 123;
  private static final String JOHN_SMITH_USER_ID_AS_STRING =
    Long.toString(JOHN_SMITH_USER_ID);
  private final User userJohnSmith = createJohnSmithUser();

  private Session session;
  private AuthenticationSession authSession;

  @Before
  public void setUp() {
    session = mock(Session.class);
    this.authSession = new AuthenticationSession(session);
  }

  @Test
  public void isLoggedIn_MUST_return_false_WHEN_the_userId_is_not_present_in_the_session() throws Exception {
    assertFalse(authSession.isLoggedIn());
  }

  @Test(expected = IllegalStateException.class)
  public void getLoggedInUserId_MUST_throw_an_exception_WHEN_the_userId_is_not_present_in_the_session() throws Exception {
    authSession.getLoggedInUserId();
  }

  @Test
  public void isLoggedIn_MUST_return_true_WHEN_the_userId_is_present_in_the_session() throws Exception {
    when(session.get(SESSION_KEY_LOGGED_IN_USER_ID))
      .thenReturn(JOHN_SMITH_USER_ID_AS_STRING);
    assertTrue(authSession.isLoggedIn());
  }

  @Test
  public void getLoggedInUserId_MUST_return_the_users_id_WHEN_the_userId_is_present_in_the_session() throws Exception {
    when(session.get(SESSION_KEY_LOGGED_IN_USER_ID))
      .thenReturn(JOHN_SMITH_USER_ID_AS_STRING);
    assertEquals(JOHN_SMITH_USER_ID, authSession.getLoggedInUserId());
  }

  @Test(expected = IllegalArgumentException.class)
  public void startLoginSession_MUST_throw_an_exception_WHEN_the_given_user_is_null() throws Exception {
    authSession.logIn(null);
  }

  @Test
  public void startLoginSession_MUST_update_the_session() throws Exception {
    authSession.logIn(userJohnSmith);
    verify(session)
      .put(SESSION_KEY_LOGGED_IN_USER_ID, JOHN_SMITH_USER_ID_AS_STRING);
  }

  private User createJohnSmithUser() {
    SaltedHashedPassword password = new SaltedHashedPassword(JOHN_SMITH_PASSWORD);
    return new User(JOHN_SMITH, JOHN_SMITH_EMAIL, password)
      .withId(JOHN_SMITH_USER_ID);
  }
}
