package com.pless.users;

import com.pless.util.ConfigurationSource;

public interface UserRepositoryFactory {

  UserRepository createUserRepository(ConfigurationSource instance);

}
