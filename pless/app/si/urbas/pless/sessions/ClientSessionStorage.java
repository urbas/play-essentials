package si.urbas.pless.sessions;

import si.urbas.pless.PlessService;
import si.urbas.pless.util.PlessServiceConfigKey;
import si.urbas.pless.util.ServiceLoader;

@PlessServiceConfigKey(ClientSessionStorage.CONFIG_CLIENT_SESSION_STORAGE_FACTORY)
public abstract class ClientSessionStorage implements PlessService {

  public static final String CONFIG_CLIENT_SESSION_STORAGE_FACTORY = "pless.clientSessionStorageFactory";

  public static ClientSessionStorage getClientSessionStorage() {
    return ClientSessionStorageServiceLoader.INSTANCE.getService();
  }

  public abstract void put(String key, String value);

  public abstract void remove(String key);

  public abstract String get(String key);

  static final class ClientSessionStorageServiceLoader {
    public static final ServiceLoader<ClientSessionStorage> INSTANCE = new ServiceLoader<>(CONFIG_CLIENT_SESSION_STORAGE_FACTORY, PlayHttpContextClientSessionStorage::new);
  }

}
