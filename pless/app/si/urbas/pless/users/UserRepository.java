package si.urbas.pless.users;

import si.urbas.pless.ConfigurationException;
import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.Factory;
import si.urbas.pless.util.SingletonFactory;
import si.urbas.pless.util.StringUtils;

import java.util.List;

import static si.urbas.pless.util.ConfigurationSource.getConfigurationSource;

public abstract class UserRepository {

  public static final String CONFIG_USER_REPOSITORY = "pless.userRepositoryFactory";

  /**
   * @return a new user that is not yet persisted.
   */
  public abstract PlessUser createUser(String email, String username, String password);

  public abstract void persistUser(PlessUser user);

  public abstract PlessUser findUserByEmail(String email);

  public abstract PlessUser findUserById(long userId);

  public abstract List<PlessUser> getAllUsers();

  public abstract boolean activateUser(String userEmail, String activationCode);

  public abstract boolean delete(String userEmail);

  public abstract boolean setUsername(long userId, String username);

  protected void throwPersistUserException(String message, PlessUser user) {
    StringBuilder exceptionMessage = new StringBuilder("Cannot persist the user '" + user + "'.");
    if (!StringUtils.isNullOrEmpty(message)) {
      exceptionMessage.append(" Reason: ").append(message);
    }
    throw new RuntimeException(exceptionMessage.toString());
  }

  public static UserRepository getUserRepository() {
    return UserRepositorySingleton.INSTANCE.createInstance(getConfigurationSource());
  }

  static class UserRepositorySingleton {
    private static final SingletonFactory<UserRepository> INSTANCE = new SingletonFactory<>(CONFIG_USER_REPOSITORY, new DefaultUserRepositoryCreator());
  }

  public static class DefaultUserRepositoryCreator implements Factory<UserRepository> {
    @Override
    public UserRepository createInstance(ConfigurationSource configurationSource) {
      throw new ConfigurationException("No user repository configured. Please select an implementation of the user repository and add it to your configuration. " + ConfigurationException.getFactoryConfigurationInstruction(CONFIG_USER_REPOSITORY, UserRepository.class));
    }
  }
}