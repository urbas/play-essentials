package si.urbas.pless.util;

public interface Factory<T> {

  T createInstance(ConfigurationSource configurationSource);

}
