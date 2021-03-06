package si.urbas.pless.users;

import si.urbas.pless.ConfigurationException;
import si.urbas.pless.PlessService;
import si.urbas.pless.util.PlessServiceConfigKey;
import si.urbas.pless.util.ServiceLoader;
import si.urbas.pless.util.StringUtils;

import java.util.List;

import static si.urbas.pless.util.ServiceLoader.createServiceLoader;

@PlessServiceConfigKey(UserRepository.CONFIG_USER_REPOSITORY)
public abstract class UserRepository implements PlessService {

  public static final String CONFIG_USER_REPOSITORY = "pless.userRepository";

  /**
   * @return a new user that is not yet persisted.
   */
  public abstract PlessUser createUser(String email, String username, String password);

  public abstract void persistUser(PlessUser user);

  public abstract PlessUser findUserByEmail(String email);

  public abstract PlessUser findUserById(long userId);

  public abstract PlessUser findUserByUsername(String username);

  public abstract List<PlessUser> getAllUsers();

  public abstract boolean activateUser(String userEmail, String activationCode);

  public abstract void mergeUser(PlessUser updatedUser);

  public abstract boolean delete(String userEmail);

  protected void throwPersistUserException(String message, PlessUser user) {
    StringBuilder exceptionMessage = new StringBuilder("Cannot persist the user '" + user + "'.");
    if (!StringUtils.isNullOrEmpty(message)) {
      exceptionMessage.append(" Reason: ").append(message);
    }
    throw new RuntimeException(exceptionMessage.toString());
  }

  public static UserRepository userRepository() {
    return UserRepositoryServiceLoader.INSTANCE.getService();
  }

  static class UserRepositoryServiceLoader {
    private static final ServiceLoader<UserRepository> INSTANCE = createServiceLoader(CONFIG_USER_REPOSITORY, () -> {
      throw new ConfigurationException("No user repository configured. Please select an implementation of the user repository and add it to your configuration. " + ConfigurationException.getServiceConfigurationInstruction(CONFIG_USER_REPOSITORY, UserRepository.class));
    });
  }

}