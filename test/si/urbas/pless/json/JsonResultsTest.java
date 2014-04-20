package si.urbas.pless.json;

import org.junit.Test;
import play.api.libs.json.JsString;
import play.api.mvc.ResponseHeader;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static play.mvc.Http.HeaderNames.CONTENT_TYPE;
import static play.mvc.Http.Status.OK;
import static si.urbas.pless.json.JsonResults.asContent;

public class JsonResultsTest {

  private final JsString fooJsString = new JsString("foo");
  private final ResponseHeader responseHeader = JsonResults.okJson(fooJsString).getWrappedSimpleResult().header();
  @SuppressWarnings("UnusedDeclaration")
  private final JsonResults jsonResults = new JsonResults();

  @Test
  public void okJson_MUST_return_an_OK_status_result() throws Exception {
    assertEquals(OK, responseHeader.status());
  }

  @Test
  public void okJson_MUST_return_json_content_type_result() throws Exception {
    assertThat(
      responseHeader.headers().get(CONTENT_TYPE).get(),
      startsWith(asContent(fooJsString).contentType())
    );
  }

  @Test
  public void asContent_MUST_return_a_json_content_instance() throws Exception {
    assertThat(
      asContent(fooJsString),
      is(instanceOf(JsonContent.class))
    );
  }

}
