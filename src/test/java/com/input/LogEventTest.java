package com.input;

import com.ErrorHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.input.DataType.BOOLEAN;
import static com.input.DataType.INTEGER;
import static org.junit.jupiter.api.Assertions.*;

public class LogEventTest{
    private Configuration config= new Configuration();
    private List<String> inputData = new ArrayList<>();
    private Event event;

    String sampleLogEvent = "2023-11-12 08:30:00,Communication_Check,success,0,[32.5 54.4 76.8],100\n";
    @BeforeAll
    static void setupTest0(){

    }
    @Test
    void test0(){
        URL configurationFileLoc = getClass().getClassLoader().getResource("sample_config_file.xml");
        inputData = new ArrayList<>(Arrays.asList("true", "0"));
        event = new Event("System_Initialization", Arrays.asList(new DataID("Status", BOOLEAN),
                new DataID("Error_Code", INTEGER)));
        if (configurationFileLoc == null){
            fail("Test resource \"sample_config_file.xml\" was not found.");
        }
        boolean isParsed = config.parseConfigFile(configurationFileLoc);
        assertEquals(true, isParsed);
        System.out.println("**--- Test test0 executed ---**");
    }


    /**
     * Test method to test parse method.
     */
    @Test
    void testParseStringToDataValue() {
        LogEvent logEvent = new LogEvent();
        // Test parsing a string to a STRING type
        String stringValue = "TestString";
        Object parsedString = parseStringToDataValue(DataType.STRING, stringValue);
        assertNotNull(parsedString);
        assertTrue(parsedString instanceof String);
        assertEquals(stringValue, parsedString);

        // Test parsing a string to a CHAR type
        String charValue = "A";
        Object parsedChar = parseStringToDataValue(DataType.CHAR, charValue);
        assertNotNull(parsedChar);
        assertTrue(parsedChar instanceof Character);
        assertEquals('A', parsedChar);

        // Test parsing a string to an INTEGER type
        String intValue = "123";
        Object parsedInt = parseStringToDataValue(DataType.INTEGER, intValue);
        assertNotNull(parsedInt);
        assertTrue(parsedInt instanceof Integer);
        assertEquals(123, parsedInt);

        // Test parsing a string to a FLOAT type
        String floatValue = "12.34";
        Object parsedFloat = parseStringToDataValue(DataType.FLOAT, floatValue);
        assertNotNull(parsedFloat);
        assertTrue(parsedFloat instanceof Float);
        assertEquals(12.34f, parsedFloat);

        // Test parsing a string to a DOUBLE type
        String doubleValue = "56.78";
        Object parsedDouble = parseStringToDataValue(DataType.DOUBLE, doubleValue);
        assertNotNull(parsedDouble);
        assertTrue(parsedDouble instanceof Double);
        assertEquals(56.78, (Double) parsedDouble, 0.001);

        // Test parsing a string to a BOOLEAN type
        String boolValue = "true";
        Object parsedBoolean = parseStringToDataValue(DataType.BOOLEAN, boolValue);
        assertNotNull(parsedBoolean);
        assertTrue(parsedBoolean instanceof Boolean);
        assertEquals(true, parsedBoolean);
        System.out.println("**--- Test testParseStringToDataValue executed ---**");
    }



    void print(Object someObject){
        System.out.println(someObject);
    }

    /**
     * Below are 2 methods in the LogEvent class, but they are private, so I pasted them here to test them.
     */
    private <T> T parseStringToDataValue(DataType type, String str)
    {
        T value;

        switch(type)
        {
            case STRING:
                value = (T) str;
                break;
            case CHAR:
                if (str.length() > 1)
                {
                    value = null;
                    break;
                }
                else
                {
                    value = (T) ((Character)str.charAt(0));
                }
                break;
            case INTEGER:
                try {
                    value = (T) Integer.valueOf(str);
                } catch (NumberFormatException ex) {
                    value = null;
                }
                break;
            case FLOAT:
                try {
                    value = (T) Float.valueOf(str);
                } catch (NumberFormatException ex) {
                    value = null;
                }
                break;
            case DOUBLE:
                try {
                    value = (T) Double.valueOf(str);
                } catch (NumberFormatException ex) {
                    value = null;
                }
                break;
            case BOOLEAN:
                value = (T) tryParseBoolean(str);
                break;
            default:
                value = null;
        }
        if (null == value)
        {
            ErrorHandler.logError("Failure parsing event from log file: incorrect data type provided.\nLoafr exiting...");
        }
        return value;
    }

    private Boolean tryParseBoolean(String str)
    {
        if (str.equalsIgnoreCase("true"))
        {
            return Boolean.TRUE;
        }
        if (str.equalsIgnoreCase("false"))
        {
            return Boolean.FALSE;
        }
        return null;
    }


