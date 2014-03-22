package com.pless.test;

import org.junit.After;

public class TestWithMockedMailer extends TestWithFakeApplication {
  
  private TemporaryEmailProvider temporaryEmailProvider;

  @Override
  public void setUp() {
    super.setUp();
    temporaryEmailProvider = new TemporaryEmailProvider();
  }

  @Override
  @After
  public void tearDown() {
    super.tearDown();
    temporaryEmailProvider.close();
  }
}