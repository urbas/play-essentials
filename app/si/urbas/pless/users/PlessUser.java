package si.urbas.pless.users;

import java.util.Date;

public interface PlessUser {

  String getEmail();

  long getId();

  byte[] getSalt();

  byte[] getHashedPassword();
  
  Date getCreationDate();
  
  boolean isActivated();
  
  String getActivationCode();

  void setActivated(boolean activated);
}