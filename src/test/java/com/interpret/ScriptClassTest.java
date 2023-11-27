package com.interpret;

import com.analyze.AnalysisTask;
import com.input.Configuration;
import com.analyze.Search;
import com.input.LogEvent;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This test class tests the public method interpretScript of the Script class.
 * Test cases:
 *      Test 1: interpretScript method is called with valid file location and a single valid script instruction, returns true
 *      Test 2: interpretScript method is called with valid file location and multiple valid script instructions, returns true
 *      Test 3: interpretScript method is called with invalid file location, returns false
 *      Test 4: interpretScript method is called with NULL for the file location parameter, returns false
 *      Test 5: interpretScript method is called with valid file location but the file is empty, returns false
 *      Test 6: interpretScript method is called with valid file location but there are invalid keywords in the script,
 *                                                          so the interpreter returns a NULL task, return false
 *
 * @author Leah Lehmeier
 */
public class ScriptClassTest
{
    static Interpreter interpreter;
    static Script script;

    @BeforeAll
    static void beforeAll()
    {
        URL configurationFileLoc = ScriptClassTest.class.getClassLoader().getResource("sample_config_file.xml");
        if (null == configurationFileLoc)
        {
            fail("Test resource \"sample_config_file.xml\" was not found.");
        }
        Configuration configuration = new Configuration();
        if (Boolean.FALSE == configuration.parseConfigFile(configurationFileLoc))
        {
            fail("Configuration file parsing failure.");
        }
        interpreter = new Interpreter(configuration);
    }

    /* * *
     * Test 1
     * * */
    @Test
    void InterpretScript_ValidFileContainingValidScriptSingleInstruction_ReturnTrue()
    {
        URL scriptFileLoc = getClass().getClassLoader().getResource("mock_scripts/mock_script_single_search_instruction.txt");

        if (null == scriptFileLoc)
        {
            fail("Test resource \"mock_script_single_search_instruction.txt\" was not found.");
        }

        script = new Script();
        Boolean interpretScript = script.interpretScript(scriptFileLoc.getPath(),interpreter);
        assertTrue(interpretScript);

        List<AnalysisTask> queue = script.getAnalyzer().getTaskQueue();
        // verify that the new task is added to the analyzer queue
        assertEquals(1,queue.size());
        assertInstanceOf(Search.class,queue.get(0));
    }

    /* * *
     * Test 2
     * * */
    @Test
    void InterpretScript_ValidFileContainingValidScriptMultipleInstructions_ReturnTrue()
    {
        URL scriptFileLoc = getClass().getClassLoader().getResource("mock_scripts/mock_script_multiple_search_instructions.txt");

        if (null == scriptFileLoc)
        {
            fail("Test resource \"mock_script_multiple_search_instructions.txt\" was not found.");
        }

        script = new Script();
        Boolean interpretScript = script.interpretScript(scriptFileLoc.getPath(),interpreter);
        assertTrue(interpretScript);

        List<AnalysisTask> queue = script.getAnalyzer().getTaskQueue();
        // verify that the new tasks are added to the analyzer queue
        assertEquals(2,queue.size());
        assertInstanceOf(Search.class,queue.get(0));
        assertInstanceOf(Search.class,queue.get(1));
        // verify that the tasks are added to the queue in the correct order
        assertEquals(LogEvent.AttributeType.DATAVALUE,queue.get(0).getAttributeType());
        assertEquals(LogEvent.AttributeType.EVENT,queue.get(1).getAttributeType());
    }

    /* * *
     * Test 3
     * * */
    @Test
    void InterpretScript_FileDoesNotExist_ReturnFalse()
    {
        script = new Script();
        Boolean interpretScript = script.interpretScript("file_does_not_exist",interpreter);
        assertFalse(interpretScript);
    }

    /* * *
     * Test 4
     * * */
    @Test
    void InterpretScript_FileLocationParameterIsNull_ReturnFalse()
    {
        script = new Script();
        Boolean interpretScript = script.interpretScript(null,interpreter);
        assertFalse(interpretScript);
    }

    /* * *
     * Test 5
     * * */
    @Test
    void InterpretScript_FileIsEmpty_ReturnFalse()
    {
        URL scriptFileLoc = getClass().getClassLoader().getResource("mock_scripts/mock_script_empty_file.txt");

        if (null == scriptFileLoc)
        {
            fail("Test resource \"mock_script_empty_file.txt\" was not found.");
        }

        script = new Script();
        Boolean interpretScript = script.interpretScript(scriptFileLoc.getPath(),interpreter);
        assertFalse(interpretScript);
    }

    /* * *
     * Test 6
     * * */
    @Test
    void InterpretScript_InterpreterReturnsNullTask_ReturnFalse()
    {
        URL scriptFileLoc = getClass().getClassLoader().getResource("mock_scripts/mock_script_invalid_keywords.txt");

        if (null == scriptFileLoc)
        {
            fail("Test resource \"mock_script_invalid_keywords.txt\" was not found.");
        }

        script = new Script();
        Boolean interpretScript = script.interpretScript(scriptFileLoc.getPath(),interpreter);
        assertFalse(interpretScript);
    }
}
