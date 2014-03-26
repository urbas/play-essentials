package com.pless.test;


public class MockedApplication extends TestApplication {

  public MockedApplication() {
    temporaryServices.add(new TemporaryConfiguration());
    temporaryServices.add(new TemporaryEmailProvider());
    temporaryServices.add(new TemporaryUserRepository());
    temporaryServices.add(new TemporaryServerSessionStorage());
    temporaryServices.add(new TemporaryClientSessionStorage());
  }

}
