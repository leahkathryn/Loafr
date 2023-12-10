package com.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Yichen Li
 */

public class LogEventTest {

    private Configuration config = new Configuration();
    private Event event;

    @BeforeEach
    void setUp() {
        event = new Event("System_Initialization", Arrays.asList(
                new DataID("Status", DataType.BOOLEAN),
                new DataID("Error_Code", DataType.INTEGER)
        ));
    }


    @Test
    void testValidConfigFileParsing() {
        URL configurationFileLoc = getClass().getClassLoader().getResource("sample_config_file.xml");
        assertNotNull(configurationFileLoc, "Test resource \"sample_config_file.xml\" was not found.");

        List<String> inputData = Arrays.asList("true", "0");
        boolean isParsed = config.parseConfigFile(configurationFileLoc);
        assertTrue(isParsed);
        System.out.println("**--- Test testValidConfigFileParsing executed ---**");
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
    void testConvertInputToDataMap_ValidInput() {
        LogEvent logEvent = new LogEvent();

        List<String> inputData = Arrays.asList("[true false]", "123456", "test");
        Event event = new Event("Sample_Event", Arrays.asList(
                new DataID("Status", DataType.BOOLEAN),
                new DataID("Numbers", DataType.INTEGER),
                new DataID("Text", DataType.STRING)
        ));

        HashMap<DataID, List<?>> dataMap = logEvent.convertInputToDataMap(inputData, event);

        assertNotNull(dataMap);
        assertEquals(3, dataMap.size());

        List<String> testKey = new ArrayList<>(dataMap.keySet().stream().map(DataID::getName).toList());
        assertEquals(3, testKey.size());
        assertTrue(testKey.contains("Status"));
        assertTrue(testKey.contains("Numbers"));
        assertTrue(testKey.contains("Text"));

        dataMap.forEach((key, value) -> assertNotNull(value)); // Ensure values are not null
    }


    /**
     * Test following cases:
     *      1. The array contains only one value.
     *      2. integer
     *      3. String
     */
    @Test
    void testConvertInputToDataMap_InvalidSizeMismatch() {
        LogEvent logEvent = new LogEvent();

        List<String> inputData = Arrays.asList("[true]", "987654", "anotherTest");
        Event event = new Event("Sample_Event", Arrays.asList(
                new DataID("Status", DataType.BOOLEAN),
                new DataID("Numbers", DataType.INTEGER),
                new DataID("Text", DataType.STRING),
                new DataID("Status", DataType.BOOLEAN),
                new DataID("Numbers", DataType.INTEGER),
                new DataID("Text", DataType.STRING)
        ));

        HashMap<DataID, List<?>> dataMap = logEvent.convertInputToDataMap(inputData, event);

        assertNotNull(dataMap);
        assertEquals(0, dataMap.size());

        dataMap.forEach((key, value) -> assertNotNull(value)); // Ensure values are not null
    }

    // More test methods for different scenarios...

    @Test
    void testConvertInputToDataMap_EmptyBracketInput() {
        LogEvent logEvent = new LogEvent();

        List<String> inputData = Arrays.asList("[]", "123456", "test");
        Event event = new Event("Sample_Event", Arrays.asList(
                new DataID("Status", DataType.BOOLEAN),
                new DataID("Numbers", DataType.INTEGER),
                new DataID("Text", DataType.STRING)
        ));

        HashMap<DataID, List<?>> dataMap = logEvent.convertInputToDataMap(inputData, event);

        assertNotNull(dataMap);
        assertEquals(0, dataMap.size());

        dataMap.forEach((key, value) -> assertNotNull(value)); // Ensure values are not null
    }


    @Test
    void testConvertInputToDataMap_RandomStringsWithSize100() {
        LogEvent logEvent = new LogEvent();

        int numberOfStrings = 100;
        List<String> randomStrings = generateRandomStrings(numberOfStrings);
        List<DataID> dataIDList = randomStrings.stream()
                .map(str -> new DataID(str, DataType.STRING))
                .collect(Collectors.toList());

        Event event = new Event("Sample_Event", dataIDList);
        HashMap<DataID, List<?>> dataMap = logEvent.convertInputToDataMap(randomStrings, event);

        assertNotNull(dataMap);
        assertEquals(numberOfStrings, dataMap.size());

        dataMap.forEach((key, value) -> assertNotNull(value)); // Ensure values are not null
    }

    // Helper methods

    private List<String> generateRandomStrings(int numberOfStrings) {
        return IntStream.range(0, numberOfStrings)
                .mapToObj(i -> generateRandomString(10)) // Change '10' to desired string length
                .collect(Collectors.toList());
    }

    private String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * chars.length());
            stringBuilder.append(chars.charAt(index));
        }
        return stringBuilder.toString();
    }

    /**
     * A little more test cases.
     */
    @Test
    void testConvertInputToDataMap_MultipleDataValues() {
        LogEvent logEvent = new LogEvent();
        List<String> inputData = Arrays.asList(
                "[true false]", "123456", "test", "[true false]", "123456", "test",
                "[true false]", "123456", "test", "[true false]", "123456", "test",
                "[true false]", "123456", "test", "[true false]", "123456", "test",
                "[true false]", "123456", "test", "[true false]", "123456", "test"
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
        List<String> testKey = new ArrayList<>(dataMap.keySet().stream().map(DataID::getName).toList());
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
        dataMap.forEach((key, value) -> assertNotNull(value)); // Ensure values are not null
    }


    /**
     * This is testcase if the number of datavalues = 0.
     */
    @Test
    void testConvertInputToDataMap_EmptyInputData() {
        LogEvent logEvent = new LogEvent();

        List<String> inputData = Collections.emptyList();
        Event event = new Event("Sample_Empty_Event", Collections.emptyList());

        HashMap<DataID, List<?>> dataMap = logEvent.convertInputToDataMap(inputData, event);

        assertNotNull(dataMap);
        assertEquals(0, dataMap.size());

        dataMap.forEach((key, value) -> assertNotNull(value)); // Ensure values are not null
    }


    /**
     * If the size is 1.
     */
    @Test
    void testConvertInputToDataMap_SingleValue() {
        LogEvent logEvent = new LogEvent();

        List<String> inputData = Collections.singletonList("987654");
        Event event = new Event("Sample_Event", Collections.singletonList(new DataID("Numbers", DataType.INTEGER)));

        HashMap<DataID, List<?>> dataMap = logEvent.convertInputToDataMap(inputData, event);

        assertNotNull(dataMap);
        assertEquals(1, dataMap.size());

        dataMap.forEach((key, value) -> assertNotNull(value)); // Ensure values are not null
    }


    /**
     * Test for null
     */
    @Test
    void testConvertInputToDataMap_NullValues() {
        LogEvent logEvent = new LogEvent();

        List<String> inputData = Arrays.asList("null", "[null]", "[null null]");
        Event event = new Event("Sample_Event", Arrays.asList(
                new DataID("null1", DataType.STRING),
                new DataID("null2", DataType.STRING),
                new DataID("null3", DataType.STRING)
        ));

        HashMap<DataID, List<?>> dataMap = logEvent.convertInputToDataMap(inputData, event);
        assertNotNull(dataMap);
        assertEquals(3, dataMap.size());
        dataMap.forEach((key, value) -> assertNotNull(value)); // Ensure values are not null
    }

    @Test
    void testConvertInputToDataMap_InvalidInputFormats() {
        LogEvent logEvent = new LogEvent();

        List<String> inputData = Arrays.asList("\n", "N/A", "\0");
        Event event = new Event("Sample_Event", Arrays.asList(
                new DataID("\n", DataType.STRING),
                new DataID("N/A", DataType.STRING),
                new DataID("\0", DataType.STRING)
        ));

        HashMap<DataID, List<?>> dataMap = logEvent.convertInputToDataMap(inputData, event);

        assertNotNull(dataMap);
        assertEquals(3, dataMap.size());

        dataMap.forEach((key, value) -> assertNotNull(value)); // Ensure values are not null
    }


    @Test
    void testConvertInputToDataMap_MissingRightBracketError() {
        LogEvent logEvent = new LogEvent();

        List<String> inputData = Arrays.asList("[true false", "123456", "test");
        Event event = new Event("Sample_Event", Arrays.asList(
                new DataID("Status", DataType.BOOLEAN),
                new DataID("Numbers", DataType.INTEGER),
                new DataID("Text", DataType.STRING)
        ));

        HashMap<DataID, List<?>> dataMap = logEvent.convertInputToDataMap(inputData, event);

        assertNotNull(dataMap);
        assertEquals(0, dataMap.size());

        dataMap.forEach((key, value) -> assertNotNull(value)); // Ensure values are not null
    }


    @Test
    void testConvertInputToDataMap_BlankDataError() {
        LogEvent logEvent = new LogEvent();

        List<String> inputData = Arrays.asList("[]", "123456", "test");
        Event event = new Event("Sample_Event", Arrays.asList(
                new DataID("Status", DataType.BOOLEAN),
                new DataID("Numbers", DataType.INTEGER),
                new DataID("Text", DataType.STRING)
        ));

        HashMap<DataID, List<?>> dataMap = logEvent.convertInputToDataMap(inputData, event);

        assertNotNull(dataMap);
        assertEquals(0, dataMap.size());

        dataMap.forEach((key, value) -> assertNotNull(value)); // Ensure values are not null
    }
}

/**
 * Notes for me and whoever needs to write this test class:
 *  Assumes that event in the line of the logfile is valid
 *  dataValuesInput/inputData is the string representation of data in a line of a valid log file
 *  eventMatch/event is an event object parsed from a line in a log file
 */
