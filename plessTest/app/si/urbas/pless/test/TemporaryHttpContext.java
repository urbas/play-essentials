package si.urbas.pless.test;

import play.api.mvc.RequestHeader;
import play.mvc.Http;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static play.mvc.Http.Context;
import static play.mvc.Http.Context.current;
import static si.urbas.pless.util.RequestParameters.params;

public class TemporaryHttpContext implements AutoCloseable {

  private final Context oldHttpContext;
  public final Http.Request request;

  public TemporaryHttpContext() {
    this(new HashMap<>(), params());
  }

  public TemporaryHttpContext(Map<String, String> flashData, HashMap<String, String[]> queryString) {
    oldHttpContext = current.get();
    request = createMockedRequest(queryString);
    current.set(new Context(123L, mock(RequestHeader.class), request, flashData, flashData, new HashMap<>()));
  }

  @Override
  public void close() {
    current.set(oldHttpContext);
  }

  private static Http.Request createMockedRequest(HashMap<String, String[]> queryString) {
    Http.Request mock = mock(Http.Request.class);
    when(mock.queryString()).thenReturn(queryString);
    Http.RequestBody requestBody = mock(Http.RequestBody.class);
    when(mock.body()).thenReturn(requestBody);
    return mock;
  }
}
