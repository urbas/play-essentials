package si.urbas.pless.json;

import org.junit.Test;
import play.api.libs.json.JsString;

import static org.junit.Assert.assertEquals;

public class JsonContentTest {

  private JsString fooJsString = new JsString("foo");
  private final JsonContent javaJsonContentFooString = new JsonContent(fooJsString);

  @Test
  public void body_MUST_produce_a_json_string() throws Exception {
    assertEquals(fooJsString.toString(), javaJsonContentFooString.body());
  }

  @Test
  public void contentType_MUST_return_the_json_application_content_type_with_the_charset() throws Exception {
    assertEquals("application/json", javaJsonContentFooString.contentType());
  }

}
