package si.urbas.pless.test;

import play.test.FakeApplication;
import play.test.Helpers;

import java.util.HashMap;
import java.util.Map;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.stop;

public class TemporaryPlayApplication implements AutoCloseable {
  protected final FakeApplication fakeApplication;

  public TemporaryPlayApplication() {
    this(new HashMap<String, String>());
  }

  public TemporaryPlayApplication(Map<String, String> applicationOptions) {
    fakeApplication = fakeApplication(applicationOptions);
    Helpers.start(fakeApplication);
  }

  @Override
  public void close() {
    stop(fakeApplication);
  }
}
