package com.mauriciotogneri.inquiry;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class UpdateQuery extends Query
{
    public UpdateQuery(Connection connection, String query)
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