package com.input;

import com.ErrorHandler;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.input.ConfigurationTest.testEventList;
import static com.input.DataType.*;
import static com.input.DataType.STRING;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.input.LogData;

public class LogDataTest
{

    private Configuration config = new Configuration();
    private List<String> inputData = new ArrayList<>();
    static List<DataID> testDataIDList = new ArrayList<>();
    private Event event;




    // test case naming convention: MethodName_StateUnderTest_ExpectedBehavior
    @Test
    void MethodName_StateUnderTest_ExpectedBehavior() {
        System.out.println("**--- Test method1 executed ---**");
    }


    @Test
    void test0() {
        URL configurationFileLoc = getClass().getClassLoader().getResource("sample_config_file.xml");
        inputData = new ArrayList<>(Arrays.asList("true", "0"));
        event = new Event("System_Initialization", Arrays.asList(new DataID("Status", BOOLEAN),
                new DataID("Error_Code", INTEGER)));
        if (configurationFileLoc == null) {
            fail("Test resource \"sample_config_file.xml\" was not found.");
        }
        boolean isParsed = config.parseConfigFile(configurationFileLoc);
        assertEquals(true, isParsed);
        System.out.println("**--- Test test0 executed ---**");
    }


    /**
     * Test if the logfile is not valid, Loafr will not crash.
     */
    @Test
    void Test0_invalid_logData_position(){
        URL configurationFileLoc = getClass().getClassLoader().getResource("sample_config_file.xml");
        Configuration configuration = new Configuration();
        configuration.parseConfigFile(configurationFileLoc);
        String someRandomString = "good_morning_everybody_this_is_meaningless";
        LogData logData = new LogData();
        logData.parseLogFile(someRandomString,configuration);
        System.out.println("**--- Test0_invalid_logData_position executed ---**");
    }


    @Test
    void Test1_parse_log_file_regular(){
        URL configurationFileLoc = getClass().getClassLoader().getResource("sample_config_file.xml");
        String path1 = "./src/test/resources/log_file_1";
        Configuration configuration = new Configuration();
        configuration.parseConfigFile(configurationFileLoc);
        LogData logData = new LogData();
        boolean parsingStatus = logData.parseLogFile(path1, configuration);
        assertNotNull(logData);
        assertEquals(10,logData.getEventList().size());
        assertTrue(parsingStatus);
        assertFalse(logData.getEventList().isEmpty());
        System.out.println("**--- Test1_parse_log_file_regular executed ---**");
    }



}
