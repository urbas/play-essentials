package si.urbas.pless.util;

import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static si.urbas.pless.util.RequestParameters.param;
import static si.urbas.pless.util.RequestParameters.params;

public class RequestParametersTest {
  public static final String FOO_PARAMETER = "foo";
  public static final String MOO_PARAMETER = "moo";
  @SuppressWarnings("UnusedDeclaration")
  private final RequestParameters requestParameters = new RequestParameters();
  private String[] mooValues = new String[]{"mooVal1", "mooVal2"};
  private String[] emptyValues = new String[]{};
  private HashMap<String, String[]> constructedParameters = params(
    param(FOO_PARAMETER, emptyValues),
    param(MOO_PARAMETER, mooValues)
  );

  @Test
  public void params_MUST_produce_a_hashMap_with_parameter_keys_as_keys() throws Exception {
    assertThat(constructedParameters.keySet(), containsInAnyOrder(FOO_PARAMETER, MOO_PARAMETER));
  }

  @Test
  public void params_MUST_produce_a_hashMap_containing_the_parameter_values() throws Exception {
    assertThat(constructedParameters.values(), containsInAnyOrder(emptyValues, mooValues));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void params_MUST_produce_a_hashMap_with_matched_keys_and_values() throws Exception {
    assertThat(constructedParameters.entrySet(), containsInAnyOrder(param(FOO_PARAMETER, emptyValues), param(MOO_PARAMETER, mooValues)));
  }
}
