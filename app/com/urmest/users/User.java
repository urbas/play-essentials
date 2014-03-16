package com.urmest.users;

public interface User {

  public abstract String getEmail();

  public abstract long getId();

  public abstract byte[] getSalt();

  public abstract byte[] getHashedPassword();

}