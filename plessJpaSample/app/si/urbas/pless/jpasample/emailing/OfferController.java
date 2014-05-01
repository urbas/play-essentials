package si.urbas.pless.jpasample.emailing;

import play.api.templates.Html;
import play.mvc.Result;
import si.urbas.pless.PlessController;
import si.urbas.pless.emailing.Email;
import si.urbas.pless.jpasample.emailing.html.OfferUpdateEmail;
import si.urbas.pless.users.PlessUser;

@SuppressWarnings("UnusedDeclaration")
public class OfferController extends PlessController {
  public static Result offer(String offerName, String offerPrice, String offerDescription) {
    saveOffer(offerName, offerPrice, offerDescription);

    PlessUser userToNotify = getInterestedUser();
    sendOfferUpdatedNotificationEmail(userToNotify, offerName, offerPrice, offerDescription);
    return ok();
  }

  public static Result viewOffer(String offerName) {
    return ok("Buy this great offer! The offer: " + offerName);
  }

  private static void sendOfferUpdatedNotificationEmail(PlessUser userToNotify, String offerName, String offerPrice, String offerDescription) {
    // SNIPPET: sendEmail
    String subject = "Offer " + offerName + " updated";
    Html emailBody = OfferUpdateEmail.apply(userToNotify, offerName, offerDescription, offerPrice);
    emailing().sendEmail(userToNotify.getEmail(), subject, emailBody);
    // ENDSNIPPET: sendEmail
    String email = "";
    Html htmlBody = new Html(null);
    // SNIPPET: simpleSendEmail
    emailing().sendEmail(email, subject, htmlBody);
    // ENDSNIPPET: simpleSendEmail
  }

  private static void complexSendMail() {
    String recipient = "";
    String sender = "";
    String subject = "";
    Html body = new Html(null);
    // SNIPPET: complexSendEmail
    Email email = emailing().createEmail();
    email.setSubject(subject);
    email.setRecipient(recipient);
    email.setFrom(sender);
    email.setBody(body);
    email.send();
    // ENDSNIPPET: complexSendEmail
  }

  @SuppressWarnings("UnusedParameters")
  private static void saveOffer(String offerName, String offerPrice, String offerDescription) {

  }

  private static PlessUser getInterestedUser() {
    return null;
  }
}
