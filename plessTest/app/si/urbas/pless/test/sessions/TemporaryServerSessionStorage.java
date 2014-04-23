package si.urbas.pless.test.sessions;

import si.urbas.pless.sessions.ServerSessionStorage;

import static org.mockito.Mockito.mock;
import static si.urbas.pless.sessions.ServerSessionStorage.CONFIG_SERVER_SESSION_STORAGE_FACTORY;
import static si.urbas.pless.test.util.TestConfigurationUtils.setConfigurationClass;

public class TemporaryServerSessionStorage implements AutoCloseable {

  private final ServerSessionStorage oldServerSessionStorage = TestServerSessionStorage.currentServerSessionStorage;

  public TemporaryServerSessionStorage() {
    this(mock(ServerSessionStorage.class));
  }

  public TemporaryServerSessionStorage(ServerSessionStorage newServerSessionStorage) {
    TestServerSessionStorage.currentServerSessionStorage = newServerSessionStorage;
    setConfigurationClass(CONFIG_SERVER_SESSION_STORAGE_FACTORY, TestServerSessionStorage.class);
  }

  @Override
  public void close() throws Exception {
    TestServerSessionStorage.currentServerSessionStorage = oldServerSessionStorage;
  }

}
