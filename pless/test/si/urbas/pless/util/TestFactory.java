package si.urbas.pless.util;

public class TestFactory implements Factory<String> {
  public static final String OBJECT_CREATED_VIA_FACTORY = "Object created via test factory";

  @Override
  public String createInstance(ConfigurationSource instance) {
    return OBJECT_CREATED_VIA_FACTORY;
  }
}