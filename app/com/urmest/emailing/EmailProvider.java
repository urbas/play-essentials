package com.urmest.emailing;

import com.urmest.util.ConfigurationSource;

public interface EmailProvider {
  Email createEmail(ConfigurationSource configurationSource);
}
