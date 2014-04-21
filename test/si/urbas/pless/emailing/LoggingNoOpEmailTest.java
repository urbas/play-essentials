package si.urbas.pless.emailing;

import org.junit.Test;
import org.slf4j.Logger;
import play.api.templates.Html;
import scala.collection.mutable.StringBuilder;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class LoggingNoOpEmailTest {

  @Test
  public void send_MUST_log() throws Exception {
    Logger logger = mock(Logger.class);
    LoggingNoOpEmail loggingNoOpEmail = new LoggingNoOpEmail(logger);
    loggingNoOpEmail.setBody(new Html(new StringBuilder()));
    loggingNoOpEmail.setFrom("Sender");
    loggingNoOpEmail.setRecipient("Recepient");
    loggingNoOpEmail.setSubject("Subject");
    loggingNoOpEmail.send();
    verify(logger).debug(any(String.class));
  }

  @Test
  public void send_MUST_use_the_system_logger_WHEN_none_is_provided() throws Exception {
    new LoggingNoOpEmail().send();
  }

}
