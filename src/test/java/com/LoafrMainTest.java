package com;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;

import static com.github.stefanbirkner.systemlambda.SystemLambda.catchSystemExit;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The LoafrMainTest class provides tests for 11 possible scenarios that the system will encounter. One outright fails,
 * Loafr_parseConfigFileFailed_SystemQuits(), while three "passes" according to JUnit5 but actually fail due to not
 * outputting the expected error message. The rest pass as expected. Reasons at to why a given test fail is elaborated on
 * in their respective JavaDocs.
 *
 * @author Jeremiah Hockett
 */

public class LoafrMainTest {



    /**
     * Test method checks to see if Loafr will execute with a valid configuration file command input. The system must not
     * quit and execute normally. The test passes.
     */
    @Test
    void Loafr_NormalOperation_SystemExecutes(){
        String logFileName = "log_file_3";
        String scriptFileName = "test_script";
        String outputFileLocName = "output.txt";

        ClassLoader classLoader = getClass().getClassLoader();

        File logFile = new File(classLoader.getResource(logFileName).getFile());
        String logPath = logFile.getAbsolutePath();
        // script file - create temp file
        File scriptFile = new File(classLoader.getResource(scriptFileName).getFile());
        String scriptPath = scriptFile.getAbsolutePath();
        // output location - will be deleted after test completes
        File testOutputFileLoc = new File(outputFileLocName);
        String testOutputFilePath = testOutputFileLoc.getAbsolutePath();


        URL configurationFileLoc = getClass().getClassLoader().getResource("mock_config_files/sample_config_file_wrong_node_in_events.xml");

        try {
            catchSystemExit(() -> Loafr.main(new String[]{"-s", scriptPath, "-l", logPath, "-o", testOutputFilePath}));
            fail("Loafr has exited. Test failed.");
        } catch (AssertionError | Exception e){
            assertTrue(true);
        }
    }

    /**
     * Test method checks to see if an invalid configuration file causes Loafr to quit. The system must quit upon detecting
     * the error. However, it executes as normal since the configuration file is hardcoded in main.
     */
    @Test
    void Loafr_parseConfigFileFailed_SystemQuits(){
        String logFileName = "log_file_3";
        String scriptFileName = "test_script";
        String outputFileLocName = "output.txt";

        ClassLoader classLoader = getClass().getClassLoader();

        File logFile = new File(classLoader.getResource(logFileName).getFile());
        String logPath = logFile.getAbsolutePath();
        // script file - create temp file
        File scriptFile = new File(classLoader.getResource(scriptFileName).getFile());
        String scriptPath = scriptFile.getAbsolutePath();
        // output location - will be deleted after test completes
        File testOutputFileLoc = new File(outputFileLocName);
        String testOutputFilePath = testOutputFileLoc.getAbsolutePath();


        URL configurationFileLoc = getClass().getClassLoader().getResource("mock_config_files/sample_config_file_wrong_node_in_events.xml");

        try {
            catchSystemExit(() -> Loafr.main(new String[]{"-s", scriptPath, "-l", logPath, "-o", testOutputFilePath}));
        } catch (AssertionError | Exception e){
            fail("Loafr has not exited upon catching error. Test failed.");
        }
    }

    /**
     * Test method checks whether the system can detect an empty command input and output an error message saying that
     * the input contain syntax errors. The test passes.
     */
    @Test
    void Loafr_argumentsEmpty_SystemQuits(){
        String logFileName = "log_file_3";
        String scriptFileName = "test_script";
        String outputFileLocName = "output.txt";

        ClassLoader classLoader = getClass().getClassLoader();

        File logFile = new File(classLoader.getResource(logFileName).getFile());
        String logPath = logFile.getAbsolutePath();
        // script file - create temp file
        File scriptFile = new File(classLoader.getResource(scriptFileName).getFile());
        String scriptPath = scriptFile.getAbsolutePath();
        // output location - will be deleted after test completes
        File testOutputFileLoc = new File(outputFileLocName);
        String testOutputFilePath = testOutputFileLoc.getAbsolutePath();


        URL configurationFileLoc = getClass().getClassLoader().getResource("mock_config_files/sample_config_file.xml");
        try {
            catchSystemExit(() -> Loafr.main(new String[]{}));
            assertTrue(true);
        } catch (AssertionError | Exception e){
            fail("Loafr has not exited upon catching error. Test failed.");
        }
    }

