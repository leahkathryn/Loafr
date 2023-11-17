package com.analyze;

import java.util.List;

public class Search implements AnalysisTask {

    private AttributeType attributeType;
    private DataID dataID;
    private String regex;
    private List<LogEvents> events;
    private List<LogEvents> outputEvents;
    private LogEvents temp;



    public Search(AttributeType type, DataID data, String reg) {
        attributeType = type;
        dataID = data;
        regex = reg;
    }

    public Search(AttributeType type, String reg) {
        attributeType = type;
        dataID = "";
        regex = reg;
    }

    public Search(String reg) {
        attributeType = null;
        dataID = "";
        regex = reg;
    }


    @Override
    public LogData execute(LogData logObject) {
        LogData output = new LogData();
        events = logObject.getLogEvents();
        for (LogEvent event : events) {
            temp = events.get(i);
            if (null == attributeType) {
                // any field looking for match, get attribute type -> make string then do search
            }
            else if (LogEvent.AttributeType.DATAVALUE == attributeType) {
                //use getdatavalue function in logevent class
            }
            else {
                // use getattribute function to search
            }

        }
       return output;
    }
}

