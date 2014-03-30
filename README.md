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

    val pless = "si.urbas" %% "pless" % "0.0.3"

    libraryDependencies ++= Seq(
        pless
    )

Pless also comes with classes that make tests in JUnit easier. To use
them, just add this dependency:

    libraryDependencies ++= Seq(
        pless % "test->test" classifier "tests"
    )

# Usage

Make sure your controller extends `PlessController`:

  public class MyController extends PlessController {

  }

Now you can use any of the below examples from within this controller.

## Authentication

To login a user with their email and password:

    auth().logIn(new PasswordLoginForm(email, password));

Check that a user is currently logged in:

    auth().isLoggedIn()

To get the email of the currently logged-in user:

    auth().getLoggedInUserEmail()

To log the user out:

    auth().logOut()

## Emailing

Here's how you send an email:

    emailing().sendEmail(recepient, emailSubject, htmlBody);

Note that `htmlBody` is a Play HTML view template. Say you have a
Play view named `OfferUpdateEmailTemplate.scala.html`, then you can send an
email like this:

    Html offerUpdateHtml = OfferUpdateEmailTemplate.apply(offerUpdateData);
    emailing().sendEmail(recepient, emailSubject, offerUpdateHtml);

### Advanced usage

    Email email = emailing().createEmail();
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

    pless.emailProviderFactory=si.urbas.pless.emailing.ApacheCommonsEmailProvider
