package si.urbas.pless.authentication;

public interface ServerSessionStorage {

  public abstract void put(String key, String value, int expirationMillis);

  public abstract String get(String key);

  public abstract boolean remove(String key);

}