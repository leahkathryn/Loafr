package com.control;

import com.input.Configuration;

import java.io.File;
import java.net.URL;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.fail;
import static com.github.stefanbirkner.systemlambda.SystemLambda.catchSystemExit;

/**
 * This test class tests the public method execute of the SimpleScriptController class.
 * Test cases:
 *      Class is initialized with an invalid script, System exit is called
 *      Class is initialized with an invalid log file, System exit is called
 *      Class is initialized with an invalid output file location, System exit is called
 *
 * @author Leah Lehmeier
 */
public class SimpleScriptControllerTest
{
    static Configuration configuration;

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
    }

    @Test
    void Execute_InvalidScript_SystemExit()
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
        try {
            catchSystemExit(() -> {
                controller.execute();
            });
        } catch (Exception ex)
        {
            fail("Exception from System exit check.");
        }
    }

    @Test
    void Execute_InvalidOutputFileLocation_SystemExit()
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

        File dir = new File("output");
        if (!dir.mkdir())
        {
            fail("Error creating test directory.");
        }

        SimpleScriptController controller = new SimpleScriptController(configuration,logFileLoc.getPath(),scriptFileLoc.getPath(),"output");
        try {
            catchSystemExit(() -> {
                controller.execute();
            });
        }
        catch (Exception ex)
        {
            fail("Exception from System exit check.");
        }
    }

    @Test
    void Execute_InvalidLogFile_SystemExit()
    {
        URL scriptFileLoc = getClass().getClassLoader().getResource("mock_scripts/mock_script_single_search_instruction.txt");

        if (null == scriptFileLoc)
        {
            fail("Test resource \"mock_script_single_search_instruction.txt\" was not found.");
        }

        SimpleScriptController controller = new SimpleScriptController(configuration,"fail_on_purpose",scriptFileLoc.getPath(),"outputLocation.txt");
        try {
            catchSystemExit(() -> {
                controller.execute();
            });
        } catch (Exception ex)
        {
            fail("Exception from System exit check.");
        }
    }
}
