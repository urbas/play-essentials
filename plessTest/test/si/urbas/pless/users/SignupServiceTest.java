package si.urbas.pless.users;

import org.junit.Test;
import si.urbas.pless.test.util.PlessTest;
import si.urbas.pless.users.SignupService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static si.urbas.pless.users.SignupService.signupService;
import static si.urbas.pless.test.util.ScopedServices.withService;

public class SignupServiceTest extends PlessTest {

  private final SignupService signupService = new SignupService();

  @Test
  public void signupService_MUST_return_the_default_instance_WHEN_not_configured() {
    assertSame(SignupService.class, signupService().getClass());
  }

  @Test
  public void signupService_MUST_return_the_configured_service() {
    withService(signupService, () -> assertEquals(signupService, signupService()));
  }

}