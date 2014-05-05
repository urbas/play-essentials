package si.urbas.pless.users;

import si.urbas.pless.ConfigurationException;
import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.Factory;
import si.urbas.pless.util.SingletonFactory;

import java.util.List;

import static si.urbas.pless.util.ConfigurationSource.getConfigurationSource;

public abstract class UserRepository {

  public static final String CONFIG_USER_REPOSITORY = "pless.userRepositoryFactory";

  public static UserRepository getUserRepository() {
    return UserRepositorySingleton.INSTANCE.createInstance(getConfigurationSource());
  }

  public abstract PlessUser findUserByEmail(String email);

  public abstract List<PlessUser> getAllUsers();

  public abstract void persistUser(String email, String username, String password);

  public abstract void persistUser(PlessUser user);

  public abstract boolean activateUser(String userEmail, String activationCode);

  public abstract boolean delete(String userEmail);

  public abstract PlessUser findUserById(long userId);

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