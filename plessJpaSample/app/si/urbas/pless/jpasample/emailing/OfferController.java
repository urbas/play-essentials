package si.urbas.pless.jpasample.emailing;

import play.api.templates.Html;
import play.mvc.Result;
import si.urbas.pless.PlessController;
import si.urbas.pless.jpasample.emailing.html.OfferUpdateEmail;
import si.urbas.pless.users.PlessUser;

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
  }

  @SuppressWarnings("UnusedParameters")
  private static void saveOffer(String offerName, String offerPrice, String offerDescription) {

  }

  private static PlessUser getInterestedUser() {
    return null;
  }
}
