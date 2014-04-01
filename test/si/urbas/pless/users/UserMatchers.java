package si.urbas.pless.users;

import org.hamcrest.*;

import si.urbas.pless.authentication.SaltedHashedPassword;

public class UserMatchers {

  public static Matcher<? super User> userWith(String userEmail, String password) {
    return new UserWithEmailAndPasswordMatcher(userEmail, password);
  }

  public static Matcher<User> activeUser() {
    return new IsActiveUserMatcher();
  }

  public static BaseUserMatcher userWithLongActivationCode() {
    return new BaseUserMatcher() {
      @Override
      public boolean matches(User user) {
        return user.getActivationCode() != null &&
          user.getActivationCode().length() > 16;
      }
    };
  }

  public static abstract class BaseUserMatcher extends BaseMatcher<User> {

    @Override
    public boolean matches(Object item) {
      if (item instanceof User) {
        return matches((User) item);
      }
      return false;
    }

    public abstract boolean matches(User user);

    @Override
    public void describeTo(Description description) {}

  }

  private static final class IsActiveUserMatcher extends BaseUserMatcher {
    @Override
    public void describeTo(Description description) {
      description.appendText("active user");
    }

    @Override
    public boolean matches(User user) {
      return user.isActivated();
    }
  }

  private static final class UserWithEmailAndPasswordMatcher extends BaseUserMatcher {

    private final String userEmail;
    private String password;

    public UserWithEmailAndPasswordMatcher(String userEmail, String password) {
      this.userEmail = userEmail;
      this.password = password;
    }

    @Override
    public void describeTo(Description description) {
      description.appendText("User [email=" + userEmail + ", password=" + password + "]");
    }

    @Override
    public boolean matches(User user) {
      return userEmail.equalsIgnoreCase(user.getEmail()) &&
        new SaltedHashedPassword(password, user.getSalt()).matches(user
          .getHashedPassword());
    }
  }

}
