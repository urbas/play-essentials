package com.pless.test;

public class PlessControllerTest extends TestWithMockedMailer {
  private static final String TEST_PERSISTENCE_UNIT = "com.pless.testPersistenceUnit";

  public PlessControllerTest() {
    applicationBuilder.setPersistenceUnit(TEST_PERSISTENCE_UNIT);
  }
}
