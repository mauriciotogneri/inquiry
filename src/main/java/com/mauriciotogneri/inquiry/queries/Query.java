package com.mauriciotogneri.inquiry.queries;

import com.mauriciotogneri.inquiry.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Query
{
    private final Connection connection;
    private final String query;
    private final Integer flag;

    public Query(Connection connection, String query, Integer flag)
    {
        this.connection = connection;
        this.query = query;
        this.flag = flag;
    }

    public Query(Connection connection, String query)
    {
        this(connection, query, null);
    }

    public int update(Object... parameters) throws DatabaseException
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

    public ResultSet result(Object... parameters) throws DatabaseException
    {
        try (PreparedStatement statement = preparedStatement(parameters))
        {
            return statement.executeQuery();
        }
        catch (Exception e)
        {
            throw new DatabaseException(e);
        }
    }

    private PreparedStatement preparedStatement() throws DatabaseException, SQLException
    {
        if (flag != null)
        {
            return connection.prepareStatement(query, flag);
        }
        else
        {
            return connection.prepareStatement(query);
        }
    }

    protected PreparedStatement preparedStatement(Object... parameters) throws DatabaseException
    {
        try
        {
            PreparedStatement statement = preparedStatement();

            for (int i = 0; i < parameters.length; i++)
            {
                Object parameter = parameters[i];
                int index = i + 1;

                if (parameter.getClass().equals(String.class))
                {
                    statement.setString(index, (String) parameter);
                }
                else if (parameter.getClass().equals(Boolean.class))
                {
                    statement.setBoolean(index, (Boolean) parameter);
                }
                else if (parameter.getClass().equals(Integer.class))
                {
                    statement.setInt(index, (Integer) parameter);
                }
                else if (parameter.getClass().equals(Long.class))
                {
                    statement.setLong(index, (Long) parameter);
                }
                else if (parameter.getClass().equals(Float.class))
                {
                    statement.setFloat(index, (Float) parameter);
                }
                else if (parameter.getClass().equals(Double.class))
                {
                    statement.setDouble(index, (Double) parameter);
                }
                else if (parameter.getClass().equals(Timestamp.class))
                {
                    statement.setTimestamp(index, (Timestamp) parameter);
                }
                else if (parameter.getClass().isEnum())
                {
                    statement.setString(index, parameter.toString());
                }
            }

            return statement;
        }
        catch (Exception e)
        {
            throw new DatabaseException(e);
        }
    }
}