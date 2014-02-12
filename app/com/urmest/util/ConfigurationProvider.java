package com.urmest.util;

import static play.Play.application;
import play.Configuration;
import play.Play;

public class ConfigurationProvider {
  public Configuration getConfiguration() {
    return application().configuration();
  }

  public boolean isProd() {
    return Play.isProd();
  }

  public boolean isDev() {
    return Play.isDev();
  }
}
