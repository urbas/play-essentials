package si.urbas.pless.users;

import org.junit.Test;
import si.urbas.pless.test.util.PlessTest;

import static org.junit.Assert.assertEquals;
import static si.urbas.pless.test.util.ScopedServices.withService;
import static si.urbas.pless.users.SignupService.signupService;

public class SignupServiceTest extends PlessTest {

  private final SignupService signupService = new SignupService();

  @Test
  public void signupService_MUST_return_the_configured_service() {
    withService(signupService, () -> assertEquals(signupService, signupService()));
  }

}