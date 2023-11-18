package com.input;

import com.ErrorHandler;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;


public class LogEvent<T> {
    private List<LogEvent> eventList = new ArrayList<>();



    /**
     *
     * @Pre-condition: The input must represent a valid file location.
     * @Post-condition: This LogObject instance represents the information in the log file.
     * @param fileName the logfile location.
     * @param configuration the configuration from the configuration class.
     * @return return the status of the parsing status.
     */
    public boolean parseLogFile(String fileName, Configuration configuration) throws IOException{
        List<Event> events = configuration.getEventList();

        File logFile = new File(fileName);
        // Check if the log file exists and is readable
        if (!logFile.exists() || !logFile.isFile() || !logFile.canRead()) {
            ErrorHandler.logError("Invalid or unreadable log file.");
            return false;
        }
        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

            int linesSize = lines.size();
            System.out.println(linesSize);

            ArrayList<String> nameEventList = new ArrayList<>();
            for (int j = 0; j < events.size(); j++){
                nameEventList.add(events.get(j).name);
            }

            for (int i = 0; i < linesSize; i++) {
                // change the line to a LogEvent here
                // 1. separate them by comma
                String toSplit = lines.get(i);
                String[] values = toSplit.split(",");

                // 2. create Timestamp
                // Define the date format
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                // Parse the timestamp string and create a Date object
                Date parsedDate = dateFormat.parse(values[0]);

                // Create a Timestamp object from the parsed Date
                Timestamp timestamp = new Timestamp(parsedDate.getTime());
//                System.out.println(timestamp);
                // 3. create DataID
                String originalDataID = values[1];
                // if the original dataID's name is not in the event, skip it.
                if (!nameEventList.contains(originalDataID)){
                    continue;
                }
                DataID dataID = new DataID();
                dataID.setName(originalDataID);
                dataID.setType(DataType.STRING);
//                System.out.println(dataID.getName());
                // 4. create data Hashmap
                LogEvent<Object> logEvent = new LogEvent<>();
                logEvent.setTimeStamp(timestamp);
                logEvent.setEventType(dataID.getName());
                // Convert the string array to a List<Map<String, Object>>
                String[] dataValuesStringArr = new String[values.length - 2];
                for (int j = 2; j < values.length; j++){
                    dataValuesStringArr[j-2] = values[j];
                }
                String dataValuesString = dataValuesStringArr.toString();
                HashMap<String, Object> resultMapList = logEvent.convertStringToDataMap(dataValuesString);
                //5. create LogEvent and store it in the list.
                LogEvent newLogEvent = new LogEvent();
                newLogEvent.setTimeStamp(timestamp);
                newLogEvent.setEventType(dataID.getName());
                newLogEvent.setDataIDMap(resultMapList);
                this.eventList.add(newLogEvent);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return true;
    }


    /** The method to output the data that is being analyzed.
     * @param outputLoc The location to be written.
     * @return return the status of output.
     */
    public boolean writeLogObject(String outputLoc){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputLoc))) {
            for (LogEvent<T> logEvent : eventList) {
                writer.write(logEvent.getTimeStamp() + "," + logEvent.getEventType() + ", ");
                HashMap<DataID, List<T>> dataIDMap = logEvent.getDataIDMap();

                for (List<T> dataList : dataIDMap.values()) {
                    for (T data : dataList) {
                        writer.write(data + ",");
                    }
                }
                writer.newLine();
            }
            return true; // Successfully wrote the log data to the output file
        } catch (IOException e) {
            ErrorHandler.logError("Error while writing log data to file: " + e.getMessage());
            return false; // Failed to write the log data to the output file
        }
    }


    public List<LogEvent> getEventList() {
        return this.eventList;
    }

    public void addLogEvent (LogEvent event, Integer index)
    {
        this.eventList.add(index, event);
    }

    public void addLogEvent (LogEvent event){
        this.eventList.add(event);
    }

    public void removeLogEvent(LogEvent event){
        this.eventList.remove(event);
    }
}