    /**
     * Test methods to test convertInputToDataMap.
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
        System.out.println("**--- Test testConvertInputToDataMap_1 executed ---**");
    }


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

        // Print statement indicating the test completion
        System.out.println("**--- Test testConvertInputToDataMap_2 executed ---**");
    }


    @Test
    void testCreateDataValueList_1() {
        // Test case 1: Testing with a list enclosed in brackets
        List<Boolean> boolOutput = new ArrayList<>();
        String boolInput = "[true false true]";
        DataType boolType = DataType.BOOLEAN;
        boolean resultBool = createDataValueList(boolType, boolInput, boolOutput);
        assertTrue(resultBool);
        assertEquals(3, boolOutput.size());
        assertTrue(boolOutput.get(0));
        assertFalse(boolOutput.get(1));
        assertTrue(boolOutput.get(2));

        // Test case 2: Testing with a single value without brackets
        List<Integer> intOutput = new ArrayList<>();
        String intInput = "123";
        DataType intType = DataType.INTEGER;
        boolean resultInt = createDataValueList(intType, intInput, intOutput);
        assertTrue(resultInt);
        assertEquals(1, intOutput.size());
        assertEquals(123, intOutput.get(0));


        // Test case 3: Testing with a single double without brackets
        List<Double> doubleOutput = new ArrayList<>();
        String doubleInput = "[3.14 2.718 1.618]";
        DataType doubleType = DataType.DOUBLE;
        boolean resultDouble = createDataValueList(doubleType, doubleInput, doubleOutput);
        assertTrue(resultDouble);
        assertEquals(3, doubleOutput.size());
        assertEquals(3.14, doubleOutput.get(0), 0.0001);
        assertEquals(2.718, doubleOutput.get(1), 0.0001);
        assertEquals(1.618, doubleOutput.get(2), 0.0001);

        // Test case 4: Testing with a single double without brackets
        List<Float> floatOutput = new ArrayList<>();
        String floatInput = "[3.14f 2.718f 1.618f]";
        DataType floatType = DataType.FLOAT;
        boolean resultFloat = createDataValueList(floatType, floatInput, floatOutput);
        assertTrue(resultFloat);
        assertEquals(3, doubleOutput.size());
        assertEquals(3.14f, doubleOutput.get(0), 0.0001);
        assertEquals(2.718f, doubleOutput.get(1), 0.0001);
        assertEquals(1.618f, doubleOutput.get(2), 0.0001);
        System.out.println("**--- Test testCreateDataValueList_1 executed ---**");
    }


    @Test
    void testCreateDataValueList_2() {
        // Test case 1: Testing with a list enclosed in brackets for string values
        List<String> stringOutput = new ArrayList<>();
        String stringInput = "[test1 test2 test3]";
        DataType stringType = DataType.STRING;
        boolean resultString = createDataValueList(stringType, stringInput, stringOutput);
        assertTrue(resultString);
        assertEquals(3, stringOutput.size());
        assertEquals("test1", stringOutput.get(0));
        assertEquals("test2", stringOutput.get(1));
        assertEquals("test3", stringOutput.get(2));

        // Test case 2: Testing with a single value without brackets for char values
        List<Character> charOutput = new ArrayList<>();
        String charInput = "a";
        DataType charType = DataType.CHAR;
        boolean resultChar = createDataValueList(charType, charInput, charOutput);
        assertTrue(resultChar);
        assertEquals(1, charOutput.size());
        assertEquals('a', charOutput.get(0));

        // Test case 3: Testing with a single value without brackets for boolean values
        List<Boolean> boolOutput = new ArrayList<>();
        String boolInput = "true";
        DataType boolType = DataType.BOOLEAN;
        boolean resultBool = createDataValueList(boolType, boolInput, boolOutput);
        assertTrue(resultBool);
        assertEquals(1, boolOutput.size());
        assertTrue(boolOutput.get(0));

        System.out.println("**--- Test testCreateDataValueList_2 executed ---**");
    }


    /**
     * This method is pasted here also because of testing.
     */
    private <T> boolean createDataValueList(DataType type, String stringValue, List<T> output)
    {
        // parse each value
        if (stringValue.startsWith("["))
        {
            // if the list is not closed with a ']', this is a syntax error
            if (!stringValue.endsWith("]"))
            {
                ErrorHandler.logError("Failure parsing event from log file: syntax error, missing ']'.\nLoafr exiting...");
                return false;
            }
            // if the list is blank, this is a syntax error
            stringValue = stringValue.substring(1,stringValue.length()-1);
            if (stringValue.isBlank())
            {
                ErrorHandler.logError("Failure parsing event from log file: syntax error, blank data list within brackets [].\nLoafr exiting...");
                return false;
            }
            // Separate each value in the String by space and store them in a list.
            String[] values = stringValue.split(" ");
            List<String> stringValueList = new ArrayList<>();
            // Remove leading/trailing whitespace and add to ArrayList
            for (String str : values)
            {
                stringValueList.add(str.strip());
            }
            // convert from String to data type
            for (String str : stringValueList)
            {
                T value;
                if (null == (value = parseStringToDataValue(type,str)))
                {
                    return false;
                }
                output.add(value);
            }
        }
        else
        {
            T value;
            if (null == (value = parseStringToDataValue(type,stringValue.strip())))
            {
                return false;
            }
            output.add(value);
        }
        return true;
    }
}



/**
 * Notes for me and whoever needs to write this test class:
 *  Assumes that event in the line of the logfile is valid
 *  dataValuesInput/inputData is the string representation of data in a line of a valid log file
 *  eventMatch/event is an event object parsed from a line in a log file
 */