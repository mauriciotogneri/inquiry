package com.mauriciotogneri.inquiry;

import java.lang.reflect.Field;
import java.sql.ResultSet;

public class TypedResultSet<T>
{
    private final ResultSet rows;
    private final Class<T> clazz;

    public TypedResultSet(ResultSet rows, Class<T> clazz)
    {
        this.rows = rows;
        this.clazz = clazz;
    }

    public QueryResult<T> rows() throws DatabaseException
    {
        int numberOfRows = numberOfRows(rows);
        QueryResult<T> result = new QueryResult<>(numberOfRows);

        try
        {
            while (rows.next())
            {
                result.add(row(rows, clazz));
            }
        }
        catch (Exception e)
        {
            throw new DatabaseException(e);
        }

        return result;
    }

    private int numberOfRows(ResultSet rows)
    {
        int numberOfRows = 0;

        try
        {
            if (rows.last())
            {
                numberOfRows = rows.getRow();
                rows.beforeFirst();
            }
        }
        catch (Exception e)
        {
            // ignore
        }

        return numberOfRows;
    }

    private T row(ResultSet rows, Class<T> clazz) throws DatabaseException
    {
        try
        {
            T object = clazz.newInstance();

            Field[] fields = clazz.getDeclaredFields();

            for (int i = 0; i < fields.length; i++)
            {
                int index = i + 1;
                Field field = fields[i];

                if (field.getType().equals(String.class))
                {
                    field.set(object, rows.getString(index));
                }
                else if (field.getType().equals(Boolean.class))
                {
                    field.set(object, rows.getBoolean(index));
                }
                else if (field.getType().equals(Integer.class))
                {
                    field.set(object, rows.getInt(index));
                }
                else if (field.getType().equals(Long.class))
                {
                    field.set(object, rows.getLong(index));
                }
                else if (field.getType().equals(Float.class))
                {
                    field.set(object, rows.getFloat(index));
                }
                else if (field.getType().equals(Double.class))
                {
                    field.set(object, rows.getDouble(index));
                }
            }

            return object;
        }
        catch (Exception e)
        {
            throw new DatabaseException(e);
        }
    }
}