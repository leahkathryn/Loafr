package com.control;

import com.input.Configuration;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * This test class tests the public method execute of the SimpleScriptController class.
 * Test cases:
 *      Test 1: Class is initialized with an invalid script, returns false
 *      Test 2: Class is initialized with an invalid log file, returns false
 *      Test 3: Class is initialized with an invalid output file location, returns false
 *
 * @author Leah Lehmeier
 */
public class SimpleScriptControllerTest
{
    static Configuration configuration;
    static File dir;

    @BeforeAll
    static void beforeAll()
    {
        URL configurationFileLoc = SimpleScriptControllerTest.class.getClassLoader().getResource("sample_config_file.xml");
        if (null == configurationFileLoc)
        {
            fail("Test resource \"sample_config_file.xml\" was not found.");
        }
        configuration = new Configuration();
        if (Boolean.FALSE == configuration.parseConfigFile(configurationFileLoc))
        {
            fail("Configuration file parsing failure.");
        }

        dir = new File("output");
    }

    @AfterEach
    void afterEach()
    {
        if (dir.exists())
        {
            dir.delete();
        }
    }

    /* * *
     * Test 1
     * * */
    @Test
    void Execute_InvalidScript_ReturnFalse()
    {
        URL scriptFileLoc = getClass().getClassLoader().getResource("mock_scripts/mock_script_invalid_keywords.txt");

        if (null == scriptFileLoc)
        {
            fail("Test resource \"mock_script_invalid_keywords.txt\" was not found.");
        }

        URL logFileLoc = getClass().getClassLoader().getResource("log_file_3");

        if (null == logFileLoc)
        {
            fail("Test resource \"log_file_3\" was not found.");
        }

        SimpleScriptController controller = new SimpleScriptController(configuration,logFileLoc.getPath(),scriptFileLoc.getPath(),"outputLocation.txt");
        assertFalse(controller.execute());
    }

    /* * *
     * Test 2
     * * */
    @Test
    void Execute_InvalidOutputFileLocation_ReturnFalse()
    {
        URL scriptFileLoc = getClass().getClassLoader().getResource("mock_scripts/mock_script_single_search_instruction.txt");

        if (null == scriptFileLoc)
        {
            fail("Test resource \"mock_script_single_search_instruction.txt\" was not found.");
        }

        URL logFileLoc = getClass().getClassLoader().getResource("log_file_3");

        if (null == logFileLoc)
        {
            fail("Test resource \"log_file_3\" was not found.");
        }

        // make a directory with the output file name so that output writing will fail
        if (!dir.mkdir())
        {
            fail("Error creating test directory.");
        }

        SimpleScriptController controller = new SimpleScriptController(configuration,logFileLoc.getPath(),scriptFileLoc.getPath(),"output");

        assertFalse(controller.execute());
    }

    /* * *
     * Test 3
     * * */
    @Test
    void Execute_InvalidLogFile_ReturnFalse()
    {
        URL scriptFileLoc = getClass().getClassLoader().getResource("mock_scripts/mock_script_single_search_instruction.txt");

        if (null == scriptFileLoc)
        {
            fail("Test resource \"mock_script_single_search_instruction.txt\" was not found.");
        }

        SimpleScriptController controller = new SimpleScriptController(configuration,"fail_on_purpose",scriptFileLoc.getPath(),"outputLocation.txt");
        assertFalse(controller.execute());
    }
}
