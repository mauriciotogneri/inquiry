package com.mauriciotogneri.inquiry;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.Timestamp;

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
                Class<?> fieldType = field.getType();

                if (fieldType.equals(String.class))
                {
                    field.set(object, rows.getString(index));
                }
                else if (fieldType.equals(Boolean.class))
                {
                    field.set(object, rows.getBoolean(index));
                }
                else if (fieldType.equals(Integer.class))
                {
                    field.set(object, rows.getInt(index));
                }
                else if (fieldType.equals(Long.class))
                {
                    field.set(object, rows.getLong(index));
                }
                else if (fieldType.equals(Float.class))
                {
                    field.set(object, rows.getFloat(index));
                }
                else if (fieldType.equals(Double.class))
                {
                    field.set(object, rows.getDouble(index));
                }
                else if (fieldType.equals(Timestamp.class))
                {
                    field.set(object, rows.getTimestamp(index));
                }
                else if (fieldType.isEnum())
                {
                    field.set(object, enumValue(fieldType, rows.getString(index)));
                }
                else if (fieldType.isArray())
                {
                    Class<?> arrayType = fieldType.getComponentType();
                    java.sql.Array columnArray = rows.getArray(index);

                    Object[] array;

                    if (columnArray == null)
                    {
                        array = (Object[]) Array.newInstance(arrayType, 0);
                    }
                    else
                    {
                        array = (Object[]) columnArray.getArray();
                    }

                    if (arrayType.equals(String.class) ||
                            arrayType.equals(Boolean.class) ||
                            arrayType.equals(Integer.class) ||
                            arrayType.equals(Long.class) ||
                            arrayType.equals(Float.class) ||
                            arrayType.equals(Double.class) ||
                            arrayType.equals(Timestamp.class))
                    {
                        field.set(object, array);
                    }
                    else if (arrayType.isEnum())
                    {
                        Object[] enumArray = (Object[]) Array.newInstance(arrayType, array.length);

                        for (int j = 0; j < array.length; j++)
                        {
                            enumArray[j] = enumValue(arrayType, array[j].toString());
                        }

                        field.set(object, enumArray);
                    }
                }
            }

            return object;
        }
        catch (Exception e)
        {
            throw new DatabaseException(e);
        }
    }

    private Object enumValue(Class<?> clazz, String text)
    {
        String input = text.toUpperCase();

        for (Object value : clazz.getEnumConstants())
        {
            if (value.toString().equals(input))
            {
                return value;
            }
        }

        return null;
    }
}