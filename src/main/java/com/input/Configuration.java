package com.input;

import java.util.List;

public class Configuration
{
    private List<Event> eventList;
    private List<DataID> dataIDList;
    private String defaultOutputLoc;

    public Configuration(String defaultOutputLoc)
    {
        this.defaultOutputLoc = defaultOutputLoc;
    }

    public List<Event> getEventList()
    {
        return eventList;
    }
    public String getDefaultOutputLocation() { return defaultOutputLoc; }
}
