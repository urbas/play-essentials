package si.urbas.pless.authentication;

import static si.urbas.pless.util.PlessConfigurationSource.getConfigurationSource;

import si.urbas.pless.util.*;

public class PlessServerSessionStorage {

  public static final String CONFIG_SERVER_SESSION_STORAGE_FACTORY = "pless.serverSessionStorageFactory";

  public static ServerSessionStorage getServerSessionStorage() {
    return ServerSessionStorageSingleton.INSTANCE
      .createInstance(getConfigurationSource());
  }

  static class ServerSessionStorageSingleton {
    private static final SingletonFactory<ServerSessionStorage> INSTANCE = new SingletonFactory<>(CONFIG_SERVER_SESSION_STORAGE_FACTORY, new DefaultSessionStorageFactory());
  }

  static final class DefaultSessionStorageFactory implements Factory<ServerSessionStorage>
  {
    @Override
    public ServerSessionStorage createInstance(ConfigurationSource configurationSource) {
      return new PlayCacheServerSessionStorage();
    }
  }
}
