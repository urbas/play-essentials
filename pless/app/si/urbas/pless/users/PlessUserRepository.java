package si.urbas.pless.users;

import static si.urbas.pless.util.ConfigurationSource.getConfigurationSource;

import si.urbas.pless.util.*;

public class PlessUserRepository {

  public static final String CONFIG_USER_REPOSITORY = "pless.userRepositoryFactory";

  public static UserRepository getUserRepository() {
    return PlessUserRepositorySingleton.INSTANCE.createInstance(getConfigurationSource());
  }

  static class PlessUserRepositorySingleton {
    private static final SingletonFactory<UserRepository> INSTANCE = new SingletonFactory<>(CONFIG_USER_REPOSITORY, new DefaultUserRepositoryCreator());
  }

  public static class DefaultUserRepositoryCreator implements Factory<UserRepository> {
    @Override
    public UserRepository createInstance(ConfigurationSource configurationSource) {
      return new PlessJpaUserRepository();
    }
  }
}
