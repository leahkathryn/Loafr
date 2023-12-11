package com.analyze;

import com.ErrorHandler;
import com.input.DataID;
import com.input.LogData;
import com.input.LogEvent;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;

import java.text.SimpleDateFormat;
import java.sql.Timestamp;
import java.util.Date;

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
            output = executeRangeFilter(events, this.range);
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

    private LogData executeRangeFilter(List<LogEvent> events, Range range) throws ParseException {
        LogData output = new LogData();

        // Range logic goes here
        // consider the Range class as a data structure that holds the "range" beginning and end
        for (LogEvent event : events) {
            switch (attributeType) {
                case TIMESTAMP -> {
                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        Date parsedDate = dateFormat.parse(event.getEventType());
                        Timestamp searchTime = new java.sql.Timestamp(parsedDate.getTime());
                        parsedDate = dateFormat.parse(range.getStartRegex());
                        Timestamp startTime = new java.sql.Timestamp(parsedDate.getTime());
                        parsedDate = dateFormat.parse(range.getEndRegex());
                        Timestamp endTime = new java.sql.Timestamp(parsedDate.getTime());
                        if (searchTime.after(startTime) && searchTime.before(endTime)) {
                            output.addLogEvent(event);
                        }
                    } catch(Exception e) {
                        System.out.println("Parse error");
                    }

                }
                case DATAVALUE -> { //Checks if event matches regex
                    HashMap<DataID, List<?>> data;
                    data = event.getDataIDMap();
                    for (List<?> dataList : data.values()) { //parses through all DataiID keys
                        for (Object dataValue : dataList) { //parses list for each dataId
                            String value = dataValue.toString();
                            if (value.compareTo(range.getStartRegex()) == 1) {
                                if (value.compareTo(range.getEndRegex()) == -1) {
                                    output.addLogEvent(event);
                                }
                            }
                        }
                    }

                }
                case DATAID -> { //Checks if DataID keys match regex

                }
                case DATATYPE -> { //Checks if Data type matches regex

                }
                default -> ErrorHandler.logError("The requested attribute type is not implemented by Loafr.");
            }
        }

        return output;
    }
}
