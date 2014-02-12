package com.urmest.test;

public class UrmestTest extends TestWithMockedMailer {
  private static final String TEST_PERSISTENCE_UNIT = "com.urmest.testPersistenceUnit";

  public UrmestTest() {
    applicationBuilder.setPersistenceUnit(TEST_PERSISTENCE_UNIT);
  }
}
