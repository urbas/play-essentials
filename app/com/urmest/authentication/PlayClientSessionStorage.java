package com.urmest.authentication;

import play.mvc.Http;

public class PlayClientSessionStorage implements ClientSessionStorage {

  @Override
  public void put(String key, String value) {
    Http.Context.current().session().put(key, value);
  }

  @Override
  public void remove(String key) {
    Http.Context.current().session().remove(key);
  }

  @Override
  public String get(String key) {
    return Http.Context.current().session().get(key);
  }

}
