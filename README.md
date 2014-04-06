# Play Essentials 

[![Build Status](https://drone.io/bitbucket.org/urbas/play-essentials/status.png)](https://drone.io/bitbucket.org/urbas/play-essentials/latest)
[![Build Status](https://travis-ci.org/urbas/play-essentials.png?branch=master)](https://travis-ci.org/urbas/play-essentials)

Play Essentials (Pless) is a library that helps you develop Play Framework applications super quickly. Just follow the
Quickstart instructions below, and you'll get a working application with authentication, user management, emailing
support and all that other jazz.

Pless takes away the worry of having to develop all the silly login, email-sending, and user-managy boilerplate and
lets you focus on what matters to your business.

Why Pless? Because it provides a Java 8-friendly API and focuses on testability, ease of use (for casual users), and
super configurability (for power-users).

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

2.  Create a new Play application:

        play new myplessapp

3.  Add these lines into `build.sbt`:

        resolvers += "Sonatype Public Repository" at "https://oss.sonatype.org/content/groups/public"

        libraryDependencies += "si.urbas" %% "pless" % "0.0.6"

4.  Put the following route into `conf/routes` (just after `GET /`)

        ->      /                           si.urbas.pless.Route

5.  Copy [persistence.xml](./samples/jpa/h2/persistence.xml) to `conf/META-INF` and add this line to `conf/application.conf`:

        jpa.default=si.urbas.pless.defaultPersistenceUnit

That's it, now you can start developing.

You can run your application the usual Play way:

    play run

and open it in your browser via the following link: [http://localhost:9000/](http://localhost:9000/)

>   TODO: make Quickstart simpler by providing a seedling.

# Usage

Make sure your controllers extend `PlessController`:

    public class MyController extends PlessController {

    }

Now you can use any of the below examples from within your controllers.

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

## Testing

Pless comes with classes that make tests in JUnit easier. To use them, just add this dependency:

    libraryDependencies += "si.urbas" %% "pless" % "0.0.6" % "test->test" classifier "tests"