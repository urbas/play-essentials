package si.urbas.pless.test;

import si.urbas.pless.util.Factories;
import si.urbas.pless.util.Factory;
import si.urbas.pless.util.Function;

import java.util.Map;

import static org.mockito.Mockito.*;
import static si.urbas.pless.test.TestConfigurationUtils.setConfigurationString;

public class TemporaryFactories implements AutoCloseable {

  private final Function<String, Factory<?>> oldFactoriesClassLoader;

  public TemporaryFactories(Map<String, Factory<?>> configuredFactories) {
    oldFactoriesClassLoader = Factories.getInstanceCreator();
    @SuppressWarnings("unchecked")
    Function<String, Factory<?>> factoriesClassLoader = spy(oldFactoriesClassLoader);
    for (String factoryConfigKey : configuredFactories.keySet()) {
      useFactoryForConfigKey(factoriesClassLoader, factoryConfigKey, configuredFactories.get(factoryConfigKey));
    }
    Factories.overrideClassLoader(factoriesClassLoader);
  }

  private void useFactoryForConfigKey(Function<String, Factory<?>> factoriesClassLoader, String factoryConfigKey, Factory factory) {
    setConfigurationString(factoryConfigKey, factoryConfigKey);
    doReturn(factory).when(factoriesClassLoader).invoke(factoryConfigKey);
  }

  @Override
  public void close() throws Exception {
    Factories.overrideClassLoader(oldFactoriesClassLoader);
  }
}
