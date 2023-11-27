package com.control;

import com.Loafr;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

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
    static String logFileName;
    static String scriptFileName;
    static File outputFile;
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

    @AfterAll
    static void tearDown()
    {
        if (outputFile.exists())
        {
            outputFile.delete();
        }
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
        File testOutputFileLoc = new File(outputFileLocName);
        String testOutputFilePath = testOutputFileLoc.getAbsolutePath();

        Loafr loafr = new Loafr();
        loafr.main(new String[]{"-s",scriptPath,"-l",logPath,"-o",testOutputFilePath});

        outputFile = new File("output.txt");
        // if this file exists, Loafr must have executed correctly a did not exit before completion
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
    }
}
