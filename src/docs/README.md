# extension-jooq

Extra helpers for using [jOOQ](https://www.jooq.org/) — a relational abstraction
layer with fluent query builders that map string keys to typed jOOQ `Field`s.

## Usage

### RelationalDB

`RelationalDB` is the central interface. It exposes a `DSLContext` plus a set of
convenience default methods (`select()`, `selectCount()`, `update(...)`,
`insertInto(...)` and transaction helpers), and can be constructed from a
`DataSource` with an explicit SQL dialect:

```java
RelationalDB db = RelationalDB.fromDataSource(dataSource, SQLDialect.POSTGRES);

List<Record> rows = db.select().from(USERS).fetch();
```

### Fluent query builders

`UpdateQueryBuilder` and `InsertQueryBuilder` build statements from maps of string
keys to values. A `Function<String, String> valueOf` lookup resolves which jOOQ
`Field` each key refers to, and a `StringToType<T>` converter produces typed
values:

```java
new InsertQueryBuilder(table, this::fieldForColumn, this::convertValue)
    .values(rowMap)
    .execute(dslContext);
```

If a value cannot be converted for the target field, the builders throw
`WrongTypeForField`.

## Key types

| Type | Purpose |
| --- | --- |
| `RelationalDB` | DSLContext wrapper with convenience methods. |
| `UpdateQueryBuilder` / `InsertQueryBuilder` | String-key → Field fluent builders. |
| `StringToType<T>` | Conversion function from `String` to a typed field value. |
| `QueryGenerator<T>` | Produces a jOOQ `Query` for a single item. |
| `TransactionHandler<T>` | Callback interface for running work inside a transaction. |
| `WrongTypeForField` | Checked exception for type-mismatched field values. |

## Dependency

```xml
<dependency>
  <groupId>com.github.codemonstur</groupId>
  <artifactId>extension-jooq</artifactId>
  <version>1.0.1</version>
</dependency>
```

Requires `org.jooq:jooq:3.17.7`.

## Building

```sh
make build
```

## License

MIT
