package com.urmest.authentication;

import play.cache.Cache;

/**
 * This server session storage uses Play's cache.
 * 
 * This class is stateless. Therefore, one instance of this
 * class may be shared across multiple requests.
 * 
 * @author matej
 */
public class CachedServerSessionStorage implements ServerSessionStorage {

  @Override
  public void put(String key, Object value, int expirationSeconds) {
    Cache.set(key, value, expirationSeconds);
  }

  @Override
  public Object get(String key) {
    return Cache.get(key);
  }

  @Override
  public void remove(String key) {
    Cache.remove(key);
  }

}
