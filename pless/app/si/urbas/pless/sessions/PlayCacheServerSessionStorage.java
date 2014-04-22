package si.urbas.pless.sessions;

import play.cache.Cache;

/**
 * This server session storage uses Play's cache.
 * <p/>
 * This class is stateless. Therefore, one instance of this class may be shared
 * across multiple requests.
 *
 * @author matej
 */
public class PlayCacheServerSessionStorage implements ServerSessionStorage {

  @Override
  public void put(String key, String value, int expirationMillis) {
    Cache.set(key, value, expirationMillis / 1000);
  }

  @Override
  public String get(String key) {
    Object value = Cache.get(key);
    return value == null ? null : value.toString();
  }

  @Override
  public void remove(String key) {
    Cache.remove(key);
  }
}
