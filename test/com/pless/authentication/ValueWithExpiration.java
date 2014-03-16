package com.pless.authentication;

import java.util.Date;

public class ValueWithExpiration<T> {

  public T value;
  private final int expirationMillis;
  private final Date creationTimestamp;

  public ValueWithExpiration(T value, int expirationMillis) {
    this.value = value;
    this.expirationMillis = expirationMillis;
    this.creationTimestamp = new Date();
  }

  public boolean isExpired() {
    return new Date().getTime() > creationTimestamp.getTime() + expirationMillis;
  }

}
