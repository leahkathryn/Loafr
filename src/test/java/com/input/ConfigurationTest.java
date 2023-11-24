package com.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.input.DataType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


public class ConfigurationTest {
    static List<Event> testEventList = new ArrayList<>();
    static List<DataID> testDataIDList = new ArrayList<>();
    private Configuration config;

    @BeforeEach
    void clearConfig()
    {
        config = new Configuration();
    }

    @BeforeAll
    static void setupTest0() {
        testEventList.add(new Event("System_Initialization", new ArrayList<DataID>(Arrays.asList(
                new DataID("Status", BOOLEAN),
                new DataID("Error_Code", INTEGER)
        ))));
        testEventList.add(new Event("Communication_Check", new ArrayList<DataID>(Arrays.asList(
                new DataID("Result", STRING),
                new DataID("Error_Code", INTEGER),
                new DataID("Frequencies", FLOAT),
                new DataID("Signal_Strength", INTEGER)
        ))));
        testEventList.add(new Event("Navigation_Test", new ArrayList<DataID>(Arrays.asList(
                new DataID("Result", STRING),
                new DataID("Error_Code",INTEGER),
                new DataID("Accuracy", FLOAT)
        ))));
        testEventList.add(new Event("Telemetry_Check", new ArrayList<DataID>(Arrays.asList(
                new DataID("Result", STRING),
                new DataID("Error_Code", INTEGER),
                new DataID("Metrics", FLOAT)
        ))));
        testEventList.add(new Event("Battery_Test", new ArrayList<DataID>(Arrays.asList(
                new DataID("Result", STRING),
                new DataID("Error_Code", INTEGER),
                new DataID("Battery_Level", INTEGER),
                new DataID("Charging", BOOLEAN),
                new DataID("Remaining_Capacity", INTEGER),
                new DataID("Voltage", INTEGER)
        ))));
        testEventList.add(new Event("Redundancy_Check", new ArrayList<DataID>(Arrays.asList(
                new DataID("Result", STRING),
                new DataID("Error_Code", INTEGER),
                new DataID("Redundancy_Levels", INTEGER),
                new DataID("Redundancy_Percentage", FLOAT)
        ))));
        testEventList.add(new Event("Thruster_Calibration", new ArrayList<DataID>(Arrays.asList(
                new DataID("Status", BOOLEAN),
                new DataID("Error_Code",INTEGER),
                new DataID("Power_Levels",INTEGER),
                new DataID("Fuel_Remaining", DOUBLE)
        ))));
        testEventList.add(new Event("Pre-Launch_Check", new ArrayList<DataID>(Arrays.asList(
                new DataID("Status", BOOLEAN),
                new DataID("Error_Code",INTEGER),
                new DataID("Authorization", BOOLEAN),
                new DataID("Result", STRING),
                new DataID("Probability_of_Success", FLOAT)
        ))));
        testEventList.add(new Event("Engine_Ignition", new ArrayList<DataID>(Arrays.asList(
                new DataID("Status", BOOLEAN),
                new DataID("Error_Code",INTEGER),
                new DataID("Fuel_Type", STRING),
                new DataID("Fuel_Remaining", DOUBLE),
                new DataID("Thrust_Power", INTEGER)
        ))));
        testEventList.add(new Event("Orbital_Maneuver", new ArrayList<DataID>(Arrays.asList(
                new DataID("Status", BOOLEAN),
                new DataID("Thrust_Power", INTEGER),
                new DataID("Angles", DOUBLE),
                new DataID("Metrics", FLOAT)
        ))));
        testEventList.add(new Event("Solar_Panel_Deployment", new ArrayList<DataID>(Arrays.asList(
                new DataID("Status", BOOLEAN),
                new DataID("Panel_Status", STRING),
                new DataID("Error_Code",INTEGER),
                new DataID("Temperature", DOUBLE),
                new DataID("Angles", DOUBLE)
        ))));

        testDataIDList = new ArrayList<>(Arrays.asList(
                new DataID("Temperature", DOUBLE),
                new DataID("Status" , BOOLEAN),
                new DataID("Result" , STRING),
                new DataID("Power_Levels" , INTEGER),
                new DataID("Frequencies" , FLOAT),
                new DataID("Signal_Strength" , INTEGER),
                new DataID("Error_Code" , INTEGER),
                new DataID("Accuracy" , FLOAT),
                new DataID("Panel_Status" , STRING),
                new DataID("Thrust_Power" , INTEGER),
                new DataID("Angles" , DOUBLE),
                new DataID("Battery_Level" , INTEGER),
                new DataID("Charging" , BOOLEAN),
                new DataID("Voltage" , INTEGER ),
                new DataID("Remaining_Capacity" , INTEGER),
                new DataID("Redundancy_Levels" , INTEGER ),
                new DataID("Redundancy_Percentage" , FLOAT),
                new DataID("Launch_ID" , INTEGER),
                new DataID("Authorization" , BOOLEAN),
                new DataID("Probability_of_Success" , FLOAT),
                new DataID("Message" , STRING),
                new DataID("Fuel_Remaining" , DOUBLE),
                new DataID("Metrics" , FLOAT ),
                new DataID("Power_Output" , INTEGER ),
                new DataID("Connection_Status" , BOOLEAN),
                new DataID("Fuel_Type" , STRING)));
    }

