package com.pless.users;

import org.junit.Test;
import org.mockito.Mockito;

import com.pless.test.PlessTest;

public class LWUserControllerTest extends PlessTest {
  
  private static final String TEST1234 = "test1234";
  private static final String MATKO_EXAMPLE_COM = "matko@example.com";
  private static final String MARKO = "Marko";

  @Test
  public void createUser_MUST_persist_the_user_in_the_user_repository() throws Exception {
    UserController.createUser(new SignupForm(MARKO, MATKO_EXAMPLE_COM, TEST1234));
    Mockito.verify(getUserRepository()).persistUser(MARKO, MATKO_EXAMPLE_COM, TEST1234);
  }
  
  @Test
  public void signUp_MUST_persist_the_user_in_the_user_repository() throws Exception {
    UserController.signUp(MARKO, MATKO_EXAMPLE_COM, TEST1234);
    Mockito.verify(getUserRepository()).persistUser(MARKO, MATKO_EXAMPLE_COM, TEST1234);
  }

}
