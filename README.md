# Play Essentials 

[![Build Status](https://drone.io/bitbucket.org/urbas/play-essentials/status.png)](https://drone.io/bitbucket.org/urbas/play-essentials/latest)
[![Build Status](https://travis-ci.org/urbas/play-essentials.png?branch=master)](https://travis-ci.org/urbas/play-essentials)

Play Essentials (Pless) is a Play Framework seedling on steroids. Just clone and
start building your custom Play app on Pless. You can also use Pless as a
library.

Why Pless? Because it provides a Java 8-friendly API and focuses on
testability, ease of use (for casual users), and configurability (for
power-users).

Some provided features (not exhaustive):

-   User management
-   Authentication management
-   Emailing support
-   JUnit test support for fake applications with an in-memory JPA database.

NOTE: all of the above features are otherwise available
through [Play Modules](http://www.playframework.com/modules). Pless
packages these modules where appropriate. However, Pless also
implements its own Java APIs wherever the Scala API of Play modules impedes
reusability from Java, testability from JUnit, and general configurability.

# Quickstart

1.  Install [Play Framework](http://www.playframework.com/download).

2.  Clone Pless:

        git clone git@bitbucket.org:urbas/play-essentials.git

3.  Run:

        cd play-essentials
        play run

4.  Open [http://localhost:9000/](http://localhost:9000/) in your browser.

5.  Edit the sources and create something wonderful.

## Using Pless as a dependency

You can also add Pless to your Play app just like this (in your `build.sbt`):

    resolvers += {
        "Urbas Nexus Releases" at "http://urbas.si:8081/nexus/content/repositories/releases/"
    }

    libraryDependencies ++= Seq(
        "si.urbas" %% "pless" % "0.0.2-SNAPSHOT"
    )

Pless also comes with classes that make tests in JUnit easier. To use
them, just add this dependency:

    libraryDependencies ++= Seq(
        "si.urbas" %% "pless" % "0.0.2-SNAPSHOT" % "test" classifier "tests"
    )

# Usage

You can use any of the below examples from within your controller.

## Authentication

Get the authentication service:

    authService = PlessAuthentication.getAuthenticationService()

To login a user with their email and password:

    authService.logIn(new PasswordLoginForm(email, password));

Check that a user is currently logged in:

    authService.isLoggedIn()

To get the ID of the currently logged-in user:

    authService.getLoggedInUserId()

To log the user out:

    authService.logOut()

## Emailing

Here's how you send an email:

    PlayEmailing.sendEmail(recepient, emailSubject, htmlBody);

Note that `htmlBody` is a Play HTML view template. Say you have a
Play view named `OfferUpdateEmailTemplate.scala.html`, then you can send an
email like this:

    Html offerUpdateHtml = OfferUpdateEmailTemplate.apply(offerUpdateData);
    PlessEmailing.sendEmail(recepient, emailSubject, offerUpdateHtml);

### Advanced usage

    Email email = PlessEmailing.createEmail();
    email.setSubject(subject);
    email.setRecipient(recipient);
    email.setFrom(sender);
    email.setBody(body);
    email.send();

### Configuration

You can put these configuration settings into `conf/application.conf`:

    smtp.from="Your Site <your.site@example.com>"
    smtp.host=smtp.example.com
    smtp.port=587
    smtp.ssl=yes
    smtp.tls=yes
    smtp.user="username@example.com"
    smtp.password=test1234

#### Advanced configuration

The following configuration is useful for mocking in tests or to plug in your
own email provider:

    pless.emailProviderFactory=com.pless.emailing.ApacheCommonsEmailProvider
