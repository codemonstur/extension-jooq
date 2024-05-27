package extension.jooq;

import org.jooq.*;
import org.jooq.Record;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.jooq.SQLDialect.*;

public interface RelationalDB {

    DSLContext relationalDB();

    default SelectSelectStep<org.jooq.Record> select() {
        return relationalDB().select();
    }
    default SelectSelectStep<Record1<Integer>> selectCount() {
        return relationalDB().selectCount();
    }
    default UpdateQueryBuilder update(final Table<? extends Record> table, final Function<String, String> valueOf) {
        return new UpdateQueryBuilder(relationalDB(), table, valueOf);
    }
    default <R extends Record> InsertSetStep<R> insertInto(final Table<R> into) {
        return relationalDB().insertInto(into);
    }
    default InsertQueryBuilder insertInto(final Table<? extends Record> table, final Function<String, String> valueOf) {
        return new InsertQueryBuilder(relationalDB(), table, valueOf);
    }
    default <R extends Record> UpdateSetFirstStep<R> update(final Table<R> update) {
        return relationalDB().update(update);
    }
    default <R extends Record> DeleteWhereStep<R> deleteFrom(final Table<R> delete) {
        return relationalDB().deleteFrom(delete);
    }
    default <T> T newDbTransaction(final TransactionHandler<T> handler) {
        return relationalDB().transactionResult(configuration -> {
            final DSLContext db = DSL.using(configuration);
            return handler.run(db);
        });
    }
    default <T> int[] executeQueries(final List<T> items, final QueryGenerator<T> queryGenerator) {
        final var db = relationalDB();
        final var queries = new ArrayList<Query>(items.size());
        for (int i = 0; i < items.size(); i++) {
            queries.add(queryGenerator.newQuery(db, i, items.get(i)));
        }
        return db.batch(queries).execute();
    }

    private static SQLDialect detectDatabaseType(final String jdbcUrl) throws WrongTypeForField {
        if (jdbcUrl == null || jdbcUrl.isEmpty()) throw new IllegalArgumentException("Missing JDBC URL, cannot detect DB type");
        if (jdbcUrl.startsWith("jdbc:mysql:")) return MYSQL;
        if (jdbcUrl.startsWith("jdbc:hsqldb:")) return HSQLDB;
        if (jdbcUrl.startsWith("jdbc:h2:")) return H2;
        throw new IllegalArgumentException("Could not detect SQL dialect from JDBC URL: " + jdbcUrl);
    }

    public static DSLContext toDSL(final DataSource dataSource, final SQLDialect dialect) {
        return DSL.using(dataSource, dialect, new Settings().withRenderSchema(false));
    }

}
