package si.urbas.pless.test;

import play.api.mvc.RequestHeader;
import play.mvc.Http;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static play.mvc.Http.Context;
import static play.mvc.Http.Context.current;

public class TemporaryHttpContext implements AutoCloseable {

  private final Context oldHttpContext;
  public final Http.Request request;

  public TemporaryHttpContext() {
    this(new HashMap<String, String>());
  }

  public TemporaryHttpContext(Map<String, String> flashData) {
    oldHttpContext = current.get();
    request = mock(Http.Request.class);
    current.set(new Context(123L, mock(RequestHeader.class), request, flashData, flashData, new HashMap<String, Object>()));
  }

  @Override
  public void close() {
    current.set(oldHttpContext);
  }
}
