package com.pless.test;

import static com.pless.authentication.PlayServerSessionStorage.CONFIG_SERVER_SESSION_STORAGE_FACTORY;
import static com.pless.test.TestConfigurationUtils.setConfigurationClass;
import static com.pless.test.TestServerSessionStorage.currentServerSessionStorage;
import static org.mockito.Mockito.mock;

import com.pless.authentication.ServerSessionStorage;

public class TemporaryServerSessionStorage implements AutoCloseable {
  
  private final ServerSessionStorage oldServerSessionStorage = currentServerSessionStorage;
  
  public TemporaryServerSessionStorage() {
    currentServerSessionStorage = mock(ServerSessionStorage.class);
    setConfigurationClass(CONFIG_SERVER_SESSION_STORAGE_FACTORY, TestServerSessionStorage.class);
  }

  @Override
  public void close() throws Exception {
    currentServerSessionStorage = oldServerSessionStorage;
  }

}
