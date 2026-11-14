package extension.jooq;

import org.jooq.DSLContext;

/**
 * The {@code TransactionHandler} interface.
 *
 * @param <T> the type parameter
 */
public interface TransactionHandler<T> {

    /**
     * Runs the operation.
     *
     * @param db the db
     * @return the result
     * @throws Exception if the operation fails
     */
    T run(DSLContext db) throws Exception;

}
