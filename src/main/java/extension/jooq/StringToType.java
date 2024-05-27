package extension.jooq;

public interface StringToType<T> {

    T toType(String parameterName, String input) throws WrongTypeForField;

}
