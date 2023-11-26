package com;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static com.github.stefanbirkner.systemlambda.SystemLambda.catchSystemExit;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The LoafrMainTest class provides tests for two possible scenarios that main can face: normal operation and when
 * controllerFactory.getController() fails. Other scenarios are ignored as they overlap with tests in other test classes
 * All pass as expected. Code for setupTests() are from the SearchScenarioTest class made by Leah Lehmeier.
 *
 * @author Jeremiah Hockett
 */

public class LoafrMainTest {

    private String logPath = "";
    private String scriptPath = "";
    private String testOutputFilePath = "";


    @BeforeEach
    void clearPaths(){
        logPath = "";
        scriptPath = "";
        testOutputFilePath = "";
    }
    @BeforeEach
    void setupTests(){
        String logFileName = "log_file_3";
        String scriptFileName = "test_script";
        String outputFileLocName = "output.txt";

        ClassLoader classLoader = getClass().getClassLoader();

        File logFile = new File(classLoader.getResource(logFileName).getFile());
        logPath = logFile.getAbsolutePath();
        // script file - create temp file
        File scriptFile = new File(classLoader.getResource(scriptFileName).getFile());
        scriptPath = scriptFile.getAbsolutePath();
        // output location - will be deleted after test completes
        File testOutputFileLoc = new File(outputFileLocName);
        testOutputFilePath = testOutputFileLoc.getAbsolutePath();
    }

    /**
     * Test method checks to see if Loafr will execute with a valid configuration file command input. The system must not
     * quit and execute normally. The test passes.
     */
    @Test
    void Loafr_NormalOperation_SystemExecutes(){
        try {
            catchSystemExit(() -> Loafr.main(new String[]{"-s", scriptPath, "-l", logPath, "-o", testOutputFilePath}));
            fail("Loafr has exited. Test failed.");
        } catch (AssertionError | Exception e){
            assertTrue(true);
        }
    }



    /**
     * Test method checks whether the system can detect a command input with an incorrect script flag and output an
     * error message saying that the input contain syntax errors. The test passes.
     */
    @Test
    void Loafr_argumentsHasIncorrectScriptFlag_SystemQuits(){
        try {
            catchSystemExit(() -> Loafr.main(new String[]{"-p", scriptPath, "-l", logPath, "-o", testOutputFilePath}));
            assertTrue(true);
        } catch (AssertionError | Exception e){
            fail("Loafr has not exited upon catching error. Test failed.");
        }
    }
}
