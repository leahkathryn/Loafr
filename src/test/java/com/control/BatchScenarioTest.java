package com.control;

import com.Loafr;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * End-to-End batch processing test scenario.
 *
 * @author Leah Lehmeier
 */
public class BatchScenarioTest
{
    static String logFileName1;
    static String logFileName3;
    static String logFileName4;
    static String scriptFileName;
    static File outputDir;
    static String outputFileLocName;
    static String outputFirstLine;
    static String outputSecondLine;
    static String outputThirdLine;
    static String outputFourthLine;

    @BeforeAll
    static void setup()
    {
        logFileName1 = "log_file_1";
        logFileName3 = "log_file_3";
        logFileName4 = "log_file_4";
        scriptFileName = "mock_scripts/mock_script_single_search_instruction.txt";
        outputFileLocName = "batch_test_output";
        outputFirstLine = "2023-11-12 09:00:00,Pre-Launch_Check,false,120,true,failure,0.001";
        outputSecondLine = "2023-11-12 09:40:00,Pre-Launch_Check,true,0,true,success,0.991";
        outputThirdLine = "2023-11-12 11:45:00,Pre-Launch_Check,true,0,true,success,0.999";
        outputFourthLine = "2023-11-12 12:00:00,Pre-Launch_Check,false,120,true,failure,0.001";
    }

    @AfterAll
    static void tearDown()
    {
        if (outputDir.exists())
        {
            if (outputDir.isDirectory())
            {
                File[] children = outputDir.listFiles();
                for (int i = 0; i < children.length; i++)
                {
                    if (!children[i].delete())
                    {
                        fail("Failed to delete the output file or directory after a test case.");
                    }
                }
            }
            if (!outputDir.delete())
            {
                fail("Failed to delete the output file or directory after a test case.");
            }
        }
    }

    @Test
    void Loafr_BatchProcessSingleScriptMultipleLogFilesMergeByEvent_Success() {
        // create command line arguments
        ClassLoader classLoader = getClass().getClassLoader();
        // log file
        File logFile1 = new File(classLoader.getResource(logFileName1).getFile());
        String logPath1 = logFile1.getAbsolutePath();
        File logFile3 = new File(classLoader.getResource(logFileName3).getFile());
        String logPath3 = logFile3.getAbsolutePath();
        File logFile4 = new File(classLoader.getResource(logFileName4).getFile());
        String logPath4 = logFile4.getAbsolutePath();

        // script file
        File scriptFile = new File(classLoader.getResource(scriptFileName).getFile());
        String scriptPath = scriptFile.getAbsolutePath();

        Loafr loafr = new Loafr();
        loafr.main(new String[]{"-b", "-s", scriptPath, "-l", logPath1, logPath3, logPath4, "-m", "event", "-o", outputFileLocName});

        outputDir = new File(outputFileLocName);
        // if this file exists, Loafr must have executed correctly a did not exit before completion
        assertTrue(outputDir.exists());
        assertTrue(outputDir.isDirectory());

        List<String> outputLines = new ArrayList<>();
        try {
            Scanner myReader = new Scanner(Paths.get(outputFileLocName,"merged_log_data_mock_script_single_search_instruction"));
            //Scanner myReader = new Scanner(outputFileLocName+"/merged_log_data_mock_script_single_search_instruction");
            // read every line of the file into the list
            while (myReader.hasNextLine()) {
                String output = myReader.nextLine();
                // do not add blank line to the list
                if (!output.isBlank()) {
                    outputLines.add(output.strip());
                }
            }
            myReader.close();
        } catch (IOException | NullPointerException e) {
            fail();
        }

        // verify output file is not empty
        assertFalse(outputLines.isEmpty());

        assertEquals(4, outputLines.size());

        // verify output is expected
        assertEquals(outputFirstLine, outputLines.get(0));
        assertEquals(outputSecondLine, outputLines.get(1));
        assertEquals(outputThirdLine, outputLines.get(2));
        assertEquals(outputFourthLine, outputLines.get(3));
    }
}
