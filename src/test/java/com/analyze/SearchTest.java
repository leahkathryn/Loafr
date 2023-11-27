package com.analyze;

import com.input.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

import com.ErrorHandler;


public class SearchTest
{
    static LogData inputLogFile = new LogData();
    static LogData result1 = new LogData();
    static LogData result2 = new LogData();
    static LogData result3 = new LogData();
    static LogData resultempty = new LogData();
    Search search;

    @BeforeAll
    static void setupTest()
    {
        //Sets up test LogData for search to analyze
        LogEvent logEvent = new LogEvent();
        List<String> inputData = Arrays.asList("true", "987651", "anotherTest");
        Event event = new Event("Sample_Event", Arrays.asList(
                new DataID("Status", DataType.BOOLEAN),
                new DataID("Numbers", DataType.INTEGER),
                new DataID("Text", DataType.STRING)
        ));
        HashMap<DataID, List<?>> dataMap = logEvent.convertInputToDataMap(inputData, event);
        logEvent.setDataIDMap(dataMap);
        Timestamp newTimeStamp = parseTimeStamp("2023-11-12 08:30:00");
        logEvent.setTimeStamp(newTimeStamp);
        logEvent.setEventType("Thruster_Calibration");
        inputLogFile.addLogEvent(logEvent);
        result1.addLogEvent(logEvent);
        result3.addLogEvent(logEvent);

        LogEvent logEvent2 = new LogEvent();
        List<String> inputData2 = Arrays.asList("true", "987652", "anotherTest");
        Event event2 = new Event("Sample_Event", Arrays.asList(
                new DataID("Status", DataType.BOOLEAN),
                new DataID("Numbers", DataType.INTEGER),
                new DataID("Text", DataType.STRING)
        ));
        HashMap<DataID, List<?>> dataMap2 = logEvent.convertInputToDataMap(inputData2, event2);
        logEvent2.setDataIDMap(dataMap2);
        Timestamp newTimeStamp2 = parseTimeStamp("2023-11-13 08:30:00");
        logEvent2.setTimeStamp(newTimeStamp2);
        logEvent2.setEventType("Thruster_Calibration");
        inputLogFile.addLogEvent(logEvent2);
        result1.addLogEvent(logEvent2);

        LogEvent logEvent3 = new LogEvent();
        List<String> inputData3 = Arrays.asList("false", "987653", "anotherTest");
        Event event3 = new Event("Sample_Event", Arrays.asList(
                new DataID("Status", DataType.BOOLEAN),
                new DataID("Numbers", DataType.INTEGER),
                new DataID("Text", DataType.STRING)
        ));
        HashMap<DataID, List<?>> dataMap3 = logEvent.convertInputToDataMap(inputData3, event3);
        logEvent3.setDataIDMap(dataMap3);
        Timestamp newTimeStamp3 = parseTimeStamp("2023-11-14 08:30:00");
        logEvent3.setTimeStamp(newTimeStamp3);
        logEvent3.setEventType("Navigation_Test");
        inputLogFile.addLogEvent(logEvent3);
        result2.addLogEvent(logEvent3);
    }

    //Translates string to Timestamp, From Yichen
    private static Timestamp parseTimeStamp(String timeStampString)
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

    // test case naming convention: MethodName_StateUnderTest_ExpectedBehavior
    @Test
    void MethodName_StateUnderTest_ExpectedBehavior() {
        System.out.println("**--- Test method1 executed ---**");
    }

    //Tests searches for matching regex in any field for event
    @Test
    void Execute_MatchingEventType_Success() {
        search = new Search("Navigation_Test"); //Search constructor
        LogData result = search.execute(inputLogFile); //Execute search
        assertEquals(result2.getEventList(), result.getEventList()); //Compares event list to see match

        // Print statement indicating the test completion
        System.out.println("**--- Test Execute_MatchingEventType_Success executed ---**");
    }

    //Tests searches for matching regex in any field for data
    @Test
    void Execute_MatchingData_Success() {
        search = new Search("false"); //Search constructor
        LogData result = search.execute(inputLogFile);
        assertEquals(result2.getEventList(), result.getEventList());

        // Print statement indicating the test completion
        System.out.println("**--- Test Execute_MatchingData_Success executed ---**");
    }

    //Test searches for case where regex does not match any data
    @Test
    void Execute_MatchingData_Failure() {
        search = new Search("nonData"); //Search constructor
        LogData result = search.execute(inputLogFile);
        assertEquals(resultempty.getEventList(), result.getEventList());

        // Print statement indicating the test completion
        System.out.println("**--- Test Execute_MatchingData_Failure executed ---**");
    }

    //Tests searches for matching Event in the Event field
    @Test
    void Execute_MatchingAttributeEvent_Success() {
        search = new Search(LogEvent.AttributeType.EVENT, "Thruster_Calibration"); //Search constructor
        LogData result = search.execute(inputLogFile);
        assertEquals(result1.getEventList(), result.getEventList());

        // Print statement indicating the test completion
        System.out.println("**--- Test Execute_MatchingAttributeEvent_Success executed ---**");
    }

    //Tests searches for no matching Events in the Event field and will not look at all fields
    @Test
    void Execute_MatchingAttributeEvent_Fail() {
        search = new Search(LogEvent.AttributeType.EVENT, "true"); //Search constructor
        LogData result = search.execute(inputLogFile);
        assertNotEquals(result1.getEventList(), result.getEventList());

        // Print statement indicating the test completion
        System.out.println("**--- Test Execute_MatchingAttributeEvent_Fail executed ---**");
    }
}
