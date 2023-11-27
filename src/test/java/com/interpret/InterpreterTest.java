package com.interpret;

import com.analyze.AnalysisTask;
import com.analyze.Search;
import com.input.Configuration;
import com.input.DataID;
import com.input.LogEvent;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This test class tests the public method getAnalysisTask of the Interpreter class.
 * Test cases:
 *      Test 1: "search" keyword with valid instructions returns Search AnalysisTask
 *      Test 2: "sort" keyword with valid instructions returns null
 *      Test 3: "filter" keyword with valid instructions returns null
 *      Test 4: undefined keyword with valid instructions returns null
 *
 *      All of the following test cases provide the "search" keyword:
 *      Test 5: Valid regular expression as only element in instructions list returns Search AnalysisTask
 *      Test 6: Valid attribute type (dataValue), DataID, and regular expression returns Search AnalysisTask
 *      Test 7: Valid attribute type (timeStamp) and regular expression returns Search AnalysisTask
 *      Test 8: Valid attribute type (event) and regular expression returns Search AnalysisTask
 *      Test 9: Valid attribute type (dataID) and regular expression returns Search AnalysisTask
 *      Test 10: Valid attribute type (dataType) and regular expression returns Search AnalysisTask
 *      Test 11: Empty instructions list returns null
 *      Test 12: Invalid attribute type returns null
 *      Test 13: Invalid number of instructions in list when attribute type is "dataValue", returns null
 *      Test 14: Invalid DataID when attribute type is "dataValue", returns null
 *      Test 15: Invalid number of instructions in list when attribute type is not "dataValue", returns null
 *      Test 16: The regular expression provided in the list is a blank String, returns null
 *
 * @author Leah Lehmeier
 */
public class InterpreterTest
{
    static Interpreter interpreter;
    static Configuration configuration;

    @BeforeAll
    static void beforeAll()
    {
        URL configurationFileLoc = InterpreterTest.class.getClassLoader().getResource("sample_config_file.xml");
        configuration = new Configuration();
        Boolean configParsed = Boolean.FALSE;
        if (Boolean.FALSE == (configParsed = configuration.parseConfigFile(configurationFileLoc)))
        {
            fail("Configuration file parsing failure.");
        }

        interpreter = new Interpreter(configuration);
    }

    /* * *
     * Test 1
     * * */
    @Test
    void GetAnalysisTask_SearchKeyword_ReturnSearchAnalysisTask()
    {
        String keyword = "search";
        List<String> instructions = new ArrayList<>(Arrays.asList("search_expr"));
        AnalysisTask task = interpreter.getAnalysisTask(keyword,instructions);
        assertNotNull(task);
        assertInstanceOf(Search.class,task);
    }

    /* * *
     * Test 2
     * * */
    @Test
    void GetAnalysisTask_SortKeyword_ReturnNull()
    {
        String keyword = "sort";
        List<String> instructions = new ArrayList<>(Arrays.asList("expr"));
        AnalysisTask task = interpreter.getAnalysisTask(keyword,instructions);
        assertNull(task);
    }

    /* * *
     * Test 3
     * * */
    @Test
    void GetAnalysisTask_FilterKeyword_ReturnNull()
    {
        String keyword = "filter";
        List<String> instructions = new ArrayList<>(Arrays.asList("expr"));
        AnalysisTask task = interpreter.getAnalysisTask(keyword,instructions);
        assertNull(task);
    }

    /* * *
     * Test 4
     * * */
    @Test
    void GetAnalysisTask_UndefinedKeyword_ReturnNull()
    {
        String keyword = "undefined";
        List<String> instructions = new ArrayList<>(Arrays.asList("expr"));
        AnalysisTask task = interpreter.getAnalysisTask(keyword,instructions);
        assertNull(task);
    }

