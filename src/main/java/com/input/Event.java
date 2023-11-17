package com.input;

import java.util.List;

public class Event
{
    String name;
    List<DataID> data;

    /**
     * Constructor.
     * @param name name provided.
     * @param data data provided.
     */
    public Event(String name, List<DataID> data)
    {
        this.name = name;
        this.data = data;
    }

    /**
     * Getter
     * @return list.
     */
    public List<DataID> getDataIDList()
    {
        return data;
    }
}
