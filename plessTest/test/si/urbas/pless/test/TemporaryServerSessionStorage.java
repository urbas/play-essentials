package si.urbas.pless.test;

import si.urbas.pless.authentication.ServerSessionStorage;

import static org.mockito.Mockito.mock;
import static si.urbas.pless.authentication.PlessServerSessionStorage.CONFIG_SERVER_SESSION_STORAGE_FACTORY;
import static si.urbas.pless.test.util.TestConfigurationUtils.setConfigurationClass;
import static si.urbas.pless.test.TestServerSessionStorage.currentServerSessionStorage;

public class TemporaryServerSessionStorage implements AutoCloseable {

  private final ServerSessionStorage oldServerSessionStorage = currentServerSessionStorage;

  public TemporaryServerSessionStorage() {
    this(mock(ServerSessionStorage.class));
  }

  public TemporaryServerSessionStorage(ServerSessionStorage newServerSessionStorage) {
    currentServerSessionStorage = newServerSessionStorage;
    setConfigurationClass(CONFIG_SERVER_SESSION_STORAGE_FACTORY, TestServerSessionStorage.class);
  }

  @Override
  public void close() throws Exception {
    currentServerSessionStorage = oldServerSessionStorage;
  }

}
