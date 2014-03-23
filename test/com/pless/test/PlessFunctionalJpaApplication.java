package com.pless.test;


public class PlessFunctionalJpaApplication extends PlessTestApplication {

  public PlessFunctionalJpaApplication() {
    temporaryServices.add(new TemporaryPlayConfiguration());
    temporaryServices.add(new TemporaryEmailProvider());
    temporaryServices.add(new TemporaryPlayJpaApplication());
    temporaryServices.add(new TemporaryEntityManagerProvider());
  }
  
}
