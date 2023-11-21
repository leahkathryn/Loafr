package com.input;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.input.DataType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class ConfigurationTest {
    private List<Event> testEventList = new ArrayList<>();
    private List<DataID> testDataIDList = new ArrayList<>();
    private Configuration config = new Configuration();
    @BeforeAll
    static void beforeAll(){

    }

    public void setupTest0() {
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
        testEventList.add(new Event("Navigation_Check", new ArrayList<DataID>(Arrays.asList(
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

        testEventList.add(new Event("System_Initialization", new ArrayList<DataID>(Arrays.asList(
                new DataID("Status", BOOLEAN),
                new DataID("Error_Code",INTEGER)
        ))));
        testEventList.add(new Event("Communication_Check", new ArrayList<DataID>(Arrays.asList(
                new DataID("Result", STRING),
                new DataID("Error_Code", INTEGER),
                new DataID("Frequencies", FLOAT),
                new DataID("Signal_Strength", INTEGER)
        ))));
        testEventList.add(new Event("Navigation_Check", new ArrayList<DataID>(Arrays.asList(
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
    void parseConfigFileTest0() {
        Boolean isParsed = config.parseConfigFile("C:\\Users\\leahk\\OneDrive\\Documents\\GitHub\\Loafr\\src\\test\\resources\\sample_config_file.xml");
        try{
            setupTest0();
            assertEquals(true, isParsed);
            assertEquals(testEventList, config.getEventList());
            assertEquals(testDataIDList, config.getDataIDList());
            System.out.println("**--- Test 0 parseConfigFile Executed ---**");
        } catch(Exception ignored){
        }
    }

    @Test
    void parseConfigFileTest1(){
        Boolean isParsed = config.parseConfigFile("C:\\Users\\leahk\\OneDrive\\Documents\\GitHub\\Loafr\\src\\test\\resources\\sample_config_file_wrong_node_in_events.xml");
        assertEquals(false, isParsed);
    }

    @Test
    void parseConfigFileTest2(){
        Boolean isParsed = config.parseConfigFile("C:\\Users\\leahk\\OneDrive\\Documents\\GitHub\\Loafr\\src\\test\\resources\\sample_config_file_wrong_dataID_node0.xml");
        assertEquals(false, isParsed);
    }

    @Test
    void parseConfigFileTest3(){
        Boolean isParsed = config.parseConfigFile("C:\\Users\\leahk\\OneDrive\\Documents\\GitHub\\Loafr\\src\\test\\resources\\sample_config_file_wrong_dataID_node1.xml");
        assertEquals(false, isParsed);
    }

    @Test
    void parseConfigFileTest4(){
        Boolean isParsed = config.parseConfigFile("C:\\Users\\leahk\\OneDrive\\Documents\\GitHub\\Loafr\\src\\test\\resources\\sample_config_file_wrong_fileLoc.xml");
        assertEquals(false, isParsed);
    }

    @Test
    void parseConfigFileTest5(){
        Boolean isParsed = config.parseConfigFile("C:\\Users\\leahk\\OneDrive\\Documents\\GitHub\\Loafr\\src\\test\\resources\\sample_config_file_missing_events.xml");
        assertEquals(false, isParsed);
    }

    @Test
    void parseConfigFileTest6(){
        Boolean isParsed = config.parseConfigFile("C:\\Users\\leahk\\OneDrive\\Documents\\GitHub\\Loafr\\src\\test\\resources\\sample_config_file_missing_dataID.xml");
        assertEquals(false, isParsed);
    }

    @Test
    void parseConfigFileTest7(){
        Boolean isParsed = config.parseConfigFile("C:\\Users\\leahk\\OneDrive\\Documents\\GitHub\\Loafr\\src\\test\\resources\\sample_config_file_missing_dataIDs.xml");
        assertEquals(false, isParsed);
    }
}
