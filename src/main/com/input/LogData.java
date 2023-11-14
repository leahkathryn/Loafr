package com.input;
import java.util.ArrayList;

// This class is simple rename the log data. Data1, Data2, Data3...
// I simply change them into a arraylist of string, I will deal this the data types later.
public class LogData {
    private ArrayList<String[]> logData = new ArrayList<>();
    private List<LogEvent> eventList;

    public boolean parseLogFile(String fileName, Configuration configuration)
    {
        List<Event> events = configuration.getEventList();

        foreach(Event event : events)
        {


        }

        return true;
    }


    /*
    public static int getSize(LogData data){
        return data.logData.size();
    }
    */
}
