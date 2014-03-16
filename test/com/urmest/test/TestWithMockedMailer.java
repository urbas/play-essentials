package com.urmest.test;

import org.junit.After;

import com.urmest.emailing.MockEmailProvider;

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