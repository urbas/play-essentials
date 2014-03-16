package com.pless.authentication;

import com.pless.util.ConfigurationSource;

public interface ServerSessionStorageFactory {

  ServerSessionStorage createStorage(ConfigurationSource instance);

}
