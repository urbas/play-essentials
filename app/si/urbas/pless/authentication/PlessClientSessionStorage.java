package si.urbas.pless.authentication;

import static si.urbas.pless.util.PlessConfigurationSource.getConfigurationSource;

import si.urbas.pless.util.*;

public class PlessClientSessionStorage {

  public static final String CONFIG_CLIENT_SESSION_STORAGE_FACTORY = "pless.clientSessionStorageFactory";

  public static ClientSessionStorage getClientSessionStorage() {
    return Singletons.CLIENT_SESSION_STORAGE_FACTORY
      .createInstance(getConfigurationSource());
  }

  private static final class Singletons {
    public static final Factory<ClientSessionStorage> CLIENT_SESSION_STORAGE_FACTORY = new SingletonFactory<>(CONFIG_CLIENT_SESSION_STORAGE_FACTORY, new DefaultClientSessionStorageFactory());
  }

  private static final class DefaultClientSessionStorageFactory implements Factory<ClientSessionStorage>
  {
    @Override
    public ClientSessionStorage createInstance(ConfigurationSource configurationSource) {
      return new PlayHttpContextClientSessionStorage();
    }
  }

}
