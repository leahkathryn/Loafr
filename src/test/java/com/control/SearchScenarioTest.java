package com.control;

import com.Loafr;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;
import static com.github.stefanbirkner.systemlambda.SystemLambda.*;

/**
 * End-to-End test scenario.
 *      I am not sure what is correct here.
 *      Should this test run all of Loafr and only verify that the output is as expected?
 *              ^ this is what this test class does
 *      Or should Loafr be tested as it executes?
 *
 * @author Leah Lehmeier
 */
public class SearchScenarioTest
{
    // @TempDir annotation will delete the file at this location after test completes
    @TempDir File testOutputFileLoc;

    static String logFileName;
    static String scriptFileName;
    static String outputFileLocName;
    static String outputFirstLine;
    static String outputSecondLine;
    static String outputThirdLine;

    @BeforeAll
    static void setup()
    {
        logFileName = "log_file_3";
        scriptFileName = "test_script";
        outputFileLocName = "output.txt";
        outputFirstLine = "2023-11-12 08:10:00.0,Communication_Check,failure,120,[16.5 102.1 76.8],0";
        outputSecondLine = "2023-11-12 08:15:00.0,Communication_Check,success,0,[32.5 54.4 76.8],100";
        outputThirdLine = "2023-11-12 09:35:00.0,Communication_Check,success,0,[32.5 54.4 76.8],100";
    }

    @Test
    void Loafr_SearchForEventType_Success()
    {
        // create command line arguments
        ClassLoader classLoader = getClass().getClassLoader();
        // log file - create temp file
        File logFile = new File(classLoader.getResource(logFileName).getFile());
        String logPath = logFile.getAbsolutePath();
        // script file - create temp file
        File scriptFile = new File(classLoader.getResource(scriptFileName).getFile());
        String scriptPath = scriptFile.getAbsolutePath();
        // output location - will be deleted after test completes
        testOutputFileLoc = new File(outputFileLocName);
        String testOutputFilePath = testOutputFileLoc.getAbsolutePath();

        Loafr loafr = new Loafr();

        // catchSystemExit() throws an AssertionError if System.exit is not called
        try {
            catchSystemExit(() -> {
                loafr.main(new String[]{"-s",scriptPath,"-l",logPath,"-o",testOutputFilePath});
            });
        }
        // in this case, the program has not exited -> success and check output
        catch (AssertionError | Exception ex)
        {
//            File outputDir = new File("output");
//            assertTrue(outputDir.exists());
//            assertTrue(outputDir.isDirectory());

            File outputFile = new File("output.txt");
            assertTrue(outputFile.exists());
            assertTrue(outputFile.isFile());

            List<String> outputLines = new ArrayList<>();
            try
            {
                Scanner myReader = new Scanner(outputFile);
                // read every line of the file into the list
                while (myReader.hasNextLine()) {
                    String output = myReader.nextLine();
                    // do not add blank line to the list
                    if (!output.isBlank())
                    {
                        outputLines.add(output.strip());
                    }
                }
                myReader.close();
            } catch (FileNotFoundException | NullPointerException e) {
                fail();
            }

            // verify output file is not empty
            assertFalse(outputLines.isEmpty());

            assertEquals(3,outputLines.size());

            // verify output is expected
            assertEquals(outputFirstLine,outputLines.get(0));
            assertEquals(outputSecondLine,outputLines.get(1));
            assertEquals(outputThirdLine,outputLines.get(2));

            if (outputFile.exists())
            {
                outputFile.delete();
            }
            // "passes" the test, ends test case
            return;
        }
        // in this case, the program did exit -> fail
        fail();
    }
}
