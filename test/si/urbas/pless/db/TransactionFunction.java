package si.urbas.pless.db;

import javax.persistence.EntityManager;

public interface TransactionFunction<T> {

  T invoke(EntityManager em);

}
