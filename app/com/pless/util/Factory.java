package com.pless.util;

public interface Factory<T> {

  T createInstance(ConfigurationSource configurationSource);

}
