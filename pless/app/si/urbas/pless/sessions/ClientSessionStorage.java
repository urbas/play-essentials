package si.urbas.pless.sessions;

import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.Factory;
import si.urbas.pless.util.SingletonFactory;

import static si.urbas.pless.util.ConfigurationSource.getConfigurationSource;

public abstract class ClientSessionStorage {

  public static final String CONFIG_CLIENT_SESSION_STORAGE_FACTORY = "pless.clientSessionStorageFactory";

  public static ClientSessionStorage getClientSessionStorage() {
    return ClientSessionStorageSingleton.INSTANCE
      .createInstance(getConfigurationSource());
  }

  public abstract void put(String key, String value);

  public abstract void remove(String key);

  public abstract String get(String key);

  static final class ClientSessionStorageSingleton {
    public static final Factory<ClientSessionStorage> INSTANCE = new SingletonFactory<>(CONFIG_CLIENT_SESSION_STORAGE_FACTORY, new DefaultClientSessionStorageFactory());
  }

  static final class DefaultClientSessionStorageFactory implements Factory<ClientSessionStorage>
  {
    @Override
    public ClientSessionStorage createInstance(ConfigurationSource configurationSource) {
      return new PlayHttpContextClientSessionStorage();
    }
  }
}
