package si.urbas.pless.jpasample.auth;

import si.urbas.pless.PlessJpaController;
import si.urbas.pless.authentication.api.AuthenticationController;
import si.urbas.pless.authentication.AuthenticationService;
import si.urbas.pless.users.PlessUser;

@SuppressWarnings("UnusedDeclaration")
public class AuthenticationSamples extends PlessJpaController {

  public static void passwordAuthentication(String email, String password) {
// SNIPPET: passwordAuthentication
    AuthenticationController.logIn(email, password);
// ENDSNIPPET: passwordAuthentication
  }

  public static void genericAuthentication(String email, String password) {
    long userId = 123;
    String username = "john smith";

// SNIPPET: theAuthApi
    AuthenticationService authenticationService = auth();
// ENDSNIPPET: theAuthApi

// SNIPPET: genericAuthentication
    auth().logIn(new PlessUser(userId, email, username, password));
// ENDSNIPPET: genericAuthentication
  }

  public static void authInfoGettersSample() {
    // SNIPPET: isLoggedIn
    auth().isLoggedIn();
    // ENDSNIPPET: isLoggedIn

    // SNIPPET: loggedInEmail
    auth().getLoggedInUserEmail();
    // ENDSNIPPET: loggedInEmail

    // SNIPPET: logOut
    auth().logOut();
    // ENDSNIPPET: logOut
  }
}
