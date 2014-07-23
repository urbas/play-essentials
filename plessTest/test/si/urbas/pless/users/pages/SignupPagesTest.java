package si.urbas.pless.users.pages;

import org.junit.Test;
import si.urbas.pless.test.util.PlessTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static si.urbas.pless.users.pages.SignupPages.signupPages;
import static si.urbas.pless.test.util.ScopedServices.withService;

public class SignupPagesTest extends PlessTest {

  private final SignupPages signupPages = new SignupPages();

  @Test
  public void signupPages_MUST_return_the_default_instance_WHEN_not_configured() {
    assertSame(SignupPages.class, signupPages().getClass());
  }

  @Test
  public void signupPages_MUST_return_the_configured_service() {
    withService(signupPages, () -> assertEquals(signupPages, signupPages()));
  }

}