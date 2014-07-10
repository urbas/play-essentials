package si.urbas.pless.helpers;

import org.junit.Before;
import org.junit.Test;
import play.mvc.Result;
import si.urbas.pless.authentication.LoggedInUserInfo;
import si.urbas.pless.test.TemporaryHttpContext;
import si.urbas.pless.test.util.PlessTest;
import si.urbas.pless.util.Supplier;

import java.util.function.Function;

import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static si.urbas.pless.helpers.ApiActionHelper.USER_NOT_LOGGED_IN_RESULT;
import static si.urbas.pless.helpers.ApiActionHelper.withAuthenticatedUser;

public class ApiActionHelperTest extends PlessTest {

  private static final String JOHN_SMITH_EMAIL = "john.smith@email.com";
  private static final String JOHN_SMITH_USERNAME = "john smith username";
  private static final String JOHN_SMITH_PASSWORD = "john smith password";
  private Supplier<Object> notAuthenticatedCallback;
  private Function<LoggedInUserInfo, Object> authenticatedCallback;
  private Function<LoggedInUserInfo, Result> authenticatedResultCallback;
  private TemporaryHttpContext temporaryHttpContext;

  @Override
  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    super.setUp();
    notAuthenticatedCallback = mock(Supplier.class);
    authenticatedCallback = mock(Function.class);
    authenticatedResultCallback = mock(Function.class);
  }

  @Test
  public void withAuthenticatedUser_MUST_not_call_the_authenticated_callback_WHEN_the_user_is_not_logged_in() {
    withAuthenticatedUser(authenticatedCallback, notAuthenticatedCallback);
    verifyZeroInteractions(authenticatedCallback);
  }

  @Test
  public void withAuthenticatedUser_MUST_call_the_authenticated_callback_WHEN_the_user_is_logged_in() {
    signUpAndLoginUser(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    withAuthenticatedUser(authenticatedCallback, notAuthenticatedCallback);
    verify(authenticatedCallback).apply(any(LoggedInUserInfo.class));
  }

  @Test
  public void withAuthenticatedUser_MUST_call_the_not_authenticated_callback_WHEN_the_user_is_not_logged_in() {
    withAuthenticatedUser(authenticatedCallback, notAuthenticatedCallback);
    verify(notAuthenticatedCallback).get();
  }

  @Test
  public void withAuthenticatedUser_MUST_return_the_value_of_the_not_authenticated_callback_WHEN_the_user_is_not_logged_in() {
    Object expectedValue = new Object();
    when(notAuthenticatedCallback.get()).thenReturn(expectedValue);
    assertSame(expectedValue, withAuthenticatedUser(authenticatedCallback, notAuthenticatedCallback));
  }

  @Test
  public void withAuthenticatedUser_MUST_return_the_value_of_the_authenticated_callback_WHEN_the_user_is_logged_in() {
    signUpAndLoginUser(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    Object expectedValue = new Object();
    when(authenticatedCallback.apply(any(LoggedInUserInfo.class))).thenReturn(expectedValue);
    assertSame(expectedValue, withAuthenticatedUser(authenticatedCallback, notAuthenticatedCallback));
  }

  @Test
  public void withAuthenticatedUser_MUST_return_the_standard_error_WHEN_the_user_is_not_logged_in() {
    assertSame(USER_NOT_LOGGED_IN_RESULT, withAuthenticatedUser(authenticatedResultCallback));
  }

}