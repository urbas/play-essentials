package com.urmest.authentication;

import com.urmest.users.User;

public class AuthenticationToken {

  private final User authenticatedUser;

  public AuthenticationToken(User user) {
    this.authenticatedUser = user;
  }
  
  public User getAuthenticatedUser() {
    return authenticatedUser;
  }

  public long getId() {
    return authenticatedUser.getId();
  }

}
