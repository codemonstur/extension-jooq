package extension.jooq;

import org.jooq.*;
import org.jooq.Record;

import java.util.function.Function;

/**
 * The {@code UpdateQueryBuilder} class.
 */
public final class UpdateQueryBuilder {

    private final DSLContext dsl;
    private final Table<? extends Record> table;
    private final Function<String, String> valueOf;

    private UpdateSetMoreStep<? extends Record> step;

    /**
     * Creates a new instance.
     *
     * @param dsl the dsl
     * @param table the table
     * @param valueOf the value of
     */
    public UpdateQueryBuilder(final DSLContext dsl, final Table<? extends Record> table, final Function<String, String> valueOf) {
        this.dsl = dsl;
        this.valueOf = valueOf;
        this.table = table;
    }

    /**
     * Returns the result of {@code set}.
     *
     * @param key the new value
     * @param field the new value
     * @return the result
     * @throws WrongTypeForField if the operation fails
     */
    public UpdateQueryBuilder set(final String key, final Field<String> field) throws WrongTypeForField {
        return set(key, field, (name, s) -> s);
    }

    /**
     * Returns the result of {@code set}.
     *
     * @param <T> the type parameter
     * @param key the new value
     * @param field the new value
     * @param convert the new value
     * @return the result
     * @throws WrongTypeForField if the operation fails
     */
    public <T> UpdateQueryBuilder set(final String key, final Field<T> field, final StringToType<T> convert) throws WrongTypeForField {
        final var first = valueOf.apply(key);
        return first == null ? this : set(field, convert.toType(key, first));
    }

    /**
     * Returns the result of {@code set}.
     *
     * @param <T> the type parameter
     * @param field the new value
     * @param value the new value
     * @return the result
     * @throws WrongTypeForField if the operation fails
     */
    public <T> UpdateQueryBuilder set(final Field<T> field, final T value) throws WrongTypeForField {
        step = (step == null ? dsl.update(table) : step).set(field, value);
        return this;
    }

    /**
     * Returns the result of {@code toIntermediateQuery}.
     *
     * @return the result
     * @throws WrongTypeForField if the operation fails
     */
    public UpdateSetMoreStep<? extends Record> toIntermediateQuery() throws WrongTypeForField {
        if (step == null) throw new WrongTypeForField("You must submit at least one field");
        return step;
    }

}
