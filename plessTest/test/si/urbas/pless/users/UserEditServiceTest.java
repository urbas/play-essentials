package si.urbas.pless.users;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import play.data.Form;
import si.urbas.pless.test.TestApplication;
import si.urbas.pless.test.util.PlessTest;
import si.urbas.pless.test.util.ScopedServices;
import si.urbas.pless.util.TemporaryDefaultService;

import java.util.HashMap;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static si.urbas.pless.test.matchers.UserMatchers.userWith;
import static si.urbas.pless.users.UserEditService.userEditService;
import static si.urbas.pless.users.UserRepository.userRepository;
import static si.urbas.pless.users.api.UserController.*;
import static si.urbas.pless.util.RequestParameters.param;
import static si.urbas.pless.util.RequestParameters.params;

public class UserEditServiceTest extends PlessTest {

  private static final String JOHN_SMITH_EMAIL = "john@example.com";
  private static final String JOHN_SMITH_USERNAME = "John Smith";
  private static final String JOHN_SMITH_PASSWORD = "john's password";
  private static final String JANE_SMITH_USERNAME = "Jane Smith";
  private static final String JANE_SMITH_EMAIL = "Jane@email.com";
  private static final String JANE_SMITH_PASSWORD = "jane's password";
  private final UserEditService userEditService = new UserEditService();
  private HashMap<String, String[]> updateAccountParams;
  private PlessUser janeSmithUser;

  @Override
  @Before
  public void setUp() {
    super.setUp();
    updateAccountParams = params(
      param(EMAIL_PARAMETER, JOHN_SMITH_EMAIL),
      param(USERNAME_PARAMETER, JOHN_SMITH_USERNAME),
      param(PASSWORD_PARAMETER, JOHN_SMITH_PASSWORD)
    );
    janeSmithUser = userRepository().createUser(JANE_SMITH_EMAIL, JANE_SMITH_USERNAME, JANE_SMITH_PASSWORD);
  }

  @Test
  public void accountUpdateForm_MUST_return_a_form_THAT_validates_correct_data() {
    assertFalse(filledAccountUpdateForm().hasErrors());
  }

  @Test
  public void createUpdatedUser_MUST_create_the_user_with_the_given_details() {
    assertThat(
      userEditService.updateUser(filledAccountUpdateForm(), janeSmithUser),
      is(both(userWith(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD)).and(sameInstance(janeSmithUser)))
    );
  }

  @Test
  public void updateUser_MUST_not_update_the_username_when_it_is_not_given() {
    updateAccountParams.remove(USERNAME_PARAMETER);
    assertUpdatedUserIs(userWith(JOHN_SMITH_EMAIL, JANE_SMITH_USERNAME, JOHN_SMITH_PASSWORD));
  }

  @Test
  public void updateUser_MUST_not_update_the_email_when_it_is_not_given() {
    updateAccountParams.remove(EMAIL_PARAMETER);
    assertUpdatedUserIs(is(userWith(JANE_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD)));
  }

  @Test
  public void updateUser_MUST_not_update_the_password_when_it_is_not_given() {
    updateAccountParams.remove(PASSWORD_PARAMETER);
    assertUpdatedUserIs(is(userWith(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JANE_SMITH_PASSWORD)));
  }

  @Test
  public void accountEditService_MUST_return_the_default_implementation_WHEN_not_configured() {
    assertEquals(UserEditService.class, userEditService().getClass());
  }

  @Test
  public void accountEditService_MUST_return_the_configured_implementation() {
    ScopedServices.withDefaultService(userEditService, () -> assertEquals(userEditService, userEditService()));
  }

  @Test
  public void userEditService_MUST_return_the_same_instance_twice_WHEN_in_test_mode() {
    UserEditService expectedUserEditService = userEditService();
    assertSame(expectedUserEditService, userEditService());
  }

  private void assertUpdatedUserIs(Matcher<? super PlessUser> matcher) {
    assertThat(
      userEditService.updateUser(filledAccountUpdateForm(), janeSmithUser),
      matcher
    );
  }

  private Form<?> filledAccountUpdateForm() {return userEditService.userEditForm().bindFromRequest(updateAccountParams);}

  @Override
  protected TestApplication createTestApplication() {
    return super.createTestApplication()
      .with(new TemporaryDefaultService(UserEditService.class, null));
  }

}