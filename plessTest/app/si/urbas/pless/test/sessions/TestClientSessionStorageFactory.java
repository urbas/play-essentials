package si.urbas.pless.test.sessions;

import si.urbas.pless.sessions.ClientSessionStorage;
import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.Factory;


public class TestClientSessionStorageFactory implements Factory<ClientSessionStorage> {
  public static ClientSessionStorage currentClientSessionStorage;

  @Override
  public ClientSessionStorage createInstance(ConfigurationSource configurationSource) {
    return currentClientSessionStorage;
  }
}
