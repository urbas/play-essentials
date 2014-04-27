package si.urbas.pless.db;

import si.urbas.pless.util.Function;

import javax.persistence.EntityManager;

public interface TransactionFunction<TReturn> extends Function<EntityManager, TReturn> {

}
