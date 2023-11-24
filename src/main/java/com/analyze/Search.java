package com.analyze;

import java.util.List;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ErrorHandler;
import com.input.LogData;
import com.input.LogEvent;
import com.input.DataID;
import com.input.LogEvent.AttributeType;


public class Search implements AnalysisTask {

    private AttributeType attributeType;
    private DataID dataID;
    private String regex;
    private List<LogEvent> events;
    private List<LogEvent> outputEvents;
    private LogEvent temp;


    public Search(AttributeType type, DataID data, String reg) {
        attributeType = type;
        dataID = data;
        regex = reg;
    }

    public Search(AttributeType type, String reg) {
        attributeType = type;
        dataID = null;
        regex = reg;
    }

    public Search(String reg) {
        attributeType = null;
        dataID = null;
        regex = reg;
    }

    // getter functions used during unit testing
    @Override
    public AttributeType getAttributeType()
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

    //Executes a search function on the inputted LogData.
    //Takes in a LogData object and returns a LogData object.
    @Override
    public <T> LogData execute(LogData logObject) {
        LogData output = new LogData();
        HashMap<DataID, List<?>> data;
        events = logObject.getEventList();

        //Searches all fields in the LogEvent.
        if (null == attributeType) {
            for (LogEvent event : events) {
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(event.getTimeStamp().toString());
                if (matcher.find()) {
                    output.addLogEvent(event);
                    continue;
                }
                Matcher matcher1 = pattern.matcher(event.getEventType().toString());
                if (matcher1.find()) {
                    output.addLogEvent(event);
                    continue;
                }
                data = event.getDataIDMap();
                outerloop:
                for (List<?> dataList : data.values()) { //parses through all DataiID keys
                    for (Object dataValue : dataList) { //parses list for each dataId
                        Matcher matcher2 = pattern.matcher(dataValue.toString());
                        if (matcher2.find()) {
                            output.addLogEvent(event);
                            break outerloop; //breaks loop since event has been added
                        }
                    }
                }
            }
        } else if (LogEvent.AttributeType.DATAVALUE == attributeType) { //Searches all DataID fields.
            for (LogEvent event : events) {
                HashMap<DataID, List<?>> dataMap = event.getDataIDMap();
                List<?> dataValues = dataMap.get(dataID);
                for (Object dataPoint : dataValues) { //parses list for each data value
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(dataPoint.toString());
                    if (matcher.find()) {
                        output.addLogEvent(event);
                        break;
                    }
                }
            }
        } else {
            // Searches the attributeType field specified in the script
            for (LogEvent event : events) {
                Pattern pattern = Pattern.compile(regex);
                switch (attributeType) {
                    case TIMESTAMP: //Checks if timestamp matches regex
                        Matcher matcher = pattern.matcher(event.getTimeStamp().toString());
                        if (matcher.find()) {
                            output.addLogEvent(event);
                        }
                        break;

                    case EVENT: //Checks if event matches regex
                        Matcher matcher1 = pattern.matcher(event.getEventType());
                        if (matcher1.find()) {
                            output.addLogEvent(event);
                        }
                        break;

                    case DATAID: //Checks if DataID keys match regex
                        data = event.getDataIDMap();
                        for (DataID dataKey : data.keySet()) {
                            Matcher matcher2 = pattern.matcher(dataKey.toString());
                            if (matcher2.find()) {
                                output.addLogEvent(event);
                                break;
                            }
                        }
                        break;

                    case DATATYPE: //Checks if Data type matches regex
                        data = event.getDataIDMap();
                        for (DataID dataKey : data.keySet()) {
                            Matcher matcher3 = pattern.matcher(dataKey.getType().toString());
                            if (matcher3.find()) {
                                output.addLogEvent(event);
                                break;
                            }
                        }
                        break;

                    default:
                        ErrorHandler.logError("The requested attribute type is not implemented by Loafr.");
                }
            }
        }
        return output;
    }
}

