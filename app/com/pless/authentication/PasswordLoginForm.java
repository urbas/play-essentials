package com.pless.authentication;

import static com.pless.util.StringUtils.isNullOrEmpty;

public class PasswordLoginForm {
  public String email;
  public String password;

  public PasswordLoginForm() {}

  public PasswordLoginForm(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public boolean isValid() {
    return !isNullOrEmpty(email) && !isNullOrEmpty(password);
  }
}