package com.urmest.emailing;

import com.urmest.util.ConfigurationSource;

public interface EmailFactory {
  Email createEmail(ConfigurationSource configurationProvider);
}