    /**
     * Test method checks whether the system can detect a commmand input with an incorrect script flag and output an
     * error message saying that the input contain syntax errors. The test passes.
     */
    @Test
    void Loafr_argumentsHasIncorrectScriptFlag_SystemQuits(){
        String logFileName = "log_file_3";
        String scriptFileName = "test_script";
        String outputFileLocName = "output.txt";

        ClassLoader classLoader = getClass().getClassLoader();

        File logFile = new File(classLoader.getResource(logFileName).getFile());
        String logPath = logFile.getAbsolutePath();
        // script file - create temp file
        File scriptFile = new File(classLoader.getResource(scriptFileName).getFile());
        String scriptPath = scriptFile.getAbsolutePath();
        // output location - will be deleted after test completes
        File testOutputFileLoc = new File(outputFileLocName);
        String testOutputFilePath = testOutputFileLoc.getAbsolutePath();


        URL configurationFileLoc = getClass().getClassLoader().getResource("mock_config_files/sample_config_file.xml");
        try {
            catchSystemExit(() -> Loafr.main(new String[]{"-p", scriptPath, "-l", logPath, "-o", testOutputFilePath}));
            assertTrue(true);
        } catch (AssertionError | Exception e){
            fail("Loafr has not exited upon catching error. Test failed.");
        }
    }

    /**
     * Test method checks whether the system can detect a commmand input with an incorrect output flag and output an
     * error message saying that the input contain syntax errors. However, the error message specifies that the input is
     * "too long." which results in the test failing.
     */
    @Test
    void Loafr_argumentsHasIncorrectOutputFlag_SystemQuits(){
        String logFileName = "log_file_3";
        String scriptFileName = "test_script";
        String outputFileLocName = "output.txt";

        ClassLoader classLoader = getClass().getClassLoader();

        File logFile = new File(classLoader.getResource(logFileName).getFile());
        String logPath = logFile.getAbsolutePath();
        // script file - create temp file
        File scriptFile = new File(classLoader.getResource(scriptFileName).getFile());
        String scriptPath = scriptFile.getAbsolutePath();
        // output location - will be deleted after test completes
        File testOutputFileLoc = new File(outputFileLocName);
        String testOutputFilePath = testOutputFileLoc.getAbsolutePath();


        URL configurationFileLoc = getClass().getClassLoader().getResource("mock_config_files/sample_config_file.xml");
        try {
            catchSystemExit(() -> Loafr.main(new String[]{"-s", scriptPath, "-l", logPath, "-p", testOutputFilePath}));
            assertTrue(true);
        } catch (AssertionError | Exception e){
            fail("Loafr has not exited upon catching error. Test failed.");
        }
    }

    /**
     * Test method checks whether the system can detect a commmand input with a null output location with corresponding
     * output flag and print an error message saying that the input contain syntax errors. The test resulted in two similar
     * error message printed in the terminal, so the test fails.
     */
    @Test
    void Loafr_argumentsHasIncorrectOutputLocNull_SystemQuits(){
        String logFileName = "log_file_3";
        String scriptFileName = "test_script";
        String outputFileLocName = "output.txt";

        ClassLoader classLoader = getClass().getClassLoader();

        File logFile = new File(classLoader.getResource(logFileName).getFile());
        String logPath = logFile.getAbsolutePath();
        // script file - create temp file
        File scriptFile = new File(classLoader.getResource(scriptFileName).getFile());
        String scriptPath = scriptFile.getAbsolutePath();
        // output location - will be deleted after test completes
        File testOutputFileLoc = new File(outputFileLocName);
        String testOutputFilePath = testOutputFileLoc.getAbsolutePath();


        URL configurationFileLoc = getClass().getClassLoader().getResource("mock_config_files/sample_config_file.xml");
        try {
            catchSystemExit(() -> Loafr.main(new String[]{"-s", scriptPath, "-l", logPath, "-o"}));
            assertTrue(true);
        } catch (AssertionError | Exception e){
            fail("Loafr has not exited upon catching error. Test failed.");
        }
    }

    /**
     * Test method checks whether the system can detect a command input only the script flag and script location and
     * print an error message saying that the input is too short. The test passes.
     */
    @Test
    void Loafr_argumentsTooShort_SystemQuits() {
        String logFileName = "log_file_3";
        String scriptFileName = "test_script";
        String outputFileLocName = "output.txt";

        ClassLoader classLoader = getClass().getClassLoader();

        File logFile = new File(classLoader.getResource(logFileName).getFile());
        String logPath = logFile.getAbsolutePath();
        // script file - create temp file
        File scriptFile = new File(classLoader.getResource(scriptFileName).getFile());
        String scriptPath = scriptFile.getAbsolutePath();
        // output location - will be deleted after test completes
        File testOutputFileLoc = new File(outputFileLocName);
        String testOutputFilePath = testOutputFileLoc.getAbsolutePath();


        URL configurationFileLoc = getClass().getClassLoader().getResource("mock_config_files/sample_config_file.xml");
        try {
            catchSystemExit(() -> Loafr.main(new String[]{"-s", scriptPath}));
            assertTrue(true);
        } catch (AssertionError | Exception e) {
            fail("Loafr has not exited upon catching error. Test failed.");
        }
    }

