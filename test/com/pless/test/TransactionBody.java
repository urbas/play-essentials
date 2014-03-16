package com.pless.test;

import javax.persistence.EntityManager;

public interface TransactionBody {

  void invoke(EntityManager em);

}
