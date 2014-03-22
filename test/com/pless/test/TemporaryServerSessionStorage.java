package com.pless.test;

import static com.pless.authentication.PlayServerSessionStorage.CONFIG_SERVER_SESSION_STORAGE_FACTORY;
import static com.pless.test.PlessTestConfigurationUtils.setConfigurationClass;

import com.pless.authentication.PlayServerSessionStorage;
import com.pless.authentication.TestServerSessionStorage;

public class TemporaryServerSessionStorage implements AutoCloseable {

  @Override
  public void close() throws Exception {
    setConfigurationClass(CONFIG_SERVER_SESSION_STORAGE_FACTORY, TestServerSessionStorage.class);
  }

}
