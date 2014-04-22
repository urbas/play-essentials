package si.urbas.pless.sessions;

import static si.urbas.pless.util.PlessConfigurationSource.getConfigurationSource;

import si.urbas.pless.util.*;

public class PlessClientSessionStorage {

  public static final String CONFIG_CLIENT_SESSION_STORAGE_FACTORY = "pless.clientSessionStorageFactory";

  public static ClientSessionStorage getClientSessionStorage() {
    return ClientSessionStorageSingleton.INSTANCE
      .createInstance(getConfigurationSource());
  }

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
