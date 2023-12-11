package com.analyze;

import com.input.DataID;
import com.input.LogData;
import com.input.LogEvent;

import java.util.List;

public class Filter implements AnalysisTask
{
    private LogEvent.AttributeType attributeType;
    private DataID dataID;
    private String regex;
    private Range range;
    private List<LogEvent> events;

    public Filter(LogEvent.AttributeType attributeType, DataID dataID, String regex)
    {
        this.attributeType = attributeType;
        this.dataID = dataID;
        this.regex = regex;
        this.range = null;
    }

    public void setRange(Range range)
    {
        this.range = range;
    }

    @Override
    public <T> LogData execute(LogData logData)
    {
        LogData output = new LogData();
        events = logData.getEventList();

        if (null != range)
        {
            output = executeRangeFilter();
        }

        return output;
    }

    // getter functions used during unit testing
    @Override
    public LogEvent.AttributeType getAttributeType()
    {
        return attributeType;
    }
    @Override
    public DataID getDataID()
    {
        return dataID;
    }
    @Override
    public String getRegex()
    {
        return regex;
    }

    private LogData executeRangeFilter()
    {
        LogData output = new LogData();

        // Range logic goes here
        // consider the Range class as a data structure that holds the "range" beginning and end

        return output;
    }
}
