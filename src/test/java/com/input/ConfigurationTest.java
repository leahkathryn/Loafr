package com.input;

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
//        Boolean isParsed = config.parseConfigFile("C:\\Users\\leahk\\OneDrive\\Documents\\GitHub\\Loafr\\src\\test\\resources\\sample_config_file.xml");
        Boolean isParsed = config.parseConfigFile("C:\\Users\\Nova\\Documents\\CSCI5801\\Loafr\\src\\test\\resources\\sample_config_file.xml");
        try{
            setupTest0();
            assertEquals(true, isParsed);
            Event configEvent;
            Event testEvent;
            for (int i = 0; i < config.getDataIDList().size(); i++){
                assertEquals(true, config.getDataIDList().get(i).getName().equals(testDataIDList.get(i).getName()));
                assertEquals(true, config.getDataIDList().get(i).getType().equals(testDataIDList.get(i).getType()));
            }
            for (int j = 0; j < config.getEventList().size(); j++){
                configEvent = config.getEventList().get(j);
                testEvent = testEventList.get(j);
                for (int k = 0; k < config.getDataIDList().size(); k++){
                    assertEquals(true, configEvent.getDataIDList().get(k).getName().equals(testEvent.getDataIDList().get(k).getName()));
                }
                assertEquals(true, config.getEventList().get(j).equals(testEventList.get(j)));
            }
            System.out.println("**--- parseConfigFileTestIfSuccessful Executed ---**");
        } catch(Exception ignored){
        }
    }

    @Test
    void parseConfigFileTestIfNodeInEventsIsWrong(){
//        Boolean isParsed = config.parseConfigFile("C:\\Users\\leahk\\OneDrive\\Documents\\GitHub\\Loafr\\src\\test\\resources\\sample_config_file_wrong_node_in_events.xml");
        Boolean isParsed = config.parseConfigFile("C:\\Users\\Nova\\Documents\\CSCI5801\\Loafr\\src\\test\\resources\\sample_config_file_wrong_node_in_events.xml");

        assertEquals(true, isParsed);
        System.out.println("**--- parseConfigFileTestIfNodeInEventsIsWrong Executed ---**");
    }

    @Test
    void parseConfigFileTestDataIDNodeMissingName(){
//        Boolean isParsed = config.parseConfigFile("C:\\Users\\leahk\\OneDrive\\Documents\\GitHub\\Loafr\\src\\test\\resources\\sample_config_file_wrong_dataID_node0.xml");
        Boolean isParsed = config.parseConfigFile("C:\\Users\\Nova\\Documents\\CSCI5801\\Loafr\\src\\test\\resources\\sample_config_file_wrong_dataID_node0.xml");

        assertEquals(false, isParsed);
        System.out.println("**--- parseConfigFileTestDataIDNodeMissingName Executed ---**");
    }

    @Test
    void parseConfigFileTestDataIDNodeMissingType(){
//        Boolean isParsed = config.parseConfigFile("C:\\Users\\leahk\\OneDrive\\Documents\\GitHub\\Loafr\\src\\test\\resources\\sample_config_file_wrong_dataID_node1.xml");
        Boolean isParsed = config.parseConfigFile("C:\\Users\\Nova\\Documents\\CSCI5801\\Loafr\\src\\test\\resources\\sample_config_file_wrong_dataID_node1.xml");

        assertEquals(false, isParsed);
        System.out.println("**--- parseConfigFileTestDataIDNodeMissingType Executed ---**");
    }

    @Test
    void parseConfigFileTestMissingDefaultFileLoc(){
//        Boolean isParsed = config.parseConfigFile("C:\\Users\\leahk\\OneDrive\\Documents\\GitHub\\Loafr\\src\\test\\resources\\sample_config_file_missing_fileLoc.xml");
        Boolean isParsed = config.parseConfigFile("C:\\Users\\Nova\\Documents\\CSCI5801\\Loafr\\src\\test\\resources\\sample_config_file_missing_fileLoc.xml");

        assertEquals(false, isParsed);
        System.out.println("**--- parseConfigFileTestMissingDefaultFileLoc Executed ---**");
    }

    @Test
    void parseConfigFileTestWrongDefaultFileLoc(){
//        Boolean isParsed = config.parseConfigFile("C:\\Users\\leahk\\OneDrive\\Documents\\GitHub\\Loafr\\src\\test\\resources\\sample_config_file_defaultFileLocNotElem.xml");
        Boolean isParsed = config.parseConfigFile("C:\\Users\\Nova\\Documents\\CSCI5801\\Loafr\\src\\test\\resources\\sample_config_file_defaultFileLocNotElem.xml");

        assertEquals(false, isParsed);
        System.out.println("**--- parseConfigFileTestMissingDefaultFileLoc Executed ---**");
    }

    @Test
    void parseConfigFileTestMissingAllEventNodes(){
//        Boolean isParsed = config.parseConfigFile("C:\\Users\\leahk\\OneDrive\\Documents\\GitHub\\Loafr\\src\\test\\resources\\sample_config_file_missing_events.xml");
        Boolean isParsed = config.parseConfigFile("C:\\Users\\Nova\\Documents\\CSCI5801\\Loafr\\src\\test\\resources\\sample_config_file_missing_events.xml");

        assertEquals(false, isParsed);
        System.out.println("**--- parseConfigFileTestMissingAllEventNodes Executed ---**");
    }

    @Test
    void parseConfigFileTestMissingDataIDNode(){
//        Boolean isParsed = config.parseConfigFile("C:\\Users\\leahk\\OneDrive\\Documents\\GitHub\\Loafr\\src\\test\\resources\\sample_config_file_missing_dataID.xml");
        Boolean isParsed = config.parseConfigFile("C:\\Users\\Nova\\Documents\\CSCI5801\\Loafr\\src\\test\\resources\\sample_config_file_missing_dataID.xml");

        assertEquals(false, isParsed);
        System.out.println("**--- parseConfigFileTestMissingAllDataIDNodes Executed ---**");
    }

    @Test
    void parseConfigFileTestMissingAllDataIDNodes(){
//        Boolean isParsed = config.parseConfigFile("C:\\Users\\leahk\\OneDrive\\Documents\\GitHub\\Loafr\\src\\test\\resources\\sample_config_file_missing_dataIDs.xml");
        Boolean isParsed = config.parseConfigFile("C:\\Users\\Nova\\Documents\\CSCI5801\\Loafr\\src\\test\\resources\\sample_config_file_missing_dataIDs.xml");

        assertEquals(false, isParsed);
        System.out.println("**--- parseConfigFileTestMissingAllDataIDNodes Executed ---**");
    }

    @Test
    void parseConfigFileTestMissingAllDataIDNodesInEvent(){
//        Boolean isParsed = config.parseConfigFile("C:\\Users\\leahk\\OneDrive\\Documents\\GitHub\\Loafr\\src\\test\\resources\\sample_config_file_missing_dataID_name_in_event.xml");
        Boolean isParsed = config.parseConfigFile("C:\\Users\\Nova\\Documents\\CSCI5801\\Loafr\\src\\test\\resources\\sample_config_file_missing_dataID_name_in_event.xml");

        assertEquals(false, isParsed);
        System.out.println("**--- parseConfigFileTestMissingAllEventNodes Executed ---**");
    }


}
