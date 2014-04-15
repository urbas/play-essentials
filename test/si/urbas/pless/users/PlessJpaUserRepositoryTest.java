package si.urbas.pless.users;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import si.urbas.pless.test.PlessJpaConfiguration;
import si.urbas.pless.test.TestJpaApplication;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static si.urbas.pless.users.PlessUserRepository.getUserRepository;
import static si.urbas.pless.users.UserMatchers.userWith;

public class PlessJpaUserRepositoryTest extends UserRepositoryTest {

  @SuppressWarnings("UnusedDeclaration")
  private static final JpaPlessUser FIRST_USER = new JpaPlessUser(1L);
  public static final String EXTENDED_COLUMN_VALUE = "this is test";
  private TestJpaApplication jpaApplication;


  @Before
  public void setUp() throws Exception {
    jpaApplication = new TestJpaApplication(PlessJpaConfiguration.PLESS_INTERNAL_TEST_PERSISTENCE_UNIT);
    userRepository = PlessUserRepository.getUserRepository();
  }

  @After
  public void tearDown() throws Exception {
    jpaApplication.close();
  }

  @Test
  public void findUserByEmail_MUST_return_persisted_extended_users() {
    getJpaUserRepository().persistUser(new TestExtendingJpaUser(USER_EMAIL, USER_PASSWORD, EXTENDED_COLUMN_VALUE));
    PlessUser user = getJpaUserRepository().findUserByEmail(USER_EMAIL);
    assertThat(user, is(instanceOf(TestExtendingJpaUser.class)));
    assertThat(user, is(userWith(USER_EMAIL, USER_PASSWORD)));
    assertEquals("this is test", ((TestExtendingJpaUser)user).testColumn);
  }

  private PlessJpaUserRepository getJpaUserRepository() {return (PlessJpaUserRepository) userRepository;}

  public static PlessUser persistAndFetchUser(String userEmail, String userPassword) {
    persistUser(userEmail, userPassword);
    return fetchUser(userEmail);
  }

  public static void persistUser(String userEmail, String userPassword) {
    getUserRepository().persistUser(userEmail, userPassword);
  }

  public static boolean activateUser(final PlessUser user) {
    return activateUser(user.getEmail(), user.getActivationCode());
  }

  public static boolean activateUser(final String email, final String activationCode) {
    return getUserRepository().activateUser(email, activationCode);
  }

  public static PlessUser fetchUser(String userEmail) {
    return getUserRepository().findUserByEmail(userEmail);
  }
}
