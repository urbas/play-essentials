package com.pless.test;

import org.junit.After;

import com.pless.emailing.MockEmailProvider;

public class TestWithMockedMailer extends TestWithFakeApplication {
  public TestWithMockedMailer() {
    applicationBuilder.setMockMailer(MockEmailProvider.class);
  }

  @Override
  @After
  public void tearDown() {
    super.tearDown();
    MockEmailProvider.reset();
  }
}