package si.urbas.pless;

import play.mvc.Controller;
import si.urbas.pless.authentication.AuthenticationService;
import si.urbas.pless.authentication.PlessAuthentication;
import si.urbas.pless.emailing.PlessEmailing;
import si.urbas.pless.users.PlessUserRepository;
import si.urbas.pless.users.UserRepository;
import si.urbas.pless.util.Callback;
import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.Function;
import si.urbas.pless.util.PlessConfigurationSource;

import javax.persistence.EntityManager;

import static si.urbas.pless.db.PlessTransactions.getTransactionProvider;

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

  protected static void withTransaction(Callback<EntityManager> callback) {
    getTransactionProvider().withTransaction(callback);
  }

  protected static <T> T withTransaction(Function<EntityManager, T> transactionFunction) {
    return getTransactionProvider().withTransaction(transactionFunction);
  }

  protected static <T> T usingDb(Function<EntityManager, T> transactionFunction) {
    return getTransactionProvider().usingDb(transactionFunction);
  }
}
