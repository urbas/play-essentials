package si.urbas.pless.test.users;

import org.junit.Before;
import si.urbas.pless.users.UserRepositoryTest;

public class HashMapUserRepositoryTest extends UserRepositoryTest {

  @Before
  public void setUp() throws Exception {
    userRepository = new HashMapUserRepository();
  }


}
