package si.urbas.pless.users;

import org.junit.Before;

public class HashMapUserRepositoryTest extends UserRepositoryTest {

  @Before
  public void setUp() throws Exception {
    userRepository = new HashMapUserRepository();
  }


}
