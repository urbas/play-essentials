package com.pless.authentication;

import java.util.HashMap;

public class TestServerSessionStorage implements ServerSessionStorage {
  private final HashMap<String, ValueWithExpiration<String>> hashMap = new HashMap<>();

  @Override
  public void remove(String key) {
    hashMap.remove(key);
  }

  @Override
  public void put(String key, String value, int expirationMillis) {
    hashMap.put(key, new ValueWithExpiration<>(value, expirationMillis));
  }

  @Override
  public String get(String key) {
    ValueWithExpiration<String> valueWithExpiration = hashMap.get(key);
    if (valueWithExpiration == null) {
      return null;
    } else {
      if (valueWithExpiration.isExpired()) {
        hashMap.remove(key);
        return null;
      }
      return valueWithExpiration.value;
    }
  }

  public void expireAllEntries() {
    hashMap.clear();
  }
}
