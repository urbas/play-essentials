package com.urmest.users;

import static com.urmest.users.routes.ref.UserController;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.callAction;
import static play.test.Helpers.status;

import javax.persistence.NoResultException;

import org.junit.Test;
import org.mockito.Mockito;

import play.mvc.Result;

import com.urmest.authentication.SaltedHashedPassword;
import com.urmest.emailing.ExceptionThrowingMailer;
import com.urmest.emailing.MockEmailProvider;
import com.urmest.test.UrmestTest;

public class UserControllerTest extends UrmestTest {
  private static final String JANE_DOE = "Jane Doe";
  private static final String JANE_DOE_EMAIL = "JaneDoe@email.com";
  public static final String JOHN_SMITH = "John Smith";
  public static final String JOHN_SMITH_EMAIL = "john.smith@email.com";
  public static final String JOHN_SMITH_PASSWORD = "johns password";

  @Test
  public void signUp_MUST_result_in_bad_request_WHEN_any_of_the_parameters_are_empty() throws Exception {
    Result result = callSignUpAction("", "", "");
    assertEquals(BAD_REQUEST, status(result));
    result = callSignUpAction("", JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
    assertEquals(BAD_REQUEST, status(result));
    result = callSignUpAction(JOHN_SMITH, "", JOHN_SMITH_PASSWORD);
    assertEquals(BAD_REQUEST, status(result));
    result = callSignUpAction(JOHN_SMITH, JOHN_SMITH_EMAIL, "");
    assertEquals(BAD_REQUEST, status(result));
  }

  @Test
  public void signUp_MUST_result_in_ok_response_WHEN_all_parameters_are_okay() throws Exception {
    Result result = callSignUpAction(JOHN_SMITH, JOHN_SMITH_EMAIL,
      JOHN_SMITH_PASSWORD);
    assertEquals(OK, status(result));
  }

  @Test
  public void signUp_MUST_put_the_new_user_into_the_database() throws Exception {
    callSignUpAction(JOHN_SMITH, JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
    assertJohnSmithIsInDb();
  }

  @Test
  public void signUp_MUST_send_an_email() throws Exception {
    callSignUpAction(JOHN_SMITH, JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
    Mockito.verify(MockEmailProvider.lastSentEmail).send();
  }

  @Test
  public void signUp_MUST_not_send_an_email_WHEN_user_was_not_created() throws Exception {
    callSignUpAction(null, JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
    assertNull(MockEmailProvider.lastSentEmail);
  }

  @Test(expected = NoResultException.class)
  public void signUp_MUST_not_persist_the_user_WHEN_sending_of_the_email_fails() throws Exception {
    MockEmailProvider.nestedMailer = new ExceptionThrowingMailer();
    try {
      callSignUpAction(JOHN_SMITH, JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
    } catch (Exception e) {}
    new TestPlayUserRepository().findUserByEmail(JOHN_SMITH_EMAIL);
  }

  @Test
  public void signUp_MUST_result_in_badRequest_WHEN_user_with_the_same_name_is_added() throws Exception {
    callSignUpAction(JOHN_SMITH, JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
    Result result = callSignUpAction(JOHN_SMITH, JANE_DOE_EMAIL,
      JOHN_SMITH_PASSWORD);
    assertEquals(BAD_REQUEST, status(result));
  }

  @Test
  public void signUp_MUST_result_in_badRequest_WHEN_user_with_the_same_email_is_added() throws Exception {
    callSignUpAction(JANE_DOE, JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
    Result result = callSignUpAction(JOHN_SMITH, JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
    assertEquals(BAD_REQUEST, status(result));
  }

  private void assertJohnSmithIsInDb() {
    User createdUser = new TestPlayUserRepository().findUserByEmail(JOHN_SMITH_EMAIL);
    long idOfNewUser = 1L;
    User expectedUser = createJohnSmithUser(idOfNewUser, createdUser.getSalt());
    assertEquals(expectedUser, createdUser);
  }

  private static User createJohnSmithUser(long userId, byte[] passwordSalt) {
    SaltedHashedPassword password = new SaltedHashedPassword(
      JOHN_SMITH_PASSWORD,
      passwordSalt);
    JpaUser user = new JpaUser(JOHN_SMITH, JOHN_SMITH_EMAIL, password);
    return user.withId(userId);
  }

  public static Result callSignUpAction(String name, String email,
                                        String password) {
    return callAction(UserController.signUp(name, email, password));
  }

  public static void createTestUser() {
    callSignUpAction(JOHN_SMITH, JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
  }
}
