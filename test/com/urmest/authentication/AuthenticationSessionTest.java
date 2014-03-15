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
  private static final String FOO_SESSION_ID = "fooSessionId";
  private static final int JOHN_SMITH_USER_ID = 123;

  private ClientSessionStorage clientSession;
  private AuthenticationSession authSession;
  private ServerSessionStorage serverSessionStorage;
  private SessionIdGenerator sessionIdGenerator;
  private final AuthenticationToken authenticationToken = new AuthenticationToken(createJohnSmithUser());

  @Before
  public void setUp() {
    clientSession = mock(ClientSessionStorage.class);
    serverSessionStorage = mock(ServerSessionStorage.class);
    sessionIdGenerator = mock(SessionIdGenerator.class);
    when(sessionIdGenerator.createSessionId()).thenReturn(FOO_SESSION_ID);
    this.authSession = new AuthenticationSession(clientSession, serverSessionStorage, sessionIdGenerator);
  }

  @Test
  public void isLoggedIn_MUST_return_false_WHEN_session_is_not_started() throws Exception {
    assertFalse(authSession.isLoggedIn());
  }

  @Test(expected = IllegalStateException.class)
  public void getLoggedInUserId_MUST_throw_an_exception_WHEN_session_is_not_started() throws Exception {
    authSession.getLoggedInUserId();
  }

  @Test(expected = IllegalArgumentException.class)
  public void logIn_MUST_throw_an_exception_WHEN_the_given_user_is_null() throws Exception {
    authSession.logIn(null);
  }

  @Test
  public void logIn_MUST_update_the_client_session() throws Exception {
    authSession.logIn(authenticationToken);
    verify(clientSession).put(SESSION_ID_KEY, FOO_SESSION_ID);
  }
  
  @Test
  public void logIn_MUST_store_the_session_into_the_server_storage() throws Exception {
    authSession.logIn(authenticationToken);
    verify(serverSessionStorage).put(
      AuthenticationSession.getSessionStorageKey(FOO_SESSION_ID),
      authenticationToken.getAuthenticatedUser().getId(),
      authSession.getExpirationSeconds());
  }

  private User createJohnSmithUser() {
    SaltedHashedPassword password = new SaltedHashedPassword(JOHN_SMITH_PASSWORD);
    return new User(JOHN_SMITH, JOHN_SMITH_EMAIL, password)
      .withId(JOHN_SMITH_USER_ID);
  }
}
