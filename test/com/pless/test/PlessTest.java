package com.pless.test;

import org.junit.After;
import org.junit.Before;

public class PlessTest {

  protected PlessTestApplication plessTestApplication;

  @Before
  public void setUp() {
    plessTestApplication = new PlessMockedApplication();
  }

  @After
  public void tearDown() {
    plessTestApplication.close();
  }

}
