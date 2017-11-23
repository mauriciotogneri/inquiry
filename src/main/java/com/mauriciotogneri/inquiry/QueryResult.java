package com.mauriciotogneri.inquiry;

import java.util.ArrayList;

public class QueryResult<T> extends ArrayList<T>
{
    public QueryResult(int initialCapacity)
    {
        super(initialCapacity);
    }

    public boolean hasElements()
    {
        return !isEmpty();
    }

    public T first()
    {
        return get(0);
    }
}