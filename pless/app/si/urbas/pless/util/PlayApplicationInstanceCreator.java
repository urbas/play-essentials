package si.urbas.pless.util;

import play.Play;

class PlayApplicationInstanceCreator implements java.util.function.Function<String, Object> {

  public static final PlayApplicationInstanceCreator INSTANCE = new PlayApplicationInstanceCreator();

  @Override
  public Object apply(String s) {
    try {
      return Play.application().classloader().loadClass(s).newInstance();
    } catch (Exception e) {
      throw new RuntimeException("Could not instantiate class '" + s + "'.", e);
    }
  }

  @SuppressWarnings("unchecked")
  public static <T> java.util.function.Function<String, T> getInstance() {
    return (java.util.function.Function<String, T>) INSTANCE;
  }
}
