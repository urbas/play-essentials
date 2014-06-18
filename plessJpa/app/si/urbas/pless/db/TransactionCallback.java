package si.urbas.pless.db;

import javax.persistence.EntityManager;
import java.util.function.Consumer;

public interface TransactionCallback extends Consumer<EntityManager> {

}
