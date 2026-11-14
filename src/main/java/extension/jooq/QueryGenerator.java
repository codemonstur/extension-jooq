package extension.jooq;

import org.jooq.DSLContext;
import org.jooq.Query;

/**
 * The {@code QueryGenerator} interface.
 *
 * @param <T> the type parameter
 */
public interface QueryGenerator<T> {
    /**
     * Returns the result of {@code newQuery}.
     *
     * @param db the db
     * @param index the index
     * @param item the item
     * @return the result
     */
    Query newQuery(DSLContext db, int index, T item);
}