    /* * *
     * Test 5
     * * */
    @Test
    void GetAnalysisTask_ValidSearchEntireLogEntryInstruction_ReturnSearchAnalysisTask()
    {
        /*** case where regex is not wrapped in forward slashes ***/
        String keyword = "search";
        List<String> instructions = new ArrayList<>(Arrays.asList("search_expr"));
        AnalysisTask task = interpreter.getAnalysisTask(keyword,instructions);
        assertNotNull(task);
        assertInstanceOf(Search.class,task);

        // verify that correct Search constructor is used
        assertNull(task.getAttributeType());
        assertNull(task.getDataID());
        // verify that the regex is stored correctly
        assertEquals("search_expr",task.getRegex());

        /*** case where regex is wrapped in forward slashes ***/
        instructions = new ArrayList<>(Arrays.asList("/search_expr/"));
        task = interpreter.getAnalysisTask(keyword,instructions);
        assertNotNull(task);
        assertInstanceOf(Search.class,task);

        // verify that correct Search constructor is used
        assertNull(task.getAttributeType());
        assertNull(task.getDataID());
        // verify that the regex is stored correctly
        assertEquals("search_expr",task.getRegex());

        /*** case where regex is wrapped in forward slashes and there is whitespace  ***/
        instructions = new ArrayList<>(Arrays.asList("/    search_expr  /"));
        task = interpreter.getAnalysisTask(keyword,instructions);
        assertNotNull(task);
        assertInstanceOf(Search.class,task);

        // verify that correct Search constructor is used
        assertNull(task.getAttributeType());
        assertNull(task.getDataID());
        // verify that the regex is stored correctly
        assertEquals("search_expr",task.getRegex());
    }

    /* * *
     * Test 6
     * * */
    @Test
    void GetAnalysisTask_ValidSearchDataValueInstruction_ReturnSearchAnalysisTask()
    {
        String keyword = "search";
        List<String> instructions = new ArrayList<>(Arrays.asList("dataValue","Fuel_Remaining","search_expr"));
        AnalysisTask task = interpreter.getAnalysisTask(keyword,instructions);
        assertNotNull(task);
        assertInstanceOf(Search.class,task);

        DataID thisDataID = null;
        for (DataID dataID : configuration.getDataIDList())
        {
             if (dataID.getName().equals("Fuel_Remaining"))
             {
                 thisDataID = dataID;
                 break;
             }
        }
        if (null == thisDataID)
        {
            fail("Configuration does not define the DataID: Fuel_Remaining.");
        }

        // verify that correct Search constructor is used
        assertEquals(LogEvent.AttributeType.DATAVALUE, task.getAttributeType());
        assertSame(thisDataID,task.getDataID());
        // verify that the regex is stored correctly
        assertEquals("search_expr",task.getRegex());
    }

    /* * *
     * Test 7
     * * */
    @Test
    void GetAnalysisTask_ValidSearchTimeStampInstruction_ReturnSearchAnalysisTask()
    {
        String keyword = "search";
        List<String> instructions = new ArrayList<>(Arrays.asList("timeStamp","search_expr"));
        AnalysisTask task = interpreter.getAnalysisTask(keyword,instructions);
        assertNotNull(task);
        assertInstanceOf(Search.class,task);

        // verify that correct Search constructor is used
        assertEquals(LogEvent.AttributeType.TIMESTAMP, task.getAttributeType());
        assertNull(task.getDataID());
        // verify that the regex is stored correctly
        assertEquals("search_expr",task.getRegex());
    }

    /* * *
     * Test 8
     * * */
    @Test
    void GetAnalysisTask_ValidSearchEventTypeInstruction_ReturnSearchAnalysisTask()
    {
        String keyword = "search";
        List<String> instructions = new ArrayList<>(Arrays.asList("event","search_expr"));
        AnalysisTask task = interpreter.getAnalysisTask(keyword,instructions);
        assertNotNull(task);
        assertInstanceOf(Search.class,task);

        // verify that correct Search constructor is used
        assertEquals(LogEvent.AttributeType.EVENT, task.getAttributeType());
        assertNull(task.getDataID());
        // verify that the regex is stored correctly
        assertEquals("search_expr",task.getRegex());
    }

    /* * *
     * Test 9
     * * */
    @Test
    void GetAnalysisTask_ValidSearchDataIDInstruction_ReturnSearchAnalysisTask()
    {
        String keyword = "search";
        List<String> instructions = new ArrayList<>(Arrays.asList("dataID","search_expr"));
        AnalysisTask task = interpreter.getAnalysisTask(keyword,instructions);
        assertNotNull(task);
        assertInstanceOf(Search.class,task);

        // verify that correct Search constructor is used
        assertEquals(LogEvent.AttributeType.DATAID, task.getAttributeType());
        assertNull(task.getDataID());
        // verify that the regex is stored correctly
        assertEquals("search_expr",task.getRegex());
    }

