package com.input;

import com.ErrorHandler;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.input.LogData;

public class LogDataTest
{

    String sampleLogEvent = "2023-11-12 08:30:00,Communication_Check,success,0,[32.5 54.4 76.8],100\n";

    @BeforeAll
    static void beforeAll()
    {
        // test class set up
    }

    // test case naming convention: MethodName_StateUnderTest_ExpectedBehavior
    @Test
    void MethodName_StateUnderTest_ExpectedBehavior() {
        System.out.println("**--- Test method1 executed ---**");
    }

    @Test
    void testTimeStamp(){
        LogEvent newLogEvent = new LogEvent();
        String[] splitString = sampleLogEvent.split(",");
        Timestamp newTimeStamp = parseTimeStamp(splitString[0]);
        newLogEvent.setTimeStamp(newTimeStamp);
        assertNotNull(newLogEvent.getTimeStamp());
        System.out.println("**--- Test TimeStamp executed ---**");
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
}
