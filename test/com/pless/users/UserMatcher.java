package com.pless.users;

import org.hamcrest.*;

import com.pless.authentication.SaltedHashedPassword;

public class UserMatcher extends BaseMatcher<User> {
  private final String userEmail;
  private String password;

  public UserMatcher(String userEmail, String password) {
    this.userEmail = userEmail;
    this.password = password;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("User with email: '" + userEmail + "' and password: '" + password + "'.");
  }

  @Override
  public boolean matches(Object item) {
    if (item instanceof User) {
      User user = (User) item;

      return userEmail.equalsIgnoreCase(user.getEmail()) &&
        new SaltedHashedPassword(password, user.getSalt()).matches(user
          .getHashedPassword());
    }
    return false;
  }

  public static Matcher<User> userWith(String userEmail, String password) {
    return new UserMatcher(userEmail, password);
  }
}
