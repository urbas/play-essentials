package com.urmest.authentication;

import static com.urmest.users.UserControllerTest.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.*;

import com.urmest.users.User;
import com.urmest.users.UserRepository;

public class AuthenticationTest {

  private Authentication authentication;
  private UserRepository userRepository;
  private AuthenticationSession authSession;
  private User existingUser;

  @Before
  public void setUp() {
    userRepository = mock(UserRepository.class);
    authSession = mock(AuthenticationSession.class);
    existingUser = new User(JOHN_SMITH, JOHN_SMITH_EMAIL, new SaltedHashedPassword(JOHN_SMITH_PASSWORD));
    when(userRepository.findUserByEmail(JOHN_SMITH_EMAIL)).thenReturn(existingUser);
    authentication = new Authentication(userRepository, authSession);
  }

  @Test(expected = Exception.class)
  public void authenticate_MUST_throw_an_exception_WHEN_the_user_does_not_exist() throws Exception {
    authentication.authenticate("joe@example.com", "test1234");
  }

  @Test
  public void authenticate_MUST_return_the_user_WHEN_authentication_was_successful() throws Exception {
    User authenticatedUser = authentication
      .authenticate(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
    Assert.assertEquals(existingUser, authenticatedUser);
  }

  @Test(expected = IllegalArgumentException.class)
  public void authenticate_MUST_throw_an_exception_WHEN_the_password_is_not_correct() throws Exception {
    User authenticatedUser = authentication
      .authenticate(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD + "foo");
    Assert.assertEquals(existingUser, authenticatedUser);
  }
}
