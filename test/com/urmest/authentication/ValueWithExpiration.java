package com.urmest.authentication;

import java.util.Date;

public class ValueWithExpiration<T> {

  public T value;
  private final int expirationSeconds;
  private final Date creationTimestamp;

  public ValueWithExpiration(T value, int expirationSeconds) {
    this.value = value;
    this.expirationSeconds = expirationSeconds;
    this.creationTimestamp = new Date();
  }

  public boolean isExpired() {
    return new Date().getTime() > creationTimestamp.getTime() + expirationSeconds;
  }

}
