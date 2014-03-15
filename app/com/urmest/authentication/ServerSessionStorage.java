package com.urmest.authentication;

public interface ServerSessionStorage {

  public abstract void put(String key, Object value, int expirationSeconds);

  public abstract Object get(String key);

  public abstract void remove(String key);

}