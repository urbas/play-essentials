package si.urbas.pless.test;

import play.test.FakeApplication;

import java.util.HashMap;
import java.util.Map;

import static play.test.Helpers.*;

public class TemporaryPlayApplication implements AutoCloseable {
  protected final FakeApplication fakeApplication;

  public TemporaryPlayApplication() {
    this(new HashMap<String, String>());
  }

  public TemporaryPlayApplication(Map<String, String> applicationOptions) {
    fakeApplication = fakeApplication(applicationOptions);
    start(fakeApplication);
  }

  @Override
  public void close() {
    stop(fakeApplication);
  }
}
