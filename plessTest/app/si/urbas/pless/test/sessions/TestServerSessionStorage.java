package si.urbas.pless.test.sessions;

import si.urbas.pless.sessions.ServerSessionStorage;
import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.Factory;


public class TestServerSessionStorage implements Factory<ServerSessionStorage> {
  public static ServerSessionStorage currentServerSessionStorage;

  @Override
  public ServerSessionStorage createInstance(ConfigurationSource configurationSource) {
    return currentServerSessionStorage;
  }
}
