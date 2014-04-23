package si.urbas.pless.sessions;

import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.Factory;
import si.urbas.pless.util.SingletonFactory;

import static si.urbas.pless.util.PlessConfigurationSource.getConfigurationSource;

public abstract class ServerSessionStorage {

  public static final String CONFIG_SERVER_SESSION_STORAGE_FACTORY = "pless.serverSessionStorageFactory";

  public static ServerSessionStorage getServerSessionStorage() {
    return ServerSessionStorageSingleton.INSTANCE
      .createInstance(getConfigurationSource());
  }

  public abstract void put(String key, String value, int expirationMillis);

  public abstract String get(String key);

  public abstract void remove(String key);

  static class ServerSessionStorageSingleton {
    private static final SingletonFactory<ServerSessionStorage> INSTANCE = new SingletonFactory<>(CONFIG_SERVER_SESSION_STORAGE_FACTORY, new DefaultSessionStorageFactory());
  }

  static final class DefaultSessionStorageFactory implements Factory<ServerSessionStorage> {
    @Override
    public ServerSessionStorage createInstance(ConfigurationSource configurationSource) {
      return new PlayCacheServerSessionStorage();
    }
  }
}