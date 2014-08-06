package si.urbas.pless.pages;

import play.mvc.Controller;
import play.twirl.api.Html;
import si.urbas.pless.PlessService;
import si.urbas.pless.util.PlessServiceConfigKey;
import si.urbas.pless.util.ServiceLoader;
import si.urbas.pless.pages.views.html.FlashMessagesView;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static play.mvc.Controller.flash;
import static si.urbas.pless.util.ServiceLoader.createServiceLoader;

@PlessServiceConfigKey(FlashMessages.CONFIG_FLASH_MESSAGES)
public class FlashMessages implements PlessService {

  public static final String CONFIG_FLASH_MESSAGES = "pless.flashMessages";
  private static final String MESSAGE_TYPE_INFO = "info";
  private static final String MESSAGE_TYPE_ERROR = "error";

  public Html flashPanel(String htmlTagId, String htmlTagClass) {
    List<String> infoMessages = allFlashMessagesOfType(MESSAGE_TYPE_INFO);
    List<String> errorMessages = allFlashMessagesOfType(MESSAGE_TYPE_ERROR);
    return FlashMessagesView.apply(htmlTagId, htmlTagClass, infoMessages, errorMessages);
  }

  public void flashMessage(String messageKey, String message) {Controller.flash(messageKey, message);}

  public static void flashInfo(String messageKey, String message) {flashMessages().flashMessage(flashInfoKey(messageKey), message);}

  public static void flashError(String messageKey, String message) {flashMessages().flashMessage(flashErrorKey(messageKey), message);}

  protected static List<String> allFlashMessagesOfType(String messageType) {
    return flash().entrySet().stream()
      .filter(keyValue -> keyValue.getKey().startsWith(messageType))
      .map(Map.Entry::getValue)
      .collect(Collectors.toList());
  }

  protected static String flashInfoKey(final String messageKey) {return flashMessageKey(MESSAGE_TYPE_INFO, messageKey);}

  protected static String flashErrorKey(final String messageKey) {return flashMessageKey(MESSAGE_TYPE_ERROR, messageKey);}

  protected static String flashMessageKey(String messagePrefix, String messageKey) {return messagePrefix + "." + messageKey;}

  public static FlashMessages flashMessages() {return FlashMessagesLoader.INSTANCE.getService();}

  private static class FlashMessagesLoader {
    public static final ServiceLoader<FlashMessages> INSTANCE = createServiceLoader(new FlashMessages());
  }

}
