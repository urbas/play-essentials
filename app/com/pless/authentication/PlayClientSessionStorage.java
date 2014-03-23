package com.pless.authentication;

import play.mvc.Http;

import com.pless.util.*;

/**
 * This client session storage uses Play's session from the HTTP context.
 * 
 * This class is stateless. A single instance of this class may be shared
 * across multiple requests. 
 * 
 * @author matej
 */
public class PlayClientSessionStorage implements ClientSessionStorage {
  
  public static final String CONFIG_CLIENT_SESSION_STORAGE_FACTORY = "pless.clientSessionStorageFactory";

  @Override
  public void put(String key, String value) {
    Http.Context.current().session().put(key, value);
  }

  @Override
  public void remove(String key) {
    Http.Context.current().session().remove(key);
  }

  @Override
  public String get(String key) {
    return Http.Context.current().session().get(key);
  }

  public static ClientSessionStorage getClientSessionStorage() {
    return PlayFactories.getFactories().createInstance(
      CONFIG_CLIENT_SESSION_STORAGE_FACTORY,
      new DefaultClientSessionStorageFactory());
  }

  private static final class DefaultClientSessionStorageFactory implements
    Factory<ClientSessionStorage>
  {
    @Override
    public ClientSessionStorage createInstance(ConfigurationSource configurationSource) {
      return new PlayClientSessionStorage();
    }
  }

}
