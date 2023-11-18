package com.analyze;

import java.util.List;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.input.LogData;
import com.input.LogEvent;
import com.input.DataID;

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


    @Override
    public <T> LogData execute(LogData logObject) {
        LogData output = new LogData();
        HashMap<DataID,List<T>> data;
        events = logObject.getEventList();

        if (null == attributeType) {
            // any field looking for match, get attribute type -> make string then do search
            // also read attribute of logevent
            for (LogEvent event : events) {
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(event.getTimeStamp().toString());
                if (matcher.find()) { //Checks if timestamp matches regex
                    output.addLogEvent(event);
                    continue;
                }
                Matcher matcher1 = pattern.matcher(event.getEventType().toString());
                if (matcher1.find()) { //Checks if event type matches regex
                    output.addLogEvent(event);
                    continue;
                }
                data = event.getDataIDMap();
                outerloop:
                for (List<T> dataList : data.values()) { //parses through all dataid keys
                    for (T dataValue : dataList) { //parses list for each dataId
                        Matcher matcher2 = pattern.matcher(dataValue.toString());
                        if (matcher2.find()) {
                            output.addLogEvent(event);
                            break outerloop; //breaks loop since event has been added
                        }
                    }
                }
            }
        }
        else if (LogEvent.AttributeType.DATAVALUE == attributeType) {
            //use getdatavalue function in logevent class
            for (LogEvent event : events) {
                HashMap<DataID, List<T>> dataMap = event.getDataIDMap();
                List<T> dataValues = dataMap.get(dataID);
                for (T dataPoint : dataValues) { //parses list for each data value
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(dataPoint.toString());
                    if (matcher.find()) {
                        output.addLogEvent(event);
                        break;
                    }
                }
            }
        }
        else {
            // timestamp, event, dataID, checks for any data type
            // switch on the attribute type, for timestamp and event get info from logevent class
            // dataID look for dataID key name
            // dataID.getType for data type search
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
                        Matcher matcher1 = pattern.matcher(event.getEventType().toString());
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
                        System.out.println("The requested attribute type has not been implemented or does not exist.")

                }

            }
        }

        return output;
    }
}