    /**
     * Test method checks whether the system can detect a command input with a null logfile location with corresponding
     * and print an error message saying that the input contain syntax errors. The test resulted in the message saying
     * that the input is too long, so the test fails.
     */
    @Test
    void Loafr_argumentsHasLogFileLocNull_SystemQuits(){
        String logFileName = "log_file_3";
        String scriptFileName = "test_script";
        String outputFileLocName = "output.txt";

        ClassLoader classLoader = getClass().getClassLoader();

        File logFile = new File(classLoader.getResource(logFileName).getFile());
        String logPath = logFile.getAbsolutePath();
        // script file - create temp file
        File scriptFile = new File(classLoader.getResource(scriptFileName).getFile());
        String scriptPath = scriptFile.getAbsolutePath();
        // output location - will be deleted after test completes
        File testOutputFileLoc = new File(outputFileLocName);
        String testOutputFilePath = testOutputFileLoc.getAbsolutePath();


        URL configurationFileLoc = getClass().getClassLoader().getResource("mock_config_files/sample_config_file.xml");
        try {
            catchSystemExit(() -> Loafr.main(new String[]{"-s", scriptPath, "-l", "", "-p", testOutputFilePath}));
            assertTrue(true);
        } catch (AssertionError | Exception e){
            fail("Loafr has not exited upon catching error. Test failed.");
        }
    }

    /**
     * Test method checks whether the system can detect a command input with duplicate log file flags and locations and
     * print an error message saying that the input is too long. The test passes.
     */
    @Test
    void Loafr_argumentsTooLong_SystemQuits(){
        String logFileName = "log_file_3";
        String scriptFileName = "test_script";
        String outputFileLocName = "output.txt";

        ClassLoader classLoader = getClass().getClassLoader();

        File logFile = new File(classLoader.getResource(logFileName).getFile());
        String logPath = logFile.getAbsolutePath();
        // script file - create temp file
        File scriptFile = new File(classLoader.getResource(scriptFileName).getFile());
        String scriptPath = scriptFile.getAbsolutePath();
        // output location - will be deleted after test completes
        File testOutputFileLoc = new File(outputFileLocName);
        String testOutputFilePath = testOutputFileLoc.getAbsolutePath();


        URL configurationFileLoc = getClass().getClassLoader().getResource("mock_config_files/sample_config_file.xml");
        try {
            catchSystemExit(() -> Loafr.main(new String[]{"-s", scriptPath, "-l", logPath, "-l", logPath,}));
            assertTrue(true);
        } catch (AssertionError | Exception e){
            fail("Loafr has not exited upon catching error. Test failed.");
        }
    }

    /**
     * Test method checks whether the system can detect a command input with an incorrect logloc flag and output an
     * error message saying that the input contain syntax errors. The test passes.
     */
    @Test
    void Loafr_argumentsHasIncorrectLocFlags_SystemQuits(){
        String logFileName = "log_file_3";
        String scriptFileName = "test_script";
        String outputFileLocName = "output.txt";

        ClassLoader classLoader = getClass().getClassLoader();

        File logFile = new File(classLoader.getResource(logFileName).getFile());
        String logPath = logFile.getAbsolutePath();
        // script file - create temp file
        File scriptFile = new File(classLoader.getResource(scriptFileName).getFile());
        String scriptPath = scriptFile.getAbsolutePath();
        // output location - will be deleted after test completes
        File testOutputFileLoc = new File(outputFileLocName);
        String testOutputFilePath = testOutputFileLoc.getAbsolutePath();


        URL configurationFileLoc = getClass().getClassLoader().getResource("mock_config_files/sample_config_file.xml");
        try {
            catchSystemExit(() -> Loafr.main(new String[]{"-s", scriptPath, "-p", logPath, "-o", testOutputFilePath}));
            assertTrue(true);
        } catch (AssertionError | Exception e){
            fail("Loafr has not exited upon catching error. Test failed.");
        }
    }

    /**
     * Test method checks whether the system can detect a command input with no output file location and flag buy with
     * a missing log file location and print an error message saying that the input is too short. The test passes.
     */
    @Test
    void Loafr_shortArgumentsHasLogFileLocNull_SystemQuits() {
        String logFileName = "log_file_3";
        String scriptFileName = "test_script";
        String outputFileLocName = "output.txt";

        ClassLoader classLoader = getClass().getClassLoader();

        File logFile = new File(classLoader.getResource(logFileName).getFile());
        String logPath = logFile.getAbsolutePath();
        // script file - create temp file
        File scriptFile = new File(classLoader.getResource(scriptFileName).getFile());
        String scriptPath = scriptFile.getAbsolutePath();
        // output location - will be deleted after test completes
        File testOutputFileLoc = new File(outputFileLocName);
        String testOutputFilePath = testOutputFileLoc.getAbsolutePath();


        URL configurationFileLoc = getClass().getClassLoader().getResource("mock_config_files/sample_config_file.xml");
        try {
            catchSystemExit(() -> Loafr.main(new String[]{"-s", scriptPath, "-l", ""}));
            assertTrue(true);
        } catch (AssertionError | Exception e) {
            fail("Loafr has not exited upon catching error. Test failed.");
        }
    }
}
