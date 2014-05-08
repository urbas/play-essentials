package si.urbas.pless.users;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import si.urbas.pless.test.JpaApplication;
import si.urbas.pless.test.PlessJpaConfiguration;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static si.urbas.pless.test.matchers.UserMatchers.userWith;

public class JpaUserRepositoryTest extends UserRepositoryTest {

  @SuppressWarnings("UnusedDeclaration")
  private static final JpaPlessUser FIRST_USER = new JpaPlessUser(1L);
  public static final String EXTENDED_COLUMN_VALUE = "this is test";
  private JpaApplication jpaApplication;

  @Before
  public void setUp() throws Exception {
    jpaApplication = new JpaApplication(PlessJpaConfiguration.PLESS_INTERNAL_TEST_PERSISTENCE_UNIT);
    userRepository = UserRepository.getUserRepository();
  }

  @After
  public void tearDown() throws Exception {
    jpaApplication.close();
  }

  @Test
  public void findUserByEmail_MUST_return_persisted_extended_users() {
    getJpaUserRepository().persistUser(new TestExtendingJpaUser(USER_EMAIL, USER_USERNAME, USER_PASSWORD, EXTENDED_COLUMN_VALUE));
    PlessUser user = getJpaUserRepository().findUserByEmail(USER_EMAIL);
    assertThat(user, is(instanceOf(TestExtendingJpaUser.class)));
    assertThat(user, is(userWith(USER_EMAIL, USER_USERNAME, USER_PASSWORD)));
    assertEquals(EXTENDED_COLUMN_VALUE, ((TestExtendingJpaUser) user).getTestColumn());
  }

  private JpaUserRepository getJpaUserRepository() {return (JpaUserRepository) userRepository;}
}
