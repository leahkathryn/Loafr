package com.analyze;

import com.input.DataID;
import com.input.LogEvent;

public class Range
{
    private LogEvent.AttributeType startAttributeType;
    private DataID startDataID;
    private String startRegex;
    private LogEvent.AttributeType endAttributeType;
    private DataID endDataID;
    private String endRegex;

    public LogEvent.AttributeType getStartAttributeType() {return startAttributeType;}
    public DataID getStartDataID() {return startDataID;}
    public String getStartRegex() {return startRegex;}
    public LogEvent.AttributeType getEndAttributeType() {return endAttributeType;}
    public DataID getEndDataID() {return endDataID;}
    public String getEndRegex() {return endRegex;}

    // constructor
    public Range(LogEvent.AttributeType startAttributeType, DataID startDataID, String startRegex,
                 LogEvent.AttributeType endAttributeType, DataID endDataID, String endRegex)
    {
        this.startAttributeType = startAttributeType;
        this.startDataID = startDataID;
        this.startRegex = startRegex;
        this.endAttributeType = endAttributeType;
        this.endDataID = endDataID;
        this.endRegex = endRegex;
    }
}
