package si.urbas.pless.users;

import javax.persistence.EntityManager;

import si.urbas.pless.test.TransactionBody;

public class PersistSingleUserTransaction implements TransactionBody {

  private final String userEmail;
  private final String userPassword;

  public PersistSingleUserTransaction(String userEmail, String userPassword) {
    this.userEmail = userEmail;
    this.userPassword = userPassword;
  }

  @Override
  public void invoke(EntityManager em) {
    UserRepository userRepository = new PlessJpaUserRepository(em);
    userRepository.persistUser(userEmail, userPassword);
  }
}