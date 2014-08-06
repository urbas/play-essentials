package si.urbas.pless.pages.test;

import play.Logger;
import si.urbas.pless.pages.FlashMessages;

public class TestFlashMessages extends FlashMessages {
  @Override
  public void flashMessage(String messageKey, String message) {
    Logger.debug("Flashed the message '" + message + "' with key '" + messageKey + "'.");
    try {
      super.flashMessage(messageKey, message);
    } catch (Exception ignored) {}
  }
}
