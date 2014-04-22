package si.urbas.pless.test.matchers;

import org.hamcrest.*;

import si.urbas.pless.authentication.SaltedHashedPassword;
import si.urbas.pless.users.PlessUser;

public class UserMatchers {

  public static Matcher<? super PlessUser> userWith(String userEmail, String username, String password) {
    return new UserWithEmailAndPasswordMatcher(userEmail, username, password);
  }

  public static Matcher<PlessUser> activeUser() {
    return new IsActiveUserMatcher();
  }

  public static BaseUserMatcher userWithLongActivationCode() {
    return new BaseUserMatcher() {
      @Override
      public boolean matches(PlessUser user) {
        return user.getActivationCode() != null &&
          user.getActivationCode().length() > 16;
      }
    };
  }

  public static abstract class BaseUserMatcher extends BaseMatcher<PlessUser> {

    @Override
    public boolean matches(Object item) {
      return item instanceof PlessUser && matches((PlessUser) item);
    }

    public abstract boolean matches(PlessUser user);

    @Override
    public void describeTo(Description description) {}

  }

  private static final class IsActiveUserMatcher extends BaseUserMatcher {
    @Override
    public void describeTo(Description description) {
      description.appendText("active user");
    }

    @Override
    public boolean matches(PlessUser user) {
      return user.isActivated();
    }
  }

  private static final class UserWithEmailAndPasswordMatcher extends BaseUserMatcher {

    private final String userEmail;
    private final String username;
    private String password;

    public UserWithEmailAndPasswordMatcher(String userEmail, String username, String password) {
      this.userEmail = userEmail;
      this.username = username;
      this.password = password;
    }

    @Override
    public void describeTo(Description description) {
      description.appendText("PlessUser [email=" + userEmail + ", username=" + username + ", password=" + password + "]");
    }

    @Override
    public boolean matches(PlessUser user) {
      return userEmail.equalsIgnoreCase(user.getEmail()) &&
        new SaltedHashedPassword(password, user.getSalt()).matches(user
          .getHashedPassword());
    }
  }

}
