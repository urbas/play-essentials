package com.pless.users;

import java.util.Date;

public interface User {

  String getEmail();

  long getId();

  byte[] getSalt();

  byte[] getHashedPassword();
  
  Date getCreationDate();
  
  boolean isActivated();
  
  String getActivationCode();

}