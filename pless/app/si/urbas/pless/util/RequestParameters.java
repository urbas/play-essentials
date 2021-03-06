package si.urbas.pless.util;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class RequestParameters {

  public static Map.Entry<String, String[]> param(String parameterName, String... parameterValues) {
    return new AbstractMap.SimpleEntry<>(parameterName, parameterValues);
  }

  @SafeVarargs
  public static HashMap<String, String[]> params(Map.Entry<String, String[]>... parameterList) {
    return addParams(new HashMap<>(), parameterList);
  }

  @SafeVarargs
  public static HashMap<String, String[]> addParams(HashMap<String, String[]> parametersMapToExpand, Map.Entry<String, String[]>... parameterList) {
    for (Map.Entry<String, String[]> parameter : parameterList) {
      parametersMapToExpand.put(parameter.getKey(), parameter.getValue());
    }
    return parametersMapToExpand;
  }
}
