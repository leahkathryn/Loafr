package com.input;
import java.util.ArrayList;

// This class is simple rename the log data. Data1, Data2, Data3...
// I simply change them into a arraylist of string, I will deal this the data types later.
public class LogData {
    ArrayList<String[]> logData = new ArrayList<>();

    public static int getSize(LogData data){
        return data.logData.size();
    }


    public static LogData constructLogData(ArrayList<String[]> data){
        LogData newLogData = new LogData();
        newLogData.logData = data;
        return newLogData;
    }
}
