package si.urbas.pless.util;

import play.Play;

class PlayApplicationInstanceCreator implements Function<String, Object> {
  public static final PlayApplicationInstanceCreator INSTANCE = new PlayApplicationInstanceCreator();

  @Override
  public Object invoke(String s) {
    try {
      return Play.application().classloader().loadClass(s).newInstance();
    } catch (Exception e) {
      throw new RuntimeException("Could not instantiate class '" + s + "'.", e);
    }
  }

  @SuppressWarnings("unchecked")
  public static <T> Function<String, T> getInstance() {
    return (Function<String, T>) INSTANCE;
  }
}
