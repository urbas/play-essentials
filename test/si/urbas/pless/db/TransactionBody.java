package si.urbas.pless.db;

import javax.persistence.EntityManager;

public interface TransactionBody {

  void invoke(EntityManager em);

}
