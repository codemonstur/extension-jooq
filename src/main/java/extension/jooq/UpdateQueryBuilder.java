package extension.jooq;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;
import org.jooq.UpdateSetMoreStep;
import org.jooq.Record;

import java.util.function.Function;

public final class UpdateQueryBuilder {

    private final DSLContext dsl;
    private final Table<? extends Record> table;
    private final Function<String, String> valueOf;

    private UpdateSetMoreStep<? extends Record> step;

    public UpdateQueryBuilder(final DSLContext dsl, final Table<? extends Record> table, final Function<String, String> valueOf) {
        this.dsl = dsl;
        this.valueOf = valueOf;
        this.table = table;
    }

    public UpdateQueryBuilder set(final String key, final Field<String> field) throws WrongTypeForField {
        return set(key, field, (name, s) -> s);
    }

    public <T> UpdateQueryBuilder set(final String key, final Field<T> field, final StringToType<T> convert) throws WrongTypeForField {
        final var first = valueOf.apply(key);
        return first == null ? this : set(field, convert.toType(key, first));
    }

    public <T> UpdateQueryBuilder set(final Field<T> field, final T value) throws WrongTypeForField {
        step = (step == null ? dsl.update(table) : step).set(field, value);
        return this;
    }

    public UpdateSetMoreStep<? extends Record> toIntermediateQuery() throws WrongTypeForField {
        if (step == null) throw new WrongTypeForField("You must submit at least one field");
        return step;
    }

}
