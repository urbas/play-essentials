package com.pless.test;

import com.pless.authentication.ServerSessionStorage;
import com.pless.util.ConfigurationSource;
import com.pless.util.Factory;


public class TestServerSessionStorage implements Factory<ServerSessionStorage> {
  public static ServerSessionStorage currentServerSessionStorage;

  @Override
  public ServerSessionStorage createInstance(ConfigurationSource configurationSource) {
    return currentServerSessionStorage;
  }
}
