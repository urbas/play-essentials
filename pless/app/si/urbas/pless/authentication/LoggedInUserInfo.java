package si.urbas.pless.authentication;

import si.urbas.pless.users.PlessUser;
import si.urbas.pless.util.StringUtils;

public class LoggedInUserInfo {

  public static final String USER_INFO_SEPARATOR = ":";
  public final long userId;
  public final String email;

  public LoggedInUserInfo(String loginSessionData) {
    int idAndEmailSeparatorIndex = loginSessionData.indexOf(USER_INFO_SEPARATOR);
    userId = Long.parseLong(loginSessionData.substring(0, idAndEmailSeparatorIndex));
    email = loginSessionData.substring(idAndEmailSeparatorIndex + 1);
    if (StringUtils.isNullOrEmpty(email)) {
      throw new IllegalStateException("Login session contains an empty email, but the user ID is " + userId);
    }
  }

  @Override
  public String toString() {
    return toRawLoginData(userId, email);
  }

  public static String toRawLoginData(long userId, String email) {
    return userId + USER_INFO_SEPARATOR + email;
  }

  public static String toRawLoginData(PlessUser user) {
    return toRawLoginData(user.getId(), user.getEmail());
  }
}
