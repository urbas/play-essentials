package com.pless.test;

import com.pless.authentication.ClientSessionStorage;
import com.pless.util.ConfigurationSource;
import com.pless.util.Factory;


public class TestClientSessionStorage implements Factory<ClientSessionStorage> {
  public static ClientSessionStorage currentClientSessionStorage;

  @Override
  public ClientSessionStorage createInstance(ConfigurationSource configurationSource) {
    return currentClientSessionStorage;
  }
}
