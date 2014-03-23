package com.pless.users;

import javax.persistence.EntityManager;

import com.pless.test.TransactionBody;

public class PersistSingleUserTransaction implements TransactionBody {

  private final String userEmail;
  private final String userPassword;

  public PersistSingleUserTransaction(String userEmail, String userPassword) {
    this.userEmail = userEmail;
    this.userPassword = userPassword;
  }

  @Override
  public void invoke(EntityManager em) {
    PlessJpaUserRepository userRepository = new PlessJpaUserRepository(em);
    userRepository.persistUser(userEmail, userPassword);
  }
}