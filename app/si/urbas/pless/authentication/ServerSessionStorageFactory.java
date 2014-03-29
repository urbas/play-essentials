package si.urbas.pless.authentication;

import si.urbas.pless.util.ConfigurationSource;

public interface ServerSessionStorageFactory {

  ServerSessionStorage createStorage(ConfigurationSource instance);

}
