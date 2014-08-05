package si.urbas.pless.users;

import org.junit.Before;
import org.junit.Test;
import play.data.Form;
import play.mvc.Result;
import si.urbas.pless.test.util.PlessTest;

import java.util.function.Supplier;

import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static si.urbas.pless.users.UserEditController.persistEditedUser;
import static si.urbas.pless.users.UserEditService.userEditService;

public class UserEditControllerTest extends PlessTest {

  private
  Supplier<Result> formInvalidCallback;
  private Form userEditForm;

  @Override
  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    super.setUp();
    formInvalidCallback = mock(Supplier.class);
    userEditForm = mock(Form.class);
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

}