package com.pless.authentication;

import com.pless.users.User;

public class AuthenticationToken {

  private final User authenticatedUser;

  public AuthenticationToken(User user) {
    this.authenticatedUser = user;
  }
  
  public User getAuthenticatedUser() {
    return authenticatedUser;
  }

}
