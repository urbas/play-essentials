package com.pless.emailing;

import com.pless.util.ConfigurationSource;

public interface EmailProvider {
  Email createEmail(ConfigurationSource configurationSource);
}
