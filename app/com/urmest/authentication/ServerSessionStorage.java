package com.urmest.authentication;

public interface ServerSessionStorage {

  public abstract void put(String key, String value, int expirationMillis);

  public abstract String get(String key);

  public abstract void remove(String key);

}