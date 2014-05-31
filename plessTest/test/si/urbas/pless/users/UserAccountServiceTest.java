package si.urbas.pless.users;

import org.junit.Test;
import play.data.Form;
import si.urbas.pless.test.util.PlessTest;
import si.urbas.pless.util.TemporaryService;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.verify;
import static si.urbas.pless.users.UserAccountService.UserAccountServiceLoader;
import static si.urbas.pless.users.UserAccountService.getUserAccountService;
import static si.urbas.pless.users.UserController.EMAIL_PARAMETER;
import static si.urbas.pless.users.UserController.PASSWORD_PARAMETER;
import static si.urbas.pless.users.UserController.USERNAME_PARAMETER;
import static si.urbas.pless.users.UserRepository.getUserRepository;
import static si.urbas.pless.util.RequestParameters.param;
import static si.urbas.pless.util.RequestParameters.params;

public class UserAccountServiceTest extends PlessTest {

  private static final String JOHN_SMITH_EMAIL = "john@example.com";
  private static final String JOHN_SMITH_USERNAME = "John Smith";
  private static final String JOHN_SMITH_PASSWORD = "john's password";
  @SuppressWarnings("UnusedDeclaration")
  private final UserAccountServiceLoader userAccountServiceLoader = new UserAccountServiceLoader();
  private final UserAccountService userAccountService = new UserAccountService();
  private final HashMap<String, String[]> updateAccountParams = params(
    param(EMAIL_PARAMETER, JOHN_SMITH_EMAIL),
    param(USERNAME_PARAMETER, JOHN_SMITH_USERNAME),
    param(PASSWORD_PARAMETER, JOHN_SMITH_PASSWORD)
  );
  private final Form<?> updateAccountForm = userAccountService.getAccountUpdateForm().bindFromRequest(updateAccountParams);

  @Test
  public void getUserAccountService_MUST_return_the_default_implementation_WHEN_not_configured() {
    assertEquals(UserAccountService.class, getUserAccountService().getClass());
  }

  @Test
  public void getAccountUpdateForm_MUST_return_a_form_THAT_validates_correct_data() {
    assertFalse(updateAccountForm.hasErrors());
  }

  @Test
  public void createUpdatedUser_MUST_create_the_user_with_the_given_details() {
    userAccountService.createUpdatedUser(updateAccountForm);
    verify(getUserRepository()).createUser(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
  }

  @Test
  public void getUserAccountService_MUST_return_the_configured_implementation() {
      try (TemporaryService userAccountService = new TemporaryService(UserAccountService.CONFIG_USER_ACCOUNT_SERVICE, this.userAccountService)) {
        assertEquals(userAccountService.serviceInstance, getUserAccountService());
      }
  }

}