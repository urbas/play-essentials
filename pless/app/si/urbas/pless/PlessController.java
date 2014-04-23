package si.urbas.pless;

import play.mvc.Controller;
import si.urbas.pless.authentication.AuthenticationService;
import si.urbas.pless.emailing.PlessEmailing;
import si.urbas.pless.users.UserRepository;
import si.urbas.pless.util.ConfigurationSource;

public class PlessController extends Controller {
  protected static UserRepository users() {
    return UserRepository.getUserRepository();
  }

  protected static AuthenticationService auth() {
    return AuthenticationService.getAuthenticationService();
  }

  protected static ConfigurationSource config() {
    return ConfigurationSource.getConfigurationSource();
  }

  protected static PlessEmailing emailing() {
    return PlessEmailing.getEmailing();
  }
}
