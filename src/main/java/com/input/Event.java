package com.input;

import java.util.List;

public class Event
{
    String name;
    List<DataID> data;

    public Event(String name, List<DataID> data)
    {
        this.name = name;
        this.data = data;
    }

    public List<DataID> getDataIDList()
    {
        return data;
    }
}
