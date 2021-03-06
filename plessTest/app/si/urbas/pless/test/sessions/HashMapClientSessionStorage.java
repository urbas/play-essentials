package si.urbas.pless.test.sessions;

import si.urbas.pless.sessions.ClientSessionStorage;

import java.util.HashMap;

public class HashMapClientSessionStorage extends ClientSessionStorage {
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

  public boolean isEmpty() {
    return hashMap.isEmpty();
  }
}