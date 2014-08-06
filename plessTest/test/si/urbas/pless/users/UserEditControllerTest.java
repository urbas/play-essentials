package si.urbas.pless.users;

import org.junit.Before;
import org.junit.Test;
import play.data.Form;
import play.mvc.Result;
import si.urbas.pless.test.util.PlessTest;

import java.util.function.Supplier;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static si.urbas.pless.test.matchers.UserMatchers.userWith;
import static si.urbas.pless.users.UserEditController.persistEditedUser;
import static si.urbas.pless.users.UserEditService.userEditService;
import static si.urbas.pless.users.UserRepository.userRepository;

public class UserEditControllerTest extends PlessTest {

  private static final String JOHN_SMITH_EMAIL = "john.smith.email";
  private static final String JOHN_SMITH_USERNAME = "john smith username";
  private static final String JOHN_SMITH_PASSWORD = "john.smith.password";
  private static final String MARK_TWAIN_EMAIL = "mark.twain.email";
  private static final String MARK_TWAIN_USERNAME = "mark.twain.username";
  private static final String MARK_TWAIN_PASSWORD = "mark.twain.password";
  private
  Supplier<Result> formInvalidCallback;
  private Form userEditForm;
  private Supplier<Result> persistErrorCallback;
  private Supplier<Result> successfulCallback;

  @Override
  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    super.setUp();
    formInvalidCallback = mock(Supplier.class);
    persistErrorCallback = mock(Supplier.class);
    successfulCallback = mock(Supplier.class);
    userEditForm = mock(Form.class);
    UserEditService userEditService = userEditService();
    doReturn(true).when(userEditService).isUserEditFormValid(eq(userEditForm));
    when(userEditForm.get()).thenReturn(new UserEditData(MARK_TWAIN_EMAIL, MARK_TWAIN_USERNAME, MARK_TWAIN_PASSWORD));
  }

  @Test
  public void persistEditedUser_MUST_call_the_invalid_form_callback_WhEN_the_form_has_errors() {
    when(userEditForm.hasErrors()).thenReturn(true);
    persistEditedUser(0L, userEditForm, null, formInvalidCallback, null);
    verify(formInvalidCallback).get();
  }

  @Test
  public void persistEditedUser_MUST_return_the_result_of_the_invalid_form_callback_WhEN_the_form_has_errors() {
    when(userEditForm.hasErrors()).thenReturn(true);
    Result expectedResult = mock(Result.class);
    when(formInvalidCallback.get()).thenReturn(expectedResult);
    assertSame(expectedResult, persistEditedUser(0L, userEditForm, null, formInvalidCallback, null));
  }

  @Test
  public void persistEditedUser_MUST_call_the_invalid_form_callback_WhEN_the_user_edit_service_reports_invalid_form() {
    UserEditService userEditService = userEditService();
    doReturn(false).when(userEditService).isUserEditFormValid(eq(userEditForm));
    persistEditedUser(0L, userEditForm, null, formInvalidCallback, null);
    verify(formInvalidCallback).get();
  }

  @Test
  public void persistEditedUser_MUST_call_the_persist_error_callback_WhEN_the_edited_user_does_not_exist() {
    persistEditedUser(0L, userEditForm, null, null, persistErrorCallback);
    verify(persistErrorCallback).get();
  }

  @Test
  public void persistEditedUser_MUST_call_the_successful_callback_WhEN_the_edited_user_exists() {
    PlessUser user = signUpAndLoginUser(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    persistEditedUser(user.getId(), userEditForm, successfulCallback, null, null);
    verify(successfulCallback).get();
  }

  @Test
  public void persistEditedUser_MUST_edit_the_user() {
    PlessUser user = signUpAndLoginUser(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    persistEditedUser(user.getId(), userEditForm, successfulCallback, null, null);
    assertThat(
      userRepository().findUserById(user.getId()),
      is(userWith(MARK_TWAIN_EMAIL, MARK_TWAIN_USERNAME, MARK_TWAIN_PASSWORD))
    );
  }

  @Test
  public void persistEditedUser_MUST_not_change_the_password_WHEN_the_new_password_is_null() {
    PlessUser user = signUpAndLoginUser(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    when(userEditForm.get()).thenReturn(new UserEditData(MARK_TWAIN_EMAIL, MARK_TWAIN_USERNAME, null));
    persistEditedUser(user.getId(), userEditForm, successfulCallback, null, null);
    assertThat(
      userRepository().findUserById(user.getId()),
      is(userWith(MARK_TWAIN_EMAIL, MARK_TWAIN_USERNAME, JOHN_SMITH_PASSWORD))
    );
  }

  @Test
  public void persistEditedUser_MUST_not_change_the_password_WHEN_the_new_password_is_empty() {
    PlessUser user = signUpAndLoginUser(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    when(userEditForm.get()).thenReturn(new UserEditData(MARK_TWAIN_EMAIL, MARK_TWAIN_USERNAME, ""));
    persistEditedUser(user.getId(), userEditForm, successfulCallback, null, null);
    assertThat(
      userRepository().findUserById(user.getId()),
      is(userWith(MARK_TWAIN_EMAIL, MARK_TWAIN_USERNAME, JOHN_SMITH_PASSWORD))
    );
  }

}