package si.urbas.pless.db;

import javax.persistence.EntityManager;

public interface TransactionFunction<TReturn> extends java.util.function.Function<EntityManager, TReturn> {

}
