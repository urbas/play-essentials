package si.urbas.pless;

import play.mvc.Controller;
import si.urbas.pless.authentication.AuthenticationService;
import si.urbas.pless.authentication.LoggedInUserInfo;
import si.urbas.pless.emailing.EmailProvider;
import si.urbas.pless.users.PlessUser;
import si.urbas.pless.users.UserRepository;
import si.urbas.pless.util.ConfigurationSource;

public class PlessController extends Controller {

  protected static UserRepository users() {
    return UserRepository.getUserRepository();
  }

  protected static AuthenticationService auth() {
    return AuthenticationService.authenticationService();
  }

  protected static ConfigurationSource config() {
    return ConfigurationSource.getConfigurationSource();
  }

  protected static EmailProvider emailing() {
    return EmailProvider.getEmailProvider();
  }

  /**
   * Fetches the logged in user from the user repository.
   *
   * <p><b>Note</b>: use the {@link si.urbas.pless.authentication.AuthenticationService#getLoggedInUserInfo()} method in
   * {@link PlessController#auth()} if you simply want to check whether any user is logged in.</p>
   *
   * @return {@code null} if no user is logged in.
   */
  protected static PlessUser loggedInUser() {
    LoggedInUserInfo loggedInUserInfo = auth().getLoggedInUserInfo();
    return loggedInUserInfo == null ? null : users().findUserById(loggedInUserInfo.userId);
  }
}
