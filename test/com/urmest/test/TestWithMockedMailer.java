package com.urmest.test;

import org.junit.After;

import com.urmest.emailing.MockMailer;

public class TestWithMockedMailer extends TestWithFakeApplication {
  public TestWithMockedMailer() {
    applicationBuilder.setMockMailer(MockMailer.class);
  }

  @Override
  @After
  public void tearDown() {
    super.tearDown();
    MockMailer.reset();
  }
}