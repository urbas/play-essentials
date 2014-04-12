package si.urbas.pless.util;

public class ClassLoaderInstanceCreator implements Function<String, Factory<?>> {
  private final ClassLoader newClassLoader;

  public ClassLoaderInstanceCreator(ClassLoader newClassLoader) {this.newClassLoader = newClassLoader;}

  @Override
  public Factory<?> invoke(String s) {
    try {
      return (Factory) newClassLoader.loadClass(s).getConstructor().newInstance();
    } catch (Exception e) {
      throw new RuntimeException("Could not instantiate class '" + s + "'.", e);
    }
  }
}
