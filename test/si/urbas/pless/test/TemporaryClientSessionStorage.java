package si.urbas.pless.test;

import static si.urbas.pless.authentication.PlessClientSessionStorage.CONFIG_CLIENT_SESSION_STORAGE_FACTORY;
import static si.urbas.pless.test.TestClientSessionStorage.currentClientSessionStorage;
import static si.urbas.pless.test.TestConfigurationUtils.setConfigurationClass;
import static org.mockito.Mockito.mock;

import si.urbas.pless.authentication.ClientSessionStorage;

public class TemporaryClientSessionStorage implements AutoCloseable {

  private final ClientSessionStorage oldClientSessionStorage = currentClientSessionStorage;
  
  public TemporaryClientSessionStorage() {
    currentClientSessionStorage = mock(ClientSessionStorage.class);
    setConfigurationClass(CONFIG_CLIENT_SESSION_STORAGE_FACTORY, TestClientSessionStorage.class);
  }

  @Override
  public void close() throws Exception {
    currentClientSessionStorage = oldClientSessionStorage;
  }

}
