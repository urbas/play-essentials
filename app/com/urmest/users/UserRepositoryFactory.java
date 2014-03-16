package com.urmest.users;

import com.urmest.util.ConfigurationSource;

public interface UserRepositoryFactory {

  UserRepository createUserRepository(ConfigurationSource instance);

}
