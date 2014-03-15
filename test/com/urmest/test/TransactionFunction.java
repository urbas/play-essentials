package com.urmest.test;

import javax.persistence.EntityManager;

public interface TransactionFunction<T> {

  T invoke(EntityManager em);

}