    /* * *
     * Test 10
     * * */
    @Test
    void GetAnalysisTask_ValidSearchDataTypeInstruction_ReturnSearchAnalysisTask()
    {
        String keyword = "search";
        List<String> instructions = new ArrayList<>(Arrays.asList("dataType","search_expr"));
        AnalysisTask task = interpreter.getAnalysisTask(keyword,instructions);
        assertNotNull(task);
        assertInstanceOf(Search.class,task);

        // verify that correct Search constructor is used
        assertEquals(LogEvent.AttributeType.DATATYPE, task.getAttributeType());
        assertNull(task.getDataID());
        // verify that the regex is stored correctly
        assertEquals("search_expr",task.getRegex());
    }

    /* * *
     * Test 11
     * * */
    @Test
    void GetAnalysisTask_EmptyInstructions_ReturnNull()
    {
        String keyword = "search";
        List<String> instructions = new ArrayList<>();
        AnalysisTask task = interpreter.getAnalysisTask(keyword,instructions);
        assertNull(task);
    }

    /* * *
     * Test 12
     * * */
    @Test
    void GetAnalysisTask_InvalidAttributeType_ReturnNull()
    {
        String keyword = "search";
        List<String> instructions = new ArrayList<>(Arrays.asList("undefined","search_expr"));
        AnalysisTask task = interpreter.getAnalysisTask(keyword,instructions);
        assertNull(task);
    }

    /* * *
     * Test 13
     * * */
    @Test
    void GetAnalysisTask_DataValueAttributeTypeInvalidNumberOfInstructions_ReturnNull()
    {
        String keyword = "search";
        List<String> instructions = new ArrayList<>(Arrays.asList("dataValue","search_expr"));
        AnalysisTask task = interpreter.getAnalysisTask(keyword,instructions);
        assertNull(task);
    }

    /* * *
     * Test 14
     * * */
    @Test
    void GetAnalysisTask_DataValueAttributeTypeInvalidDataID_ReturnNull()
    {
        String keyword = "search";
        List<String> instructions = new ArrayList<>(Arrays.asList("dataValue","undefined","search_expr"));
        AnalysisTask task = interpreter.getAnalysisTask(keyword,instructions);
        assertNull(task);
    }

    /* * *
     * Test 15
     * * */
    @Test
    void GetAnalysisTask_OtherAttributeTypeInvalidNumberOfInstructions_ReturnNull()
    {
        String keyword = "search";
        // timeStamp - too many instructions
        List<String> instructions = new ArrayList<>(Arrays.asList("timeStamp","undefined","search_expr"));
        AnalysisTask task = interpreter.getAnalysisTask(keyword,instructions);
        assertNull(task);

        // event - too many instructions
        instructions = new ArrayList<>(Arrays.asList("event","undefined","search_expr"));
        task = interpreter.getAnalysisTask(keyword,instructions);
        assertNull(task);

        // dataType - too many instructions
        instructions = new ArrayList<>(Arrays.asList("dataType","undefined","search_expr"));
        task = interpreter.getAnalysisTask(keyword,instructions);
        assertNull(task);

        // dataID - too many instructions
        instructions = new ArrayList<>(Arrays.asList("dataID","undefined","search_expr"));
        task = interpreter.getAnalysisTask(keyword,instructions);
        assertNull(task);
    }

    /* * *
     * Test 16
     * * */
    @Test
    void GetAnalysisTask_EmptyRegEx_ReturnNull()
    {
        String keyword = "search";
        List<String> instructions = new ArrayList<>(Arrays.asList("/    /"));
        AnalysisTask task = interpreter.getAnalysisTask(keyword,instructions);
        assertNull(task);

        instructions = new ArrayList<>(Arrays.asList(""));
        task = interpreter.getAnalysisTask(keyword,instructions);
        assertNull(task);

        instructions = new ArrayList<>(Arrays.asList("   "));
        task = interpreter.getAnalysisTask(keyword,instructions);
        assertNull(task);
    }
}
