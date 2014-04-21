package si.urbas.pless.util;

public class ClassLoaderInstanceCreator implements Function<String, Object> {
  private final ClassLoader classLoader;

  public ClassLoaderInstanceCreator(ClassLoader classLoader) {this.classLoader = classLoader;}

  @Override
  public Object invoke(String className) {
    try {
      return classLoader.loadClass(className).newInstance();
    } catch (Exception e) {
      throw new RuntimeException("Could not instantiate class '" + className + "'.", e);
    }
  }
}
