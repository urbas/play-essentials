package com.pless.test;

import org.junit.Before;

/**
 * Starts up a fake Play application with an in-memory JPA database and a mocked mailer.
 * 
 * Use this to test your controllers.
 */
public class PlessContollerWithJpaTest extends PlessJpaTest {
  @Before
  public void setUp() {
    plessTestApplication = new PlayFunctionalJpaApplication();
  }
}
