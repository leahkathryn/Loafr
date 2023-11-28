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

/**
 * @author Yichen Li
 */
public class LogData
{
    private List<LogEvent> eventList = new ArrayList<>();

    /**
     * @Pre-condition: The input must represent a valid file location.
     * @Post-condition: This LogObject instance represents the information in the log file.
     * @param fileName the logfile location.
     * @param configuration the configuration from the configuration class.
     * @return return the status of the parsing status.
     */
    public boolean parseLogFile(String fileName, Configuration configuration)
    {
        File logFile = new File(fileName);
        // Check if the log file exists and is readable
        if (!logFile.exists() || !logFile.isFile() || !logFile.canRead()) {
            ErrorHandler.logError("Failure opening log file: File not found.\nLoafr exiting...");
            return false;
        }

        // Read each line of log file into a list
        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        catch (IOException e) {
            ErrorHandler.logError("Failure opening log file: IOException.\nLoafr exiting...");
            return false;
        }

        // for easy access, make a map of event names to event instances
        List<Event> events = configuration.getEventList();
        HashMap<String,Event> nameEventMap = new HashMap<>();
        for (Event event : events)
        {
            nameEventMap.put(event.name,event);
        }

        // Parse each line of the log file into a LogEvent
        for (String line : lines)
        {
            // Separate each value in the line by comma and store them in a list.
            if (!line.contains(",")){
                continue;
            }
            String[] values = line.split(",");
            List<String> dataValuesInput = new ArrayList<>();
            // Remove leading/trailing whitespace and add to ArrayList
            for (String str : values)
            {
                dataValuesInput.add(str.strip());
            }

            // Create Timestamp
            if (dataValuesInput.size() == 0){
                System.out.println("This is not valid, please check the logfile.");
                break;
            }
            Timestamp timestamp = parseTimeStamp(dataValuesInput.get(0));

            // Create LogEvent
            String originalEvent = dataValuesInput.get(1);
            // If the input event name is not defined by the config file, Loafr fails.
            if (!nameEventMap.containsKey(originalEvent)){
                ErrorHandler.logError("Failure parsing log file: there is an unrecognized event listed in this " +
                        "log file: " + originalEvent + ".\nLoafr exiting...");
                return false;
            }
            Event eventMatch = nameEventMap.get(originalEvent);

            // Remove timestamp and event name from input list: now it only contains data values
            dataValuesInput.remove(0);
            dataValuesInput.remove(0);

            // Create LogEvent
            LogEvent logEvent = new LogEvent();
            logEvent.setTimeStamp(timestamp);
            logEvent.setEventType(originalEvent);

            // Convert String data values to a DataIDMap and set it.
            logEvent.setDataIDMap(logEvent.convertInputToDataMap(dataValuesInput,eventMatch));

            if (logEvent.getDataIDMap().isEmpty())
            {
                // ErrorHandler already sent message
                return false;
            }
            this.eventList.add(logEvent);
        }
        return true;
    }

    private Timestamp parseTimeStamp(String timeStampString)
    {
        // Define the date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // Parse the timestamp string and create a Date object
        Date parsedDate;
        try {
            parsedDate = dateFormat.parse(timeStampString);
        }
        catch (ParseException ex)
        {
            ErrorHandler.logError("Failure parsing time stamp.\nLoafr exiting...");
            return null;
        }

        // Create a Timestamp object from the parsed Date
        return new Timestamp(parsedDate.getTime());
    }

    /** The method to output the data that is being analyzed.
     * @param outputLoc      The location to be written.
     * @param configuration  the Configuration that defines Events and DataIDs
     * @return               return the status of output.
     */
    public boolean writeLogData(String outputLoc, Configuration configuration)
    {
        /**************************************************/
        // This part is for grading, so that a grader can run Loafr
        // and the output will be put in an "output" directory in their current
        // working directory. If they choose, they can edit the config file for a new
        // default output location, or they can provide an output location.
        // Leah Lehmeier
        if (outputLoc.equals("."))
        {
            File dir = new File("output");
            if (!dir.mkdir())
            {
                ErrorHandler.logError("Error creating output directory in current working directory." +
                        "\nChange permissions or provide a valid output location.");
                return false;
            }
            File output = new File(dir, "output.txt");
            try {
                output.createNewFile();
            }
            catch (IOException ex)
            {
                ErrorHandler.logError("Error while writing log data to file: " + ex.getMessage());
                return false; // Failed to write the log data to the output file
            }
            outputLoc = output.getAbsolutePath();
        }
        /**********************************************/

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputLoc))) {
            for (LogEvent logEvent : eventList) {
                writer.write(logEvent.getTimeStamp() + "," + logEvent.getEventType() + ",");
                HashMap<DataID, List<?>> dataIDMap = logEvent.getDataIDMap();

                // there is probably a better way to do this - quick fix
                Event thisEvent = null;
                for (Event event : configuration.getEventList())
                {
                    if (event.name.equals(logEvent.getEventType()))
                    {
                        thisEvent = event;
                        break;
                    }
                }
                if (null == thisEvent)
                {
                    ErrorHandler.logError("Error while writing log data to file: event type unknown.");
                    return false;
                }
                // write the DataIDs in the correct order in the file
                for (DataID dataID : thisEvent.getDataIDList())
                {
                    List<?> dataList = dataIDMap.get(dataID);
                    if (dataList.size() > 1)
                    {
                        writer.write("[");
                        writer.write(dataList.get(0).toString());
                        for (int i = 1; i < dataList.size(); i++)
                        {
                            writer.write(" " + dataList.get(i));
                        }
                        writer.write("]");
                    }
                    else
                    {
                        writer.write(dataList.get(0).toString());
                    }
                    if (thisEvent.getDataIDList().indexOf(dataID) < thisEvent.getDataIDList().size()-1)
                    {
                        writer.write(",");
                    }
                }
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            ErrorHandler.logError("Error while writing log data to file: " + e.getMessage());
            return false;
        }
    }


    public List<LogEvent> getEventList()
    {
        return eventList;
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
