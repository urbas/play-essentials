package com.urmest.emailing;

import com.urmest.util.ConfigurationProvider;

public interface Mailer {
  Email createEmail(ConfigurationProvider configurationProvider);
}
