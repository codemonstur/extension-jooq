package extension.jooq;

/**
 * The {@code StringToType} interface.
 *
 * @param <T> the type parameter
 */
public interface StringToType<T> {

    /**
     * Returns the result of {@code toType}.
     *
     * @param parameterName the parameter name
     * @param input the input
     * @return the result
     * @throws WrongTypeForField if the operation fails
     */
    T toType(String parameterName, String input) throws WrongTypeForField;

}
