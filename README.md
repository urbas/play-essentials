# Play Essentials

[![Build Status](https://drone.io/bitbucket.org/urbas/play-essentials/status.png)](https://drone.io/bitbucket.org/urbas/play-essentials/latest)
[![Build Status](https://travis-ci.org/urbas/play-essentials.png?branch=master)](https://travis-ci.org/urbas/play-essentials)

> Play Essentials is in development. Do not use it in production until release `1.0.0`.

Play Essentials (Pless) is a library that helps you develop Play Framework applications in Java super quickly. Just
follow the Quickstart instructions below, and you'll get a working application with authentication, user management,
emailing support and all that other jazz.

Pless takes away the worry of having to develop all the silly login, email-sending, and user-management boilerplate. It
lets you focus on what matters to your business.

Pless provides a Java 8 API and focuses on testability. It's easy to use (for casual users), and it's super configurable
(for power-users).

Some features (not exhaustive):

-   User management (sign up, sign up emails, account activation, deletion, password reset, account detail modification)
-   Authentication management (login, logout, secure authentication sessions, authentication session timeout)
-   Emailing support
-   JUnit test support with completely mocked applications or with an in-memory JPA database.

NOTE: some of the features above are otherwise available through [Play Modules](http://www.playframework.com/modules).
Pless packages these modules where appropriate. However, Pless also implements its own Java APIs wherever the Scala API
of Play modules impedes reusability from Java, testability from JUnit, and general configurability.

# Quickstart

1.  Install [Play Framework](http://www.playframework.com/download).

2.  Create a new Play application.

3.  Add these lines into `build.sbt`:

    ```scala
    resolvers += "Sonatype Public Repository" at "https://oss.sonatype.org/content/groups/public"

    libraryDependencies += "si.urbas" %% "pless-jpa" % "0.0.16"
    ```

4.  Put the following route into `conf/routes` (just after `GET /`)

    ```scala
    ->    /                  si.urbas.pless.pages.Routes
->    /                  si.urbas.pless.api.Routes
    ```

5.  Copy [persistence.xml](./plessJpaSample/conf/META-INF/persistence.xml) to `conf/META-INF` and add this line to `conf/application.conf`:

        jpa.default=pless.jpasample.defaultPersistenceUnit

That's it. Start developing!

>   TODO: make Quickstart easier by providing an Activator template.

# Usage

## JPA-based applications

Make sure your controllers look like this:

```java
import si.urbas.pless.PlessJpaController;

public class MyController extends PlessJpaController {

}
```

Now you can use any of the below examples from within your controllers.

## Authentication

Executing an action with an authenticated user:

```java
import play.mvc.Result;
import si.urbas.pless.PlessJpaController;

import static si.urbas.pless.authentication.AuthenticationHelpers.withAuthenticatedUser;
import static si.urbas.pless.util.ApiResults.SUCCESS;

public class WithAuthenticatedUserController extends PlessJpaController {

  public static Result myAction() {
    return withAuthenticatedUser(loggedInUserInfo -> {
      performSomeOperation(loggedInUserInfo.userId);
      return SUCCESS;
    });
  }

  private static void performSomeOperation(long userId) {}
}
```

### Authentication pages

>   TODO: Web pages for authentication have not been created yet.

### Authentication REST API

Consumers of your application can log-in through the following REST API:

```scala
POST  /api/auth/login      si.urbas.pless.authentication.api.AuthenticationController.logIn()
```

### Lower-level authentication API

You can get access to the entire authentication API like this (from within your controller):

```java
AuthenticationService authenticationService = auth();
```

Check that a user is currently logged in:

```java
auth().isLoggedIn();
```

To get the email of the currently logged-in user:

```java
auth().getLoggedInUserEmail();
```

You can log-in a user directly from your controller. Here's an example of password authentication:

```java
AuthenticationController.logIn(email, password);
```

To log the user out:

```java
auth().logOut();
```

>   TODO: Implement authentication through Play's authenticator mechanism.

### Custom authentication mechanism

You can introduce a custom authentication procedure through the generic login method:

```java
auth().logIn(new PlessUser(userId, email, username, password));
```

The above method starts an authentication session for the given user (even if the user does not exist in the user
repository).

## Emailing

Here's how you send an email:

```java
emailing().sendEmail(email, subject, htmlBody);
```

Note that `htmlBody` is a Play HTML view template. Say you have a
Play view named `OfferUpdateEmailTemplate.scala.html`, then you can send an
email like this:


```java
String subject = "Offer " + offerName + " updated";
Html emailBody = OfferUpdateEmail.apply(userToNotify, offerName, offerDescription, offerPrice);
emailing().sendEmail(userToNotify.getEmail(), subject, emailBody);
```

### Advanced emailing

```java
Email email = emailing().createEmail();
email.setSubject(subject);
email.setRecipient(recipient);
email.setFrom(sender);
email.setBody(body);
email.send();
```

### Emailing configuration

You can put these configuration settings into `conf/application.conf`:

```java
smtp.from="Your email <your.email@gmail.com>"
smtp.host=smtp.gmail.com
smtp.port=587
smtp.ssl=yes
smtp.tls=yes
smtp.user="your.email@gmail.com"
smtp.password=test1234
```

## Testing

Pless comes with classes that make tests in JUnit easier. To use them, just add this dependency:

```scala
libraryDependencies += "si.urbas" %% "pless-jpa-test" % "0.0.16" % "test"
```

### Testing with an in-memory JPA database

You tests should extend from our test base-class that brings up an in-memory JPA database together with a working test Play aplication. You only have to override the following function:

```java
import si.urbas.pless.test.PlayJpaControllerTest;

public class MyControllerTest extends PlayJpaControllerTest {

  @Override
  protected String getTestPersistenceUnit() {
    return "pless.jpasample.testPersistenceUnit";
  }
}
```