package extension.jooq;

import org.jooq.DSLContext;
import org.jooq.Query;

public interface QueryGenerator<T> {
    Query newQuery(DSLContext db, int index, T item);
}
