package si.urbas.pless.test;

import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.Factories;
import si.urbas.pless.util.Factory;

import static org.mockito.Mockito.doReturn;
import static si.urbas.pless.util.ConfigurationSource.getConfigurationSource;

public class TemporaryFactory implements AutoCloseable {
  public final Factory<Object> oldFactory;
  public final String factoryClassName;
  public final Factory<Object> currentFactory;

  public TemporaryFactory(String factoryClassName, Factory<Object> currentFactory) {
    this.factoryClassName = factoryClassName;
    this.currentFactory = currentFactory;
    oldFactory = Factories.getOverriddenFactory(factoryClassName);
    Factories.overrideFactory(factoryClassName, currentFactory);
  }

  public static TemporaryFactory configureFactoryForInstance(String factoryConfigKey, final Object instance) {
    String factoryClassName = instance.getClass().getCanonicalName();
    doReturn(factoryClassName).when(getConfigurationSource()).getString(factoryConfigKey);
    return new TemporaryFactory(factoryClassName, new Factory<Object>() {
      @Override
      public Object createInstance(ConfigurationSource configurationSource) {
        return instance;
      }
    });
  }

  @Override
  public void close() {
    Factories.overrideFactory(factoryClassName, oldFactory);
  }
}
