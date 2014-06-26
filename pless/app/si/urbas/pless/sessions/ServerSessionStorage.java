package si.urbas.pless.sessions;

import si.urbas.pless.PlessService;
import si.urbas.pless.util.*;

import static si.urbas.pless.util.ServiceLoader.createServiceLoader;

@PlessServiceConfigKey(ServerSessionStorage.CONFIG_SERVER_SESSION_STORAGE_FACTORY)
public abstract class ServerSessionStorage implements PlessService {

  public static final String CONFIG_SERVER_SESSION_STORAGE_FACTORY = "pless.serverSessionStorageFactory";

  public abstract void put(String key, String value, int expirationMillis);

  public abstract String get(String key);

  public abstract void remove(String key);

  public static ServerSessionStorage getServerSessionStorage() {
    return ServerSessionStorageServiceLoader.INSTANCE.getService();
  }

  static class ServerSessionStorageServiceLoader {
    private static final ServiceLoader<ServerSessionStorage> INSTANCE = createServiceLoader(CONFIG_SERVER_SESSION_STORAGE_FACTORY, PlayCacheServerSessionStorage::new);
  }

}