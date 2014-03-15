package com.urmest.authentication;

import play.cache.Cache;

public class PlayServerSessionStorage implements ServerSessionStorage {

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
