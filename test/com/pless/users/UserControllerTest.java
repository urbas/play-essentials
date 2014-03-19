package com.pless.users;

import static com.pless.users.routes.ref.UserController;
import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.test.Helpers.callAction;
import static play.test.Helpers.status;

import javax.persistence.NoResultException;

import org.junit.Test;

import play.mvc.Result;

import com.pless.authentication.SaltedHashedPassword;
import com.pless.emailing.ExceptionThrowingMailer;
import com.pless.emailing.MockEmailProvider;
import com.pless.test.PlessControllerTest;

public class UserControllerTest extends PlessControllerTest {
  private static final String JANE_DOE_EMAIL = "JaneDoe@email.com";
  public static final String JOHN_SMITH = "John Smith";
  public static final String JOHN_SMITH_EMAIL = "john.smith@email.com";
  public static final String JOHN_SMITH_PASSWORD = "johns password";

  @Test(expected = NoResultException.class)
  public void signUp_MUST_not_persist_the_user_WHEN_sending_of_the_email_fails() throws Exception {
    MockEmailProvider.nestedMailer = new ExceptionThrowingMailer();
    try {
      callSignUpAction(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
    } catch (Exception e) {}
    new JpaUserRepository(getEm()).findUserByEmail(JOHN_SMITH_EMAIL);
  }

  private void assertJohnSmithIsInDb() {
    User createdUser = new JpaUserRepository(getEm()).findUserByEmail(JOHN_SMITH_EMAIL);
    long idOfNewUser = 1L;
    User expectedUser = createJohnSmithUser(idOfNewUser, createdUser.getSalt());
    assertEquals(expectedUser, createdUser);
  }

  private static User createJohnSmithUser(long userId, byte[] passwordSalt) {
    SaltedHashedPassword password = new SaltedHashedPassword(
      JOHN_SMITH_PASSWORD,
      passwordSalt);
    JpaUser user = new JpaUser(JOHN_SMITH_EMAIL, password);
    return user.withId(userId);
  }

  public static Result callSignUpAction(String email, String password) {
    return callAction(UserController.signUp(email, password));
  }

  public static void createTestUser() {
    callSignUpAction(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
  }
}
