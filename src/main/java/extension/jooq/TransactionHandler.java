package extension.jooq;

import org.jooq.DSLContext;

public interface TransactionHandler<T> {

    T run(DSLContext db) throws Exception;

}
