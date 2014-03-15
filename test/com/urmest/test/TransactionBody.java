package com.urmest.test;

import javax.persistence.EntityManager;

public interface TransactionBody {

  void invoke(EntityManager em);

}
