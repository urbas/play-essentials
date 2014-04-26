package si.urbas.pless.test.emailing;

import si.urbas.pless.emailing.Email;
import si.urbas.pless.emailing.EmailProvider;
import si.urbas.pless.util.ConfigurationSource;

public class SingleEmailProvider extends EmailProvider {

  private final Email email;

  SingleEmailProvider(Email email) {this.email = email;}

  @Override
  public Email createEmail(ConfigurationSource configurationSource) {
    return email;
  }
}
