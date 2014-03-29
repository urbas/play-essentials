package si.urbas.pless.emailing;

import si.urbas.pless.util.ConfigurationSource;

public interface EmailProvider {
  Email createEmail(ConfigurationSource configurationSource);
}
