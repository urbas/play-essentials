package si.urbas.pless.authentication;

import play.mvc.Http;

/**
 * This client session storage uses Play's session from the HTTP context.
 * 
 * This class is stateless. A single instance of this class may be shared
 * across multiple requests. 
 * 
 * @author matej
 */
public class PlayHttpContextClientSessionStorage implements ClientSessionStorage {
  
  @Override
  public void put(String key, String value) {
    Http.Context.current().session().put(key, value);
  }

  @Override
  public void remove(String key) {
    Http.Context.current().session().remove(key);
  }

  @Override
  public String get(String key) {
    return Http.Context.current().session().get(key);
  }

}
