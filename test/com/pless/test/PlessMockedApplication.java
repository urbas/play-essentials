package com.pless.test;


public class PlessMockedApplication extends PlessTestApplication {

  public PlessMockedApplication() {
    temporaryServices.add(new TemporaryGlobalConfiguration());
    temporaryServices.add(new TemporaryEmailProvider());
    temporaryServices.add(new TemporaryUserRepository());
    temporaryServices.add(new TemporaryServerSessionStorage());
    temporaryServices.add(new TemporaryClientSessionStorage());
  }

}
