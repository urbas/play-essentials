package si.urbas.pless.test.sessions;

import si.urbas.pless.sessions.ClientSessionStorage;

import static org.mockito.Mockito.mock;
import static si.urbas.pless.sessions.PlessClientSessionStorage.CONFIG_CLIENT_SESSION_STORAGE_FACTORY;
import static si.urbas.pless.test.util.TestConfigurationUtils.setConfigurationClass;

public class TemporaryClientSessionStorage implements AutoCloseable {

  private final ClientSessionStorage oldClientSessionStorage = TestClientSessionStorageFactory.currentClientSessionStorage;

  public TemporaryClientSessionStorage() {
    this(mock(ClientSessionStorage.class));
  }

  public TemporaryClientSessionStorage(ClientSessionStorage clientSessionStorage) {
    TestClientSessionStorageFactory.currentClientSessionStorage = clientSessionStorage;
    setConfigurationClass(CONFIG_CLIENT_SESSION_STORAGE_FACTORY, TestClientSessionStorageFactory.class);
  }

  @Override
  public void close() throws Exception {
    TestClientSessionStorageFactory.currentClientSessionStorage = oldClientSessionStorage;
  }

}
