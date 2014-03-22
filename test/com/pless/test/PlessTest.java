package com.pless.test;

import org.junit.After;
import org.junit.Before;

public class PlessTest {

  private PlessTestApplication plessTestApplication;

  public PlessTestApplication getPlessTestApplication() {
    return plessTestApplication;
  }

  @Before
  public void setUp() {
    plessTestApplication = new PlessTestApplication();
  }

  @After
  public void tearDown() {
    plessTestApplication.close();
  }

}
