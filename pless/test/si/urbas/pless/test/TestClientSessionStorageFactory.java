package si.urbas.pless.test;

import si.urbas.pless.authentication.ClientSessionStorage;
import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.Factory;


public class TestClientSessionStorageFactory implements Factory<ClientSessionStorage> {
  public static ClientSessionStorage currentClientSessionStorage;

  @Override
  public ClientSessionStorage createInstance(ConfigurationSource configurationSource) {
    return currentClientSessionStorage;
  }
}
