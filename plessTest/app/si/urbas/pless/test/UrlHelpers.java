package si.urbas.pless.test;

import play.api.mvc.Call;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

public class UrlHelpers {
  public static String escapedAbsoluteUrl(TemporaryHttpContext httpContext, Call call) {return escapeHtml4(call.absoluteURL(httpContext.request, false));}
}
