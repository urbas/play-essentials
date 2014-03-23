package com.pless.test;

import static com.pless.authentication.PlayClientSessionStorage.CONFIG_CLIENT_SESSION_STORAGE_FACTORY;
import static com.pless.test.TestConfigurationUtils.setConfigurationClass;
import static com.pless.test.TestClientSessionStorage.currentClientSessionStorage;
import static org.mockito.Mockito.mock;

import com.pless.authentication.ClientSessionStorage;

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
