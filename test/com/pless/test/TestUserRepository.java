package com.pless.test;

import com.pless.users.UserRepository;
import com.pless.util.ConfigurationSource;
import com.pless.util.Factory;

public class TestUserRepository implements Factory<UserRepository> {

  public static UserRepository currentUserRepository;

  @Override
  public UserRepository createInstance(ConfigurationSource configurationSource) {
    return currentUserRepository;
  }

}
