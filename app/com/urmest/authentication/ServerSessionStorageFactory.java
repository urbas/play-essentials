package com.urmest.authentication;

import com.urmest.util.ConfigurationSource;

public interface ServerSessionStorageFactory {

  ServerSessionStorage createStorage(ConfigurationSource instance);

}
