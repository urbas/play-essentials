package com.pless.users;

import static com.pless.util.StringUtils.isNullOrEmpty;

public class SignupForm {
  public String email;
  public String password;

  public SignupForm() {}

  public SignupForm(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public boolean isValid() {
    return !isNullOrEmpty(email) && !isNullOrEmpty(password);
  }
}