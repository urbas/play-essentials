# Play Essentials 

[![Build Status](https://drone.io/bitbucket.org/urbas/play-essentials/status.png)](https://drone.io/bitbucket.org/urbas/play-essentials/latest)

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

    libraryDependencies ++= Seq(
        "com.pless" %% "play-essentials" % "0.0.1-SNAPSHOT"
    )

Pless also comes with classes that make tests in JUnit easier. To use
them, just add this dependency:

    libraryDependencies ++= Seq(
        "com.pless" %% "play-essentials" % "0.0.1-SNAPSHOT" % "test" classifier "tests"
    )

# Usage

## Authentication

You can use any of the below examples from within your controller.

To start an authentication session with a password login:

    PlayAuthentication.logIn(new PasswordLoginForm(email, password));

Check that a user is logged in:

    PlayAuthentication.isLoggedIn()

To get the ID of the currently logged-in user:

    PlayAuthentication.getLoggedInUserId()

To log the user out:

    PlayAuthentication.logOut()

## Emailing

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

    smtp.emailProvider=com.pless.emailing.ApacheCommonsEmailProvider
    dev.smtp.emailProvider=com.pless.emailing.LoggingNoOpEmailProvider
    test.smtp.emailProvider=com.pless.emailing.MockEmailProvider

### Usage

Here's how you send an email:

    PlayEmailing.sendEmail(recepient, emailSubject, htmlBody);

Note that `htmlBody` is a Play HTML view template. Say you have a
Play view named `OfferUpdateEmailTemplate.scala.html`, then you can send an
email like this:

    Html offerUpdateHtml = OfferUpdateEmailTemplate.apply(offerUpdateData);
    PlayEmailing.sendEmail(recepient, emailSubject, offerUpdateHtml);

#### Advanced usage

    Email email = PlayEmailing.createEmail();
    email.setSubject(subject);
    email.setRecipient(recipient);
    email.setFrom(sender);
    email.setBody(body);
    email.send();
