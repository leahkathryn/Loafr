package com.input;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.input.DataType.BOOLEAN;
import static com.input.DataType.INTEGER;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Yichen Li
 */
public class LogEventTest
{
    private Configuration config = new Configuration();
    private List<String> inputData = new ArrayList<>();
    private Event event;

    @BeforeAll
    static void setupTest0() {

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
     * Test methods to test convertInputToDataMap.
     * Notes from Leah, test margin cases for this method only, not the private methods.
     *
     * The test method below contains:
     *      1. regular brackets with boolean value
     *      2. integer
     *      3. string
     */
    @Test
    void testConvertInputToDataMap_1() {
        LogEvent logEvent = new LogEvent();

        // Mocking input data and event
        List<String> inputData = Arrays.asList("[true false]", "123456", "test");
        Event event = new Event("Sample_Event", Arrays.asList(
                new DataID("Status", DataType.BOOLEAN),
                new DataID("Numbers", DataType.INTEGER),
                new DataID("Text", DataType.STRING)
        ));

        HashMap<DataID, List<?>> dataMap = logEvent.convertInputToDataMap(inputData, event);

        // Assuming the conversion is successful
        assertNotNull(dataMap);
        assertEquals(3, dataMap.size());

        ArrayList<String> testKey = new ArrayList<>();
        for (DataID key : dataMap.keySet()) {
            testKey.add(key.getName());
            System.out.println("Key: " + key.getName() + ", Value: " + dataMap.get(key));
        }

        assertEquals(3, dataMap.size());
        assertTrue(testKey.contains("Status"));
        assertTrue(testKey.contains("Numbers"));
        assertTrue(testKey.contains("Text"));
        for (DataID key : dataMap.keySet()) {
            assertNotNull(dataMap.get(key)); // Ensure values are not null
        }
    }


    /**
     * Test following cases:
     *      1. The array contains only one value.
     *      2. integer
     *      3. String
     */
    @Test
    void testConvertInputToDataMap_2() {
        LogEvent logEvent = new LogEvent();

        // Changed input data
        List<String> inputData = Arrays.asList("[true]", "987654", "anotherTest");

        // Event remains the same as in the previous test case
        Event event = new Event("Sample_Event", Arrays.asList(
                new DataID("Status", DataType.BOOLEAN),
                new DataID("Numbers", DataType.INTEGER),
                new DataID("Text", DataType.STRING)
        ));

        HashMap<DataID, List<?>> dataMap = logEvent.convertInputToDataMap(inputData, event);

        // Assuming the conversion is successful
        assertNotNull(dataMap);
        assertEquals(3, dataMap.size());

        ArrayList<String> testKey = new ArrayList<>();
        for (DataID key : dataMap.keySet()) {
            testKey.add(key.getName());
            assertNotNull(dataMap.get(key)); // Ensure values are not null
        }

        // Check if specific keys are present in the dataMap
        assertTrue(testKey.contains("Status"));
        assertTrue(testKey.contains("Numbers"));
        assertTrue(testKey.contains("Text"));
    }


    /**
     * A little more test cases.
     */
    @Test
    void testConvertInputToDataMap_3() {
        LogEvent logEvent = new LogEvent();
        List<String> inputData = Arrays.asList(
                "[true false]", "123456", "test","[true false]", "123456", "test",
                "[true false]", "123456", "test","[true false]", "123456", "test",
                "[true false]", "123456", "test","[true false]", "123456", "test",
                "[true false]", "123456", "test","[true false]", "123456", "test"
        );
        Event event = new Event("Sample_Event", Arrays.asList(
                new DataID("Status1", DataType.BOOLEAN),
                new DataID("Numbers1", DataType.INTEGER),
                new DataID("Text1", DataType.STRING),

                new DataID("Status2", DataType.BOOLEAN),
                new DataID("Numbers2", DataType.INTEGER),
                new DataID("Text2", DataType.STRING),

                new DataID("Status3", DataType.BOOLEAN),
                new DataID("Numbers3", DataType.INTEGER),
                new DataID("Text3", DataType.STRING),

                new DataID("Status4", DataType.BOOLEAN),
                new DataID("Numbers4", DataType.INTEGER),
                new DataID("Text4", DataType.STRING),

                new DataID("Status5", DataType.BOOLEAN),
                new DataID("Numbers5", DataType.INTEGER),
                new DataID("Text5", DataType.STRING),

                new DataID("Status6", DataType.BOOLEAN),
                new DataID("Numbers6", DataType.INTEGER),
                new DataID("Text6", DataType.STRING),

                new DataID("Status7", DataType.BOOLEAN),
                new DataID("Numbers7", DataType.INTEGER),
                new DataID("Text7", DataType.STRING),

                new DataID("Status8", DataType.BOOLEAN),
                new DataID("Numbers8", DataType.INTEGER),
                new DataID("Text8", DataType.STRING)

        ));

        HashMap<DataID, List<?>> dataMap = logEvent.convertInputToDataMap(inputData, event);

        assertNotNull(dataMap);
        assertEquals(inputData.size(), dataMap.size());

        ArrayList<String> testKey = new ArrayList<>();
        for (DataID key : dataMap.keySet()) {
            testKey.add(key.getName());
//            System.out.println("Key: " + key.getName() + ", Value: " + dataMap.get(key));
        }

        assertEquals(inputData.size(), dataMap.size());
        assertTrue(testKey.contains("Status1"));
        assertTrue(testKey.contains("Numbers1"));
        assertTrue(testKey.contains("Text1"));
        assertTrue(testKey.contains("Status2"));
        assertTrue(testKey.contains("Numbers2"));
        assertTrue(testKey.contains("Text2"));
        assertTrue(testKey.contains("Status3"));
        assertTrue(testKey.contains("Numbers3"));
        assertTrue(testKey.contains("Text3"));
        assertTrue(testKey.contains("Status4"));
        assertTrue(testKey.contains("Numbers4"));
        assertTrue(testKey.contains("Text4"));
        assertTrue(testKey.contains("Status5"));
        assertTrue(testKey.contains("Numbers5"));
        assertTrue(testKey.contains("Text5"));
        assertTrue(testKey.contains("Status6"));
        assertTrue(testKey.contains("Numbers6"));
        assertTrue(testKey.contains("Text6"));
        assertTrue(testKey.contains("Status7"));
        assertTrue(testKey.contains("Numbers7"));
        assertTrue(testKey.contains("Text7"));
        assertTrue(testKey.contains("Status8"));
        assertTrue(testKey.contains("Numbers8"));
        assertTrue(testKey.contains("Text8"));
        for (DataID key : dataMap.keySet()) {
            assertNotNull(dataMap.get(key)); // Ensure values are not null
        }
    }


    /**
     * This is testcase if the number of datavalues = 0.
     */
    @Test
    void testConvertInputToDataMap_4_empty() {
        LogEvent logEvent = new LogEvent();

        List<String> inputData = Arrays.asList();
        Event event = new Event("Sample_Empty_Event", Arrays.asList());

        HashMap<DataID, List<?>> dataMap = logEvent.convertInputToDataMap(inputData, event);

        // Assuming the conversion is successful
        assertNotNull(dataMap);
        assertEquals(0, dataMap.size());

        ArrayList<String> testKey = new ArrayList<>();
        for (DataID key : dataMap.keySet()) {
            testKey.add(key.getName());
            assertNotNull(dataMap.get(key)); // Ensure values are not null
        }

        // Check if specific keys are present in the dataMap
        assertFalse(testKey.contains("Status"));
        assertFalse(testKey.contains("Numbers"));
        assertFalse(testKey.contains("Text"));
    }

    /**
     * If the size is 1.
     */
    @Test
    void testConvertInputToDataMap_5_one() {
        LogEvent logEvent = new LogEvent();

        // Changed input data
        List<String> inputData = Arrays.asList( "987654");

        // Event remains the same as in the previous test case
        Event event = new Event("Sample_Event", Arrays.asList(
                new DataID("Numbers", DataType.INTEGER)
        ));

        HashMap<DataID, List<?>> dataMap = logEvent.convertInputToDataMap(inputData, event);

        // Assuming the conversion is successful
        assertNotNull(dataMap);
        assertEquals(1, dataMap.size());

        ArrayList<String> testKey = new ArrayList<>();
        for (DataID key : dataMap.keySet()) {
            testKey.add(key.getName());
            assertNotNull(dataMap.get(key)); // Ensure values are not null
        }

        // Check if specific keys are present in the dataMap
        assertTrue(testKey.contains("Numbers"));
    }


    /**
     * Test for null
     */
    @Test
    void testConvertInputToDataMap_6_null() {
        LogEvent logEvent = new LogEvent();

        // Changed input data
        List<String> inputData = Arrays.asList("null", "[null]", "[null null]");

        // Event remains the same   as in the previous test case
        Event event = new Event("Sample_Event", Arrays.asList(
                new DataID("null1", DataType.STRING),
                new DataID("null2", DataType.STRING),
                new DataID("null3", DataType.STRING)
        ));

        HashMap<DataID, List<?>> dataMap = logEvent.convertInputToDataMap(inputData, event);

        // Assuming the conversion is successful
        assertNotNull(dataMap);
        assertEquals(3, dataMap.size());

        ArrayList<String> testKey = new ArrayList<>();
        for (DataID key : dataMap.keySet()) {
            testKey.add(key.getName());
            assertNotNull(dataMap.get(key)); // Ensure values are not null
        }

        // Check if specific keys are present in the dataMap
        assertTrue(testKey.contains("null1"));
        assertTrue(testKey.contains("null2"));
        assertTrue(testKey.contains("null3"));
    }


    /**
     * Test for null
     */
    @Test
    void testConvertInputToDataMap_7_otherStrangeThings() {
        LogEvent logEvent = new LogEvent();

        // Changed input data
        List<String> inputData = Arrays.asList("\n", "N/A", "\0");

        // Event remains the same as in the previous test case
        Event event = new Event("Sample_Event", Arrays.asList(
                new DataID("\n", DataType.STRING),
                new DataID("N/A", DataType.STRING),
                new DataID("\0", DataType.STRING)
        ));

        HashMap<DataID, List<?>> dataMap = logEvent.convertInputToDataMap(inputData, event);

        // Assuming the conversion is successful
        assertNotNull(dataMap);
        assertEquals(3, dataMap.size());

        ArrayList<String> testKey = new ArrayList<>();
        for (DataID key : dataMap.keySet()) {
            testKey.add(key.getName());
            assertNotNull(dataMap.get(key)); // Ensure values are not null
        }

        // Check if specific keys are present in the dataMap
        assertTrue(testKey.contains("\n"));
        assertTrue(testKey.contains("N/A"));
        assertTrue(testKey.contains("\0"));
    }


    /**
     * Test if we have too many elements. For example... 100 strings.
     */
    @Test
    void testConvertInputToDataMap_8_RandomStrings() {
        LogEvent logEvent = new LogEvent();

        // Generate 100 random strings and create DataIDs for each string
        int numberOfStrings = 100;
        List<String> randomStrings = generateRandomStrings(numberOfStrings);
        List<DataID> dataIDList = randomStrings.stream()
                .map(str -> new DataID(str, DataType.STRING))
                .collect(Collectors.toList());

        // Create Event with the list of DataID objects
        Event event = new Event("Sample_Event", dataIDList);
        HashMap<DataID, List<?>> dataMap = logEvent.convertInputToDataMap(randomStrings, event);
        assertNotNull(dataMap);
        assertEquals(numberOfStrings, dataMap.size());

        ArrayList<String> testKey = new ArrayList<>();
        for (DataID key : dataMap.keySet()) {
            testKey.add(key.getName());
            assertNotNull(dataMap.get(key)); // Ensure values are not null
        }

        // Check if all keys are present in the dataMap
        for (String str : randomStrings) {
            assertTrue(testKey.contains(str));
        }
    }

    // Helper method to generate random strings
    private List<String> generateRandomStrings(int numberOfStrings) {
        return IntStream.range(0, numberOfStrings)
                .mapToObj(i -> generateRandomString(10)) // Change '10' to desired string length
                .collect(Collectors.toList());
    }

    // Helper method to generate a single random string
    private String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * chars.length());
            stringBuilder.append(chars.charAt(index));
        }
        return stringBuilder.toString();
    }

}

/**
 * Notes for me and whoever needs to write this test class:
 *  Assumes that event in the line of the logfile is valid
 *  dataValuesInput/inputData is the string representation of data in a line of a valid log file
 *  eventMatch/event is an event object parsed from a line in a log file
 */
