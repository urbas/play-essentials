package si.urbas.pless.authentication;

public interface ClientSessionStorage {

  void put(String key, String value);

  void remove(String key);

  String get(String key);

}