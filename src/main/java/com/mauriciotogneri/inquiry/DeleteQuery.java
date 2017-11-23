package com.mauriciotogneri.inquiry;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class DeleteQuery extends Query
{
    public DeleteQuery(Connection connection, String query)
    {
        super(connection, query);
    }

    public int execute(Object... parameters) throws DatabaseException
    {
        try (PreparedStatement statement = preparedStatement(parameters))
        {
            return statement.executeUpdate();
        }
        catch (Exception e)
        {
            throw new DatabaseException(e);
        }
    }
}