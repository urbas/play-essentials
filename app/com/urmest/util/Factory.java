package com.urmest.util;

public interface Factory<T> {

  T createInstance(ConfigurationSource instance);

}
