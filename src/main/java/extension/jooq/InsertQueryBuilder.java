package extension.jooq;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.InsertSetMoreStep;
import org.jooq.Table;
import org.jooq.Record;

import java.util.function.Function;

public final class InsertQueryBuilder {

    private final DSLContext dsl;
    private final Table<? extends Record> table;
    private final Function<String, String> valueOf;

    private InsertSetMoreStep<? extends Record> step;

    public InsertQueryBuilder(final DSLContext dsl, final Table<? extends Record> table, final Function<String, String> valueOf) {
        this.dsl = dsl;
        this.valueOf = valueOf;
        this.table = table;
    }

    public InsertQueryBuilder set(final String key, final Field<String> field) throws WrongTypeForField {
        return set(key, field, (name, s) -> s);
    }

    public <T> InsertQueryBuilder set(final String key, final Field<T> field, final StringToType<T> convert) throws WrongTypeForField {
        final var value = valueOf.apply(key);
        return value == null ? this : set(field, convert.toType(key, value));
    }

    public <T> InsertQueryBuilder set(final Field<T> field, final T value) {
        step = step == null ? dsl.insertInto(table).set(field, value) : step.set(field, value);
        return this;
    }

    public InsertSetMoreStep<? extends Record> toIntermediateQuery() throws WrongTypeForField {
        if (step == null) throw new WrongTypeForField("You need to set at least one field");
        return step;
    }

}
