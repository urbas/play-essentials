package si.urbas.pless.test;

import si.urbas.pless.users.UserRepository;
import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.Factory;

public class TestUserRepository implements Factory<UserRepository> {

  public static UserRepository currentUserRepository;

  @Override
  public UserRepository createInstance(ConfigurationSource configurationSource) {
    return currentUserRepository;
  }

}
