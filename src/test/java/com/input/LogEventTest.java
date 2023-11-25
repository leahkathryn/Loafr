package com.input;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.input.DataType.BOOLEAN;
import static com.input.DataType.INTEGER;
import static org.junit.jupiter.api.Assertions.*;

public class LogEventTest {
    private Configuration config= new Configuration();
    private List<String> inputData = new ArrayList<>();
    private Event event;

    @BeforeAll
    static void setupTest0(){

    }
    @Test
    void test0(){
        URL configurationFileLoc = getClass().getClassLoader().getResource("sample_config_file.xml");
        LogEvent testLogEvent = new LogEvent();
        inputData = new ArrayList<>(Arrays.asList("true", "0"));
        event = new Event("System_Initialization", Arrays.asList(new DataID("Status", BOOLEAN), new DataID("Error_Code", INTEGER)));
        if (configurationFileLoc == null){
            fail("Test resource \"sample_config_file.xml\" was not found.");
        }
        boolean isParsed = config.parseConfigFile(configurationFileLoc);
    }
}

/**
 * Notes for me and whoever needs to write this test class:
 *  Assumes that event in the line of the logfile is valid
 *  dataValuesInput/inputData is the string representation of data in a line of a valid log file
 *  eventMatch/event is an event object parsed from a line in a log file
 */