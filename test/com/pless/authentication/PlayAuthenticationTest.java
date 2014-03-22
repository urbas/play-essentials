package com.pless.authentication;

import static com.pless.authentication.PlayAuthentication.logIn;
import static com.pless.users.PlayUserRepository.getUserRepository;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import com.pless.test.PlessTest;
import com.pless.users.JpaUser;
import com.pless.users.PlayUserRepository;

public class PlayAuthenticationTest extends PlessTest {

  private static final String EMAIL = "j@smith.com";
  private static final String PASSWORD = "password1234";

  @Test(expected = IllegalArgumentException.class)
  public void logIn_MUST_throw_an_exception_WHEN_credentials_are_incomplete() throws Exception {
    logIn(mock(PasswordLoginForm.class));
  }

  @Test(expected = IllegalArgumentException.class)
  public void logIn_MUST_throw_an_exception_WHEN_the_user_does_not_exist() throws Exception {
    logIn(new PasswordLoginForm(EMAIL, PASSWORD));
  }

  @Test
  @Ignore
  public void logIn_MUST_start_a_login_session_WHEN_the_credentials_are_okay() throws Exception {
    when(getUserRepository().findUserByEmail(EMAIL))
    .thenReturn(new JpaUser(EMAIL, PASSWORD));
    logIn(new PasswordLoginForm(EMAIL, PASSWORD));
  }
}
