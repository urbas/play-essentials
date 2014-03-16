package com.pless.users;

import static com.pless.util.StringUtils.isNullOrEmpty;

public class SignupForm {
  public String name;
  public String email;
  public String password;

  public SignupForm() {}

  public SignupForm(String name, String email, String password) {
    this.name = name;
    this.email = email;
    this.password = password;
  }

  public boolean isValid() {
    return !isNullOrEmpty(name) && !isNullOrEmpty(email) && !isNullOrEmpty(password);
  }
}