    @Test
    void parseConfigFileTestIfSuccessful() {
        URL configurationFileLoc = getClass().getClassLoader().getResource("sample_config_file.xml");

        if (null == configurationFileLoc)
        {
            fail("Test resource \"sample_config_file.xml\" was not found.");
        }

        Boolean isParsed = config.parseConfigFile(configurationFileLoc);
        // verify no parsing errors occurred
        assertEquals(Boolean.TRUE, isParsed);

        // verify DataIDList is correct size
        assertEquals(testDataIDList.size(),config.getDataIDList().size());
        // verify EventList is correct size
        assertEquals(testEventList.size(),config.getEventList().size());

        // verify all DataIDs are stored correctly
        for (int i = 0; i < testDataIDList.size(); i++){
            assertEquals(testDataIDList.get(i).getName(), config.getDataIDList().get(i).getName());
            assertEquals(testDataIDList.get(i).getType(), config.getDataIDList().get(i).getType());
        }

        Event configEvent;
        Event testEvent;
        // verify all Events are stored correctly
        for (int j = 0; j < testEventList.size(); j++){
            configEvent = config.getEventList().get(j);
            testEvent = testEventList.get(j);
            // verify this Event has the correct name
            assertEquals(testEventList.get(j).name, config.getEventList().get(j).name);
            // verify DataIDList for this Event is correct size
            assertEquals(testEvent.getDataIDList().size(),configEvent.getDataIDList().size());
            for (int k = 0; k < testEvent.getDataIDList().size(); k++){
                assertEquals(testEvent.getDataIDList().get(k).getName(), configEvent.getDataIDList().get(k).getName());
            }
        }
    }

    @Test
    void parseConfigFileTestIfNodeInEventsIsNotEvent(){
        URL configurationFileLoc = getClass().getClassLoader().getResource("mock_config_files/sample_config_file_wrong_node_in_events.xml");

        if (null == configurationFileLoc)
        {
            fail("Test resource \"sample_config_file_wrong_node_in_events.xml\" was not found.");
        }

        Boolean isParsed = config.parseConfigFile(configurationFileLoc);
        // parseConfigFile must detect child nodes of "events" that are not named "event", not name attribute of event
        assertEquals(false, isParsed);
    }

