package com.pless.util;

/**
 * Guideline: implementations of this interface should be immutable.
 * 
 * @author matej
 */
public interface ConfigurationSource {

  boolean isDevelopment();

  boolean isProduction();

  String getString(String configKey);

  int getInt(String configKey, int defaultValue);

  boolean getBoolean(String configKey, boolean defaultValue);

}
