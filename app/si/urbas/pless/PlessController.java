package si.urbas.pless;

import play.mvc.Controller;
import si.urbas.pless.authentication.AuthenticationService;
import si.urbas.pless.authentication.PlessAuthentication;
import si.urbas.pless.emailing.PlessEmailing;
import si.urbas.pless.users.PlessUserRepository;
import si.urbas.pless.users.UserRepository;
import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.PlessConfigurationSource;

public class PlessController extends Controller {

  protected static UserRepository users() {
    return PlessUserRepository.getUserRepository();
  }

  protected static AuthenticationService auth() {
    return PlessAuthentication.getAuthenticationService();
  }

  protected static ConfigurationSource config() {
    return PlessConfigurationSource.getConfigurationSource();
  }

  protected static PlessEmailing emailing() {
    return PlessEmailing.getEmailing();
  }
}
