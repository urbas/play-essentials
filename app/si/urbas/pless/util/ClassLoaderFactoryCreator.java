package si.urbas.pless.util;

public class ClassLoaderFactoryCreator implements Function<String, Factory<?>> {
  private final ClassLoader classLoader;

  public ClassLoaderFactoryCreator(ClassLoader classLoader) {this.classLoader = classLoader;}

  @Override
  public Factory<?> invoke(String className) {
    try {
      return (Factory) classLoader.loadClass(className).newInstance();
    } catch (Exception e) {
      throw new RuntimeException("Could not instantiate class '" + className + "'.", e);
    }
  }
}
