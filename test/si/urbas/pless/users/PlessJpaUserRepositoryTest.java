package si.urbas.pless.users;

import org.junit.After;
import org.junit.Before;
import si.urbas.pless.test.PlessJpaTest;

import static si.urbas.pless.users.PlessUserRepository.getUserRepository;

public class PlessJpaUserRepositoryTest extends UserRepositoryTest {

  @SuppressWarnings("UnusedDeclaration")
  private static final JpaUser FIRST_USER = new JpaUser(1L);
  private PlessJpaTest jpaSetup;


  @Before
  public void setUp() throws Exception {
    jpaSetup = new PlessJpaTest();
    jpaSetup.setUp();
    userRepository = PlessUserRepository.getUserRepository();
  }

  @After
  public void tearDown() throws Exception {
    jpaSetup.tearDown();
  }

  public static User persistAndFetchUser(String userEmail, String userPassword) {
    persistUser(userEmail, userPassword);
    return fetchUser(userEmail);
  }

  public static void persistUser(String userEmail, String userPassword) {
    getUserRepository().persistUser(userEmail, userPassword);
  }

  public static boolean activateUser(final User user) {
    return activateUser(user.getEmail(), user.getActivationCode());
  }

  public static boolean activateUser(final String email, final String activationCode) {
    return getUserRepository().activateUser(email, activationCode);
  }

  public static User fetchUser(String userEmail) {
    return getUserRepository().findUserByEmail(userEmail);
  }
}
