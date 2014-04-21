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
    return addParams(new HashMap<String, String[]>(), parameterList);
  }

  @SafeVarargs
  public static HashMap<String, String[]> addParams(HashMap<String, String[]> parametersMap, Map.Entry<String, String[]>... parameterList) {
    for (Map.Entry<String, String[]> parameter : parameterList) {
      parametersMap.put(parameter.getKey(), parameter.getValue());
    }
    return parametersMap;
  }
}
