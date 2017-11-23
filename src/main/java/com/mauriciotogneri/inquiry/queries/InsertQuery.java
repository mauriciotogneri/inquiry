package com.mauriciotogneri.inquiry.queries;

import com.mauriciotogneri.inquiry.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class InsertQuery extends Query
{
    public InsertQuery(Connection connection, String query)
    {
        super(connection, query, Statement.RETURN_GENERATED_KEYS);
    }

    public long execute(Object... parameters) throws DatabaseException
    {
        try (PreparedStatement statement = preparedStatement(parameters))
        {
            statement.executeUpdate();

            try (ResultSet rows = statement.getGeneratedKeys())
            {
                if (rows.next())
                {
                    return rows.getLong(1);
                }
                else
                {
                    throw new DatabaseException();
                }
            }
        }
        catch (Exception e)
        {
            throw new DatabaseException(e);
        }
    }
}