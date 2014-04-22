package si.urbas.pless.test;

import static si.urbas.pless.authentication.PlessClientSessionStorage.CONFIG_CLIENT_SESSION_STORAGE_FACTORY;
import static si.urbas.pless.test.TestClientSessionStorageFactory.currentClientSessionStorage;
import static si.urbas.pless.test.util.TestConfigurationUtils.setConfigurationClass;
import static org.mockito.Mockito.mock;

import si.urbas.pless.authentication.ClientSessionStorage;

public class TemporaryClientSessionStorage implements AutoCloseable {

  private final ClientSessionStorage oldClientSessionStorage = currentClientSessionStorage;

  public TemporaryClientSessionStorage() {
    this(mock(ClientSessionStorage.class));
  }

  public TemporaryClientSessionStorage(ClientSessionStorage clientSessionStorage) {
    currentClientSessionStorage = clientSessionStorage;
    setConfigurationClass(CONFIG_CLIENT_SESSION_STORAGE_FACTORY, TestClientSessionStorageFactory.class);
  }

  @Override
  public void close() throws Exception {
    currentClientSessionStorage = oldClientSessionStorage;
  }

}
