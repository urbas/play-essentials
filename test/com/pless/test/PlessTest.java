package com.pless.test;

public class PlessTest extends TestWithMockedMailer {
  private static final String TEST_PERSISTENCE_UNIT = "com.pless.testPersistenceUnit";

  public PlessTest() {
    applicationBuilder.setPersistenceUnit(TEST_PERSISTENCE_UNIT);
  }
}
