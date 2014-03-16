package com.pless.authentication;

import java.util.HashMap;

public final class TestClientSessionStorage implements ClientSessionStorage {
  private final HashMap<String, String> hashMap = new HashMap<>();

  @Override
  public void remove(String key) {
    hashMap.remove(key);
  }

  @Override
  public void put(String key, String value) {
    hashMap.put(key, value);
  }

  @Override
  public String get(String key) {
    return hashMap.get(key);
  }
}