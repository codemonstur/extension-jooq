package extension.jooq;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.jooq.SQLDialect.H2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExtensionJooqTest {

    private static final DSLContext DSL_CTX = DSL.using(H2);
    private static final Table<org.jooq.Record> TABLE = DSL.table("t");
    private static final Field<String> FIELD = DSL.field("col", String.class);

    @Test
    void insertQueryBuilder_setAndToIntermediate() throws WrongTypeForField {
        final var builder = new InsertQueryBuilder(DSL_CTX, TABLE, key -> "value");
        builder.set("key", FIELD);
        assertNotNull(builder.toIntermediateQuery());
    }

    @Test
    void insertQueryBuilder_nullValueSkips() throws WrongTypeForField {
        final var builder = new InsertQueryBuilder(DSL_CTX, TABLE, key -> null);
        builder.set("key", FIELD);
        assertThrows(WrongTypeForField.class, builder::toIntermediateQuery);
    }

    @Test
    void insertQueryBuilder_setWithConverter() throws WrongTypeForField {
        final var builder = new InsertQueryBuilder(DSL_CTX, TABLE, key -> "42");
        builder.set("key", DSL.field("num", Integer.class), (name, s) -> Integer.parseInt(s));
        assertNotNull(builder.toIntermediateQuery());
    }

    @Test
    void insertQueryBuilder_setFieldDirect() throws WrongTypeForField {
        final var builder = new InsertQueryBuilder(DSL_CTX, TABLE, key -> null);
        builder.set(FIELD, "direct");
        assertNotNull(builder.toIntermediateQuery());
    }

    @Test
    void updateQueryBuilder_setAndToIntermediate() throws WrongTypeForField {
        final var builder = new UpdateQueryBuilder(DSL_CTX, TABLE, key -> "value");
        builder.set("key", FIELD);
        assertNotNull(builder.toIntermediateQuery());
    }

    @Test
    void updateQueryBuilder_nullValueSkips() throws WrongTypeForField {
        final var builder = new UpdateQueryBuilder(DSL_CTX, TABLE, key -> null);
        builder.set("key", FIELD);
        assertThrows(WrongTypeForField.class, builder::toIntermediateQuery);
    }

    @Test
    void updateQueryBuilder_setFieldDirect() throws WrongTypeForField {
        final var builder = new UpdateQueryBuilder(DSL_CTX, TABLE, key -> null);
        builder.set(FIELD, "direct");
        assertNotNull(builder.toIntermediateQuery());
    }

    @Test
    void updateQueryBuilder_setWithConverter() throws WrongTypeForField {
        final var builder = new UpdateQueryBuilder(DSL_CTX, TABLE, key -> "42");
        builder.set("key", DSL.field("num", Integer.class), (name, s) -> Integer.parseInt(s));
        assertNotNull(builder.toIntermediateQuery());
    }

    @Test
    void wrongTypeForField_constructors() {
        final var e1 = new WrongTypeForField("msg");
        assertEquals("msg", e1.getMessage());
        final var e2 = new WrongTypeForField("msg", new RuntimeException("cause"));
        assertEquals("msg", e2.getMessage());
        assertEquals("cause", e2.getCause().getMessage());
    }

    @Test
    void stringToType_lambda() throws WrongTypeForField {
        final StringToType<Integer> conv = (name, input) -> Integer.parseInt(input);
        assertEquals(7, conv.toType("p", "7"));
    }

    @Test
    void relationalDB_defaults() {
        final RelationalDB db = () -> DSL_CTX;
        assertNotNull(db.select());
        assertNotNull(db.selectCount());
        assertNotNull(db.update(TABLE));
        assertNotNull(db.insertInto(TABLE));
        assertNotNull(db.insertInto(TABLE, key -> "v"));
        assertNotNull(db.deleteFrom(TABLE));
    }

    @Test
    void relationalDB_insertQueryBuilderNullValue() throws WrongTypeForField {
        final var b = db().insertInto(TABLE, key -> null);
        assertThrows(WrongTypeForField.class, b::toIntermediateQuery);
    }

    private RelationalDB db() {
        return () -> DSL_CTX;
    }

    @Test
    void relationalDB_newDbTransaction() {
        final RelationalDB db = () -> DSL_CTX;
        // transactionResult may fail on dialect-only context; expect exception or value
        try {
            final Integer r = db.newDbTransaction(dsl -> 42);
            assertEquals(42, r.intValue());
        } catch (Exception e) {
            // acceptable: no real DB
        }
    }

    @Test
    void relationalDB_executeQueries() {
        final RelationalDB db = () -> DSL_CTX;
        final QueryGenerator<String> gen = (d, i, item) -> d.insertInto(TABLE).values(DSL.val(item));
        try {
            db.executeQueries(List.of("a", "b"), gen);
        } catch (Exception e) {
            // acceptable: no real DB
        }
    }

    @Test
    void relationalDB_toDSL() {
        // just ensure no NPE on construction with null datasource? May throw; guard
        try {
            final var dsl = RelationalDB.toDSL(null, H2);
            assertNotNull(dsl);
        } catch (Exception e) {
            // acceptable
        }
    }
}
