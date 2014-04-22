package si.urbas.pless.sessions;

public abstract class ClientSessionStorage {

  public abstract void put(String key, String value);

  public abstract void remove(String key);

  public abstract String get(String key);

}