    @Test
    void parseConfigFileTestDataIDNodeMissingName(){
        URL configurationFileLoc = getClass().getClassLoader().getResource("mock_config_files/sample_config_file_wrong_dataID_node0.xml");

        if (null == configurationFileLoc)
        {
            fail("Test resource \"sample_config_file_wrong_dataID_node0.xml\" was not found.");
        }

        Boolean isParsed = config.parseConfigFile(configurationFileLoc);
        // verify expected parsing errors occurred
        assertEquals(false, isParsed);
    }

    @Test
    void parseConfigFileTestDataIDNodeMissingType(){
        URL configurationFileLoc = getClass().getClassLoader().getResource("mock_config_files/sample_config_file_wrong_dataID_node1.xml");

        if (null == configurationFileLoc)
        {
            fail("Test resource \"sample_config_file_wrong_dataID_node1.xml\" was not found.");
        }

        Boolean isParsed = config.parseConfigFile(configurationFileLoc);
        // verify expected parsing errors occurred
        assertEquals(false, isParsed);
    }

    @Test
    void parseConfigFileTestMissingDefaultFileLoc(){
        URL configurationFileLoc = getClass().getClassLoader().getResource("mock_config_files/sample_config_file_missing_fileLoc.xml");

        if (null == configurationFileLoc)
        {
            fail("Test resource \"sample_config_file_missing_fileLoc.xml\" was not found.");
        }

        Boolean isParsed = config.parseConfigFile(configurationFileLoc);
        // verify expected parsing errors occurred
        assertEquals(false, isParsed);
    }

    @Test
    void parseConfigFileTestWrongDefaultFileLoc(){
        URL configurationFileLoc = getClass().getClassLoader().getResource("mock_config_files/sample_config_file_defaultFileLocNotElem.xml");

        if (null == configurationFileLoc)
        {
            fail("Test resource \"sample_config_file_defaultFileLocNotElem.xml\" was not found.");
        }

        Boolean isParsed = config.parseConfigFile(configurationFileLoc);
        // verify expected parsing errors occurred
        assertEquals(false, isParsed);
    }

    @Test
    void parseConfigFileTestMissingAllEventNodes(){
        URL configurationFileLoc = getClass().getClassLoader().getResource("mock_config_files/sample_config_file_missing_events.xml");

        if (null == configurationFileLoc)
        {
            fail("Test resource \"sample_config_file_missing_events.xml\" was not found.");
        }

        Boolean isParsed = config.parseConfigFile(configurationFileLoc);
        // verify expected parsing errors occurred
        assertEquals(false, isParsed);
    }

    @Test
    void parseConfigFileTestMissingDataIDNode(){
        URL configurationFileLoc = getClass().getClassLoader().getResource("mock_config_files/sample_config_file_missing_dataID.xml");

        if (null == configurationFileLoc)
        {
            fail("Test resource \"sample_config_file_missing_dataID.xml\" was not found.");
        }

        Boolean isParsed = config.parseConfigFile(configurationFileLoc);
        // verify expected parsing errors occurred
        assertEquals(false, isParsed);
    }

    @Test
    void parseConfigFileTestMissingAllDataIDNodes(){
        URL configurationFileLoc = getClass().getClassLoader().getResource("mock_config_files/sample_config_file_missing_dataIDs.xml");

        if (null == configurationFileLoc)
        {
            fail("Test resource \"sample_config_file_missing_dataIDs.xml\" was not found.");
        }

        Boolean isParsed = config.parseConfigFile(configurationFileLoc);
        // verify expected parsing errors occurred
        assertEquals(false, isParsed);
    }

    @Test
    void parseConfigFileTestMissingAllDataIDNodesInEvent(){
        URL configurationFileLoc = getClass().getClassLoader().getResource("mock_config_files/sample_config_file_missing_dataID_name_in_event.xml");

        if (null == configurationFileLoc)
        {
            fail("Test resource \"sample_config_file_missing_dataID_name_in_event.xml\" was not found.");
        }

        Boolean isParsed = config.parseConfigFile(configurationFileLoc);
        // verify expected parsing errors occurred
        assertEquals(false, isParsed);
    }
}
