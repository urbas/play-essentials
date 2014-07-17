package si.urbas.pless.pages;

import org.junit.Test;
import si.urbas.pless.test.util.PlessTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static si.urbas.pless.pages.SignupPages.getSignupPages;
import static si.urbas.pless.test.util.ScopedServices.withService;

public class SignupPagesTest extends PlessTest {

  private final SignupPages signupPages = new SignupPages();

  @Test
  public void getSignupPages_MUST_return_the_default_service_WHEN_not_configured() {
    assertSame(SignupPages.class, getSignupPages().getClass());
  }

  @Test
  public void getSignupPages_MUST_return_the_configured_service() {
    withService(signupPages, () -> assertEquals(signupPages, getSignupPages()));
  }

}