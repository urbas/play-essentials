package si.urbas.pless.test.users;

import si.urbas.pless.users.UserRepository;
import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.Factory;

public class TestUserRepositoryFactory implements Factory<UserRepository> {

  public static UserRepository currentUserRepository;

  @Override
  public UserRepository createInstance(ConfigurationSource configurationSource) {
    return currentUserRepository;
  }

}
