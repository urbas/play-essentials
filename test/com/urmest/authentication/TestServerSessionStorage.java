package com.urmest.authentication;

import java.util.HashMap;

public class TestServerSessionStorage implements ServerSessionStorage {
  private final HashMap<String, ValueWithExpiration<Object>> hashMap = new HashMap<>();

  @Override
  public void remove(String key) {
    hashMap.remove(key);
  }

  @Override
  public void put(String key, Object value, int expirationSeconds) {
    hashMap.put(key, new ValueWithExpiration<>(value, expirationSeconds));
  }

  @Override
  public Object get(String key) {
    ValueWithExpiration<Object> valueWithExpiration = hashMap.get(key);
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
