package com.pless.authentication;

import com.pless.util.*;

public class PlessClientSessionStorage {

  public static ClientSessionStorage getClientSessionStorage() {
    return PlessFactories
      .getFactories()
      .createInstance(
        PlayHttpContextClientSessionStorage.CONFIG_CLIENT_SESSION_STORAGE_FACTORY,
        new PlessClientSessionStorage.DefaultClientSessionStorageFactory());
  }

  static final class DefaultClientSessionStorageFactory implements Factory<ClientSessionStorage>
  {
    @Override
    public ClientSessionStorage createInstance(ConfigurationSource configurationSource) {
      return new PlayHttpContextClientSessionStorage();
    }
  }

}
