package com.pless.util;

public class ProductionFactory implements Factory<String> {
  public static final String OBJECT_CREATED_VIA_FACTORY = "Object created via production factory";

  @Override
  public String createInstance(ConfigurationSource instance) {
    return OBJECT_CREATED_VIA_FACTORY;
  }
}