package com.mauriciotogneri.inquiry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SelectQuery<T> extends Query
{
    private final Class<T> clazz;

    public SelectQuery(Connection connection, String query, Class<T> clazz)
    {
        super(connection, query);

        this.clazz = clazz;
    }

    public QueryResult<T> execute(Object... parameters) throws DatabaseException
    {
        try (PreparedStatement statement = preparedStatement(parameters);
             ResultSet rows = statement.executeQuery())
        {
            TypedResultSet<T> typedResultSet = new TypedResultSet<>(rows, clazz);

            return typedResultSet.rows();
        }
        catch (Exception e)
        {
            throw new DatabaseException(e);
        }
    }
}