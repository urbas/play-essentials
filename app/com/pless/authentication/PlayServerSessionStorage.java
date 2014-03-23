package com.pless.authentication;

import com.pless.util.*;

public class PlayServerSessionStorage {

  public static final String CONFIG_SERVER_SESSION_STORAGE_FACTORY = "pless.serverSessionStorageFactory";

  public static ServerSessionStorage getServerSessionStorage() {
    return PlayFactories.getFactories().createInstance(
      CONFIG_SERVER_SESSION_STORAGE_FACTORY,
      new DefaultSessionStorageFactory());
  }

  private static final class DefaultSessionStorageFactory implements
    Factory<ServerSessionStorage>
  {
    @Override
    public ServerSessionStorage createInstance(ConfigurationSource configurationSource) {
      return new PlayCacheServerSessionStorage();
    }
  }
}
