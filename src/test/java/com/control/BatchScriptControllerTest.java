package com.control;

import com.input.Configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This test class tests the public method execute of the BatchScriptController class.
 * Test cases:
 *      Test 1: Class is initialized with one script and multiple log files and no merge instruction,
 *              returns true and output is as expected.
 *      Test 2: Class is initialized with multiple scripts and one log file and no merge instruction,
 *              returns true and output is as expected.
 *      Test 3: Class is initialized with multiple scripts and multiple log files and no merge instruction,
 *              returns true and output is as expected.
 *      Test 4: Class is initialized with one script and one log file and no merge instruction,
 *              returns true and output is as expected.
 *      Test 5: Class is initialized with one script and multiple log files with the merge-by-log-event instruction,
 *              returns true and output is as expected.
 *      Test 6: Class is initialized with one script and multiple log files with the merge-by-log-file instruction,
 *              returns true and output is as expected.
 *      Test 7: Class is initialized with one script and one log file with a valid merge instruction,
 *              returns true and output is as expected.
 *      Test 8: Class is initialized with one invalid script and other valid scripts,
 *              returns true and outputs warning about invalid script that was not processed,
 *              the rest of the output is as expected.
 *      Test 9: Class is initialized with one invalid log file and other valid log files,
 *              returns true and outputs warning about invalid log file that was not processed,
 *              the rest of the output is as expected.
 *      Test 10: Class is initialized with one valid script and multiple valid log files and an invalid merge instruction,
 *               return false.
 *      Test 11: Class is initialized with one valid script and all of the log files are invalid, return false.
 *      Test 12: Class is initialized with an invalid script and multiple valid log files, return false.
 *      Test 13: Class is initialized with an invalid script and an invalid log file, return false.
 *      Test 14: Class is initialized with an invalid output file location, return false.
 *
 * @author Leah Lehmeier
 */
public class BatchScriptControllerTest
{
    static Configuration configuration;
    static File output;
    static String outputName;
    static String outputLogFile1_PreLaunch_Check;
    static String outputLogFile1_Communication_Check;
    static String outputLogFile4_PreLaunch_Check;
    static String outputLogFile4_Communication_Check_1;
    static String outputLogFile4_Communication_Check_2;
    static String outputLogFile3_PreLaunch_Check_1;
    static String outputLogFile3_PreLaunch_Check_2;

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

        outputName = "outputDirectory";
        output = new File(outputName);

        outputLogFile1_PreLaunch_Check = "2023-11-12 12:00:00,Pre-Launch_Check,false,120,true,failure,0.001";
        outputLogFile1_Communication_Check = "2023-11-12 08:30:00,Communication_Check,success,0,[32.5 54.4 76.8],100";
        outputLogFile4_PreLaunch_Check = "2023-11-12 11:45:00,Pre-Launch_Check,true,0,true,success,0.999";
        outputLogFile4_Communication_Check_1 = "2023-11-12 08:10:00,Communication_Check,success,0,[32.5 54.4 76.8],100";
        outputLogFile4_Communication_Check_2 = "2023-11-12 08:30:00,Communication_Check,success,0,[45.6 7.4 102.2],100";
        outputLogFile3_PreLaunch_Check_1 = "2023-11-12 09:00:00,Pre-Launch_Check,false,120,true,failure,0.001";
        outputLogFile3_PreLaunch_Check_2 = "2023-11-12 09:40:00,Pre-Launch_Check,true,0,true,success,0.991";
    }

    @AfterEach
    void afterEach()
    {
        if (null != output && output.exists())
        {
            if (output.isDirectory())
            {
                File[] children = output.listFiles();
                for (int i = 0; i < children.length; i++)
                {
                    if (!children[i].delete())
                    {
                        fail("Failed to delete the output file or directory after a test case.");
                    }
                }
            }
            if (!output.delete())
            {
                fail("Failed to delete the output file or directory after a test case.");
            }
        }
    }

    /* * *
     * Test 1
     * * */
    @Test
    void Execute_ValidScriptMultipleValidLogFilesNoMerge_ReturnTrue()
    {
        List<String> logFileNames = new ArrayList<>(Arrays.asList("log_file_1","log_file_4","log_file_3"));
        List<String> logFileLocs = getFilePathList(logFileNames);
        List<String> scriptFileNames = new ArrayList<>(Arrays.asList("mock_scripts/mock_script_single_search_instruction.txt"));
        List<String> scriptFileLocs = getFilePathList(scriptFileNames);

        BatchScriptController controller = new BatchScriptController(configuration,"",logFileLocs,scriptFileLocs,outputName);
        assertTrue(controller.execute());

        // this combination of input should generate 3 output files
        // the provided output location is stored in the global outputName
        // BatchScriptController should generate a directory with this name
        // within the directory there will be output files named as logfilename_scriptname.txt

        output = new File(outputName);
        // if this file exists, BatchScriptController must have executed correctly a did not exit before completion
        assertTrue(output.exists());
        assertTrue(output.isDirectory());

        // check first output
        List<String> outputLines = getOutput(outputName+"/log_file_1_mock_script_single_search_instruction");
        // verify output file is not empty
        assertFalse(outputLines.isEmpty());
        assertEquals(1,outputLines.size());
        // verify output is expected
        assertEquals(outputLogFile1_PreLaunch_Check,outputLines.get(0));

        // check second output
        outputLines = getOutput(outputName+"/log_file_4_mock_script_single_search_instruction");
        // verify output file is not empty
        assertFalse(outputLines.isEmpty());
        assertEquals(1,outputLines.size());
        // verify output is expected
        assertEquals(outputLogFile4_PreLaunch_Check,outputLines.get(0));

        // check third output
        outputLines = getOutput(outputName+"/log_file_3_mock_script_single_search_instruction");
        // verify output file is not empty
        assertFalse(outputLines.isEmpty());
        assertEquals(2,outputLines.size());
        // verify output is expected
        assertEquals(outputLogFile3_PreLaunch_Check_1,outputLines.get(0));
        assertEquals(outputLogFile3_PreLaunch_Check_2,outputLines.get(1));
    }

    /* * *
     * Test 2
     * * */
    @Test
    void Execute_MultipleValidScriptsValidLogFileNoMerge_ReturnTrue()
    {
        List<String> logFileNames = new ArrayList<>(Arrays.asList("log_file_1"));
        List<String> logFileLocs = getFilePathList(logFileNames);
        List<String> scriptFileNames = new ArrayList<>(Arrays.asList("mock_scripts/mock_script_single_search_instruction.txt","mock_scripts/mock_script_single_search_instruction_2.txt"));
        List<String> scriptFileLocs = getFilePathList(scriptFileNames);

        BatchScriptController controller = new BatchScriptController(configuration,"",logFileLocs,scriptFileLocs,outputName);
        assertTrue(controller.execute());

        // this combination of input should generate 2 output files
        // the provided output location is stored in the global outputName
        // BatchScriptController should generate a directory with this name
        // within the directory there will be output files named as logfilename_scriptname.txt

        output = new File(outputName);
        // if this file exists, BatchScriptController must have executed correctly a did not exit before completion
        assertTrue(output.exists());
        assertTrue(output.isDirectory());

        // check first output
        List<String> outputLines = getOutput(outputName+"/log_file_1_mock_script_single_search_instruction");
        // verify output file is not empty
        assertFalse(outputLines.isEmpty());
        assertEquals(1,outputLines.size());
        // verify output is expected
        assertEquals(outputLogFile1_PreLaunch_Check,outputLines.get(0));

        // check second output
        outputLines = getOutput(outputName+"/log_file_1_mock_script_single_search_instruction_2");
        // verify output file is not empty
        assertFalse(outputLines.isEmpty());
        assertEquals(1,outputLines.size());
        // verify output is expected
        assertEquals(outputLogFile1_Communication_Check,outputLines.get(0));
    }

    /* * *
     * Test 3
     * * */
    @Test
    void Execute_MultipleValidScriptsMultipleValidLogFilesNoMerge_ReturnTrue()
    {
        List<String> logFileNames = new ArrayList<>(Arrays.asList("log_file_1","log_file_4"));
        List<String> logFileLocs = getFilePathList(logFileNames);
        List<String> scriptFileNames = new ArrayList<>(Arrays.asList("mock_scripts/mock_script_single_search_instruction.txt","mock_scripts/mock_script_single_search_instruction_2.txt"));
        List<String> scriptFileLocs = getFilePathList(scriptFileNames);

        BatchScriptController controller = new BatchScriptController(configuration,"",logFileLocs,scriptFileLocs,outputName);
        assertTrue(controller.execute());

        // this combination of input should generate 4 output files
        // the provided output location is stored in the global outputName
        // BatchScriptController should generate a directory with this name
        // within the directory there will be output files named as logfilename_scriptname.txt

        output = new File(outputName);
        // if this file exists, BatchScriptController must have executed correctly a did not exit before completion
        assertTrue(output.exists());
        assertTrue(output.isDirectory());

        // check first output
        List<String> outputLines = getOutput(outputName+"/log_file_1_mock_script_single_search_instruction");
        // verify output file is not empty
        assertFalse(outputLines.isEmpty());
        assertEquals(1,outputLines.size());
        // verify output is expected
        assertEquals(outputLogFile1_PreLaunch_Check,outputLines.get(0));

        // check second output
        outputLines = getOutput(outputName+"/log_file_1_mock_script_single_search_instruction_2");
        // verify output file is not empty
        assertFalse(outputLines.isEmpty());
        assertEquals(1,outputLines.size());
        // verify output is expected
        assertEquals(outputLogFile1_Communication_Check,outputLines.get(0));

        // check third output
        outputLines = getOutput(outputName+"/log_file_4_mock_script_single_search_instruction");
        // verify output file is not empty
        assertFalse(outputLines.isEmpty());
        assertEquals(1,outputLines.size());
        // verify output is expected
        assertEquals(outputLogFile4_PreLaunch_Check,outputLines.get(0));

        // check fourth output
        outputLines = getOutput(outputName+"/log_file_4_mock_script_single_search_instruction_2");
        // verify output file is not empty
        assertFalse(outputLines.isEmpty());
        assertEquals(2,outputLines.size());
        // verify output is expected
        assertEquals(outputLogFile4_Communication_Check_1,outputLines.get(0));
        assertEquals(outputLogFile4_Communication_Check_2,outputLines.get(1));
    }

    /* * *
     * Test 4
     * * */
    @Test
    void Execute_ValidScriptValidLogFileNoMerge_ReturnTrue()
    {
        List<String> logFileNames = new ArrayList<>(Arrays.asList("log_file_1"));
        List<String> logFileLocs = getFilePathList(logFileNames);
        List<String> scriptFileNames = new ArrayList<>(Arrays.asList("mock_scripts/mock_script_single_search_instruction.txt"));
        List<String> scriptFileLocs = getFilePathList(scriptFileNames);

        BatchScriptController controller = new BatchScriptController(configuration,"",logFileLocs,scriptFileLocs,outputName);
        assertTrue(controller.execute());

        // this combination of input should generate 1 output file
        // the provided output location is stored in global outputName
        // BatchScriptController should generate a directory with this name
        // within the directory there will be output files named as logfilename_scriptname.txt

        output = new File(outputName);
        // if this file exists, BatchScriptController must have executed correctly a did not exit before completion
        assertTrue(output.exists());
        assertTrue(output.isDirectory());

        // check first output
        List<String> outputLines = getOutput(outputName+"/log_file_1_mock_script_single_search_instruction");
        // verify output file is not empty
        assertFalse(outputLines.isEmpty());
        assertEquals(1,outputLines.size());
        // verify output is expected
        assertEquals(outputLogFile1_PreLaunch_Check,outputLines.get(0));
    }

    /* * *
     * Test 5
     * * */
    @Test
    void Execute_ValidScriptMultipleValidLogFilesMergeByLogEvent_ReturnTrue()
    {
        List<String> logFileNames = new ArrayList<>(Arrays.asList("log_file_1","log_file_4","log_file_3"));
        List<String> logFileLocs = getFilePathList(logFileNames);
        List<String> scriptFileNames = new ArrayList<>(Arrays.asList("mock_scripts/mock_script_single_search_instruction.txt"));
        List<String> scriptFileLocs = getFilePathList(scriptFileNames);

        BatchScriptController controller = new BatchScriptController(configuration,"event",logFileLocs,scriptFileLocs,outputName);
        assertTrue(controller.execute());

        // this combination of input should generate 1 output file
        // the provided output location is 'outputFile.txt'
        // LogData should generate a file with this name

        output = new File(outputName);
        // if this file exists, BatchScriptController must have executed correctly a did not exit before completion
        assertTrue(output.exists());
        assertTrue(output.isDirectory());

        // check first output
        List<String> outputLines = getOutput(outputName+"/merged_log_data_mock_script_single_search_instruction");

        // verify output file is not empty
        assertFalse(outputLines.isEmpty());
        assertEquals(4,outputLines.size());
        // verify output is expected - the entries should be in chronological order
        assertEquals(outputLogFile3_PreLaunch_Check_1,outputLines.get(0));
        assertEquals(outputLogFile3_PreLaunch_Check_2,outputLines.get(1));
        assertEquals(outputLogFile4_PreLaunch_Check,outputLines.get(2));
        assertEquals(outputLogFile1_PreLaunch_Check,outputLines.get(3));
    }

    /* * *
     * Test 6
     * * */
    @Test
    void Execute_ValidScriptMultipleValidLogFilesMergeByLogFile_ReturnTrue()
    {
        List<String> logFileNames = new ArrayList<>(Arrays.asList("log_file_1","log_file_4","log_file_3"));
        List<String> logFileLocs = getFilePathList(logFileNames);
        List<String> scriptFileNames = new ArrayList<>(Arrays.asList("mock_scripts/mock_script_single_search_instruction.txt"));
        List<String> scriptFileLocs = getFilePathList(scriptFileNames);

        BatchScriptController controller = new BatchScriptController(configuration,"log",logFileLocs,scriptFileLocs,outputName);
        assertTrue(controller.execute());

        // this combination of input should generate 1 output file
        // the provided output location is 'outputFile.txt'
        // LogData should generate a file with this name

        output = new File(outputName);
        // if this file exists, BatchScriptController must have executed correctly a did not exit before completion
        assertTrue(output.exists());
        assertTrue(output.isDirectory());

        // check first output
        List<String> outputLines = getOutput(outputName+"/merged_log_data_mock_script_single_search_instruction");
        // verify output file is not empty
        assertFalse(outputLines.isEmpty());
        assertEquals(4,outputLines.size());
        // verify output is expected - the entries should be in chronological order
        assertEquals(outputLogFile1_PreLaunch_Check,outputLines.get(0));
        assertEquals(outputLogFile4_PreLaunch_Check,outputLines.get(1));
        assertEquals(outputLogFile3_PreLaunch_Check_1,outputLines.get(2));
        assertEquals(outputLogFile3_PreLaunch_Check_2,outputLines.get(3));
    }

    /* * *
     * Test 7
     * * */
    @Test
    void Execute_ValidScriptValidLogFileMergeByLogFile_ReturnTrue()
    {
        List<String> logFileNames = new ArrayList<>(Arrays.asList("log_file_1"));
        List<String> logFileLocs = getFilePathList(logFileNames);
        List<String> scriptFileNames = new ArrayList<>(Arrays.asList("mock_scripts/mock_script_single_search_instruction.txt"));
        List<String> scriptFileLocs = getFilePathList(scriptFileNames);

        BatchScriptController controller = new BatchScriptController(configuration,"log",logFileLocs,scriptFileLocs,outputName);
        assertTrue(controller.execute());

        // this combination of input should generate 1 output file
        // the provided output location is 'outputFile.txt'
        // LogData should generate a file with this name

        output = new File(outputName);
        // if this file exists, BatchScriptController must have executed correctly a did not exit before completion
        assertTrue(output.exists());
        assertTrue(output.isDirectory());

        // check output
        List<String> outputLines = getOutput(outputName+"/merged_log_data_mock_script_single_search_instruction");
        // verify output file is not empty
        assertFalse(outputLines.isEmpty());
        assertEquals(1,outputLines.size());
        // verify output is expected - the entries should be in chronological order
        assertEquals(outputLogFile1_PreLaunch_Check,outputLines.get(0));
    }

    /* * *
     * Test 8
     * * */
    @Test
    void Execute_MultipleScriptsOneInvalidScriptValidLogFile_ReturnTrue()
    {
        List<String> logFileNames = new ArrayList<>(Arrays.asList("log_file_1"));
        List<String> logFileLocs = getFilePathList(logFileNames);
        List<String> scriptFileNames = new ArrayList<>(Arrays.asList("mock_scripts/mock_script_single_search_instruction.txt","mock_scripts/mock_script_single_search_instruction_2.txt"));
        List<String> scriptFileLocs = getFilePathList(scriptFileNames);
        scriptFileLocs.add("invalid");

        BatchScriptController controller = new BatchScriptController(configuration,"",logFileLocs,scriptFileLocs,outputName);
        assertTrue(controller.execute());

        // this combination of input should generate 2 output files
        // the provided output location is stored in global outputName
        // BatchScriptController should generate a directory with this name
        // within the directory there will be output files named as logfilename_scriptname.txt

        output = new File(outputName);
        // if this file exists, BatchScriptController must have executed correctly a did not exit before completion
        assertTrue(output.exists());
        assertTrue(output.isDirectory());

        // check first output
        List<String> outputLines = getOutput(outputName+"/log_file_1_mock_script_single_search_instruction");
        // verify output file is not empty
        assertFalse(outputLines.isEmpty());
        assertEquals(1,outputLines.size());
        // verify output is expected
        assertEquals(outputLogFile1_PreLaunch_Check,outputLines.get(0));

        // check second output
        outputLines = getOutput(outputName+"/log_file_1_mock_script_single_search_instruction_2");
        // verify output file is not empty
        assertFalse(outputLines.isEmpty());
        assertEquals(1,outputLines.size());
        // verify output is expected
        assertEquals(outputLogFile1_Communication_Check,outputLines.get(0));
    }

    /* * *
     * Test 9
     * * */
    @Test
    void Execute_ValidScriptMultipleValidLogFilesInvalidLogFile_ReturnTrue()
    {
        List<String> logFileNames = new ArrayList<>(Arrays.asList("log_file_1","log_file_4"));
        List<String> logFileLocs = getFilePathList(logFileNames);
        logFileLocs.add("invalid");
        List<String> scriptFileNames = new ArrayList<>(Arrays.asList("mock_scripts/mock_script_single_search_instruction.txt"));
        List<String> scriptFileLocs = getFilePathList(scriptFileNames);

        BatchScriptController controller = new BatchScriptController(configuration,"",logFileLocs,scriptFileLocs,outputName);
        assertTrue(controller.execute());

        // this combination of input should generate 2 output files
        // the provided output location is stored in the global outputName
        // BatchScriptController should generate a directory with this name
        // within the directory there will be output files named as logfilename_scriptname.txt

        output = new File(outputName);
        // if this file exists, BatchScriptController must have executed correctly a did not exit before completion
        assertTrue(output.exists());
        assertTrue(output.isDirectory());

        // check first output
        List<String> outputLines = getOutput(outputName+"/log_file_1_mock_script_single_search_instruction");
        // verify output file is not empty
        assertFalse(outputLines.isEmpty());
        assertEquals(1,outputLines.size());
        // verify output is expected
        assertEquals(outputLogFile1_PreLaunch_Check,outputLines.get(0));

        // check second output
        outputLines = getOutput(outputName+"/log_file_4_mock_script_single_search_instruction");
        // verify output file is not empty
        assertFalse(outputLines.isEmpty());
        assertEquals(1,outputLines.size());
        // verify output is expected
        assertEquals(outputLogFile4_PreLaunch_Check,outputLines.get(0));
    }

    /* * *
     * Test 10
     * * */
    @Test
    void Execute_ValidScriptMultipleValidLogFilesInvalidMerge_ReturnFalse()
    {
        List<String> logFileNames = new ArrayList<>(Arrays.asList("log_file_1","log_file_4","log_file_3"));
        List<String> logFileLocs = getFilePathList(logFileNames);
        List<String> scriptFileNames = new ArrayList<>(Arrays.asList("mock_scripts/mock_script_single_search_instruction.txt"));
        List<String> scriptFileLocs = getFilePathList(scriptFileNames);

        BatchScriptController controller = new BatchScriptController(configuration,"invalid",logFileLocs,scriptFileLocs,outputName);
        assertFalse(controller.execute());
    }

    /* * *
     * Test 11
     * * */
    @Test
    void Execute_ValidScriptInvalidLogFilesNoMerge_ReturnFalse()
    {
        List<String> logFileLocs = new ArrayList<>(Arrays.asList("invalid1","invalid2","invalid3"));
        List<String> scriptFileNames = new ArrayList<>(Arrays.asList("mock_scripts/mock_script_single_search_instruction.txt"));
        List<String> scriptFileLocs = getFilePathList(scriptFileNames);

        BatchScriptController controller = new BatchScriptController(configuration,"",logFileLocs,scriptFileLocs,outputName);
        assertFalse(controller.execute());
    }

    /* * *
     * Test 12
     * * */
    @Test
    void Execute_InvalidScriptMultipleValidLogFilesNoMerge_ReturnFalse()
    {
        List<String> logFileNames = new ArrayList<>(Arrays.asList("log_file_1","log_file_4","log_file_3"));
        List<String> logFileLocs = getFilePathList(logFileNames);
        List<String> scriptFileLocs = new ArrayList<>(Arrays.asList("invalid"));

        BatchScriptController controller = new BatchScriptController(configuration,"",logFileLocs,scriptFileLocs,outputName);
        assertFalse(controller.execute());
    }

    /* * *
     * Test 13
     * * */
    @Test
    void Execute_InvalidScriptInvalidLogFilesNoMerge_ReturnFalse()
    {
        List<String> logFileLocs = new ArrayList<>(Arrays.asList("invalid1","invalid2","invalid3"));
        List<String> scriptFileLocs = new ArrayList<>(Arrays.asList("invalid"));

        BatchScriptController controller = new BatchScriptController(configuration,"",logFileLocs,scriptFileLocs,outputName);
        assertFalse(controller.execute());
    }

    /* * *
     * Test 14
     * * */
    @Test
    void Execute_ValidScriptMultipleValidLogFilesInvalidOutputLocationNoMerge_ReturnTrue()
    {
        List<String> logFileNames = new ArrayList<>(Arrays.asList("log_file_1","log_file_4","log_file_3"));
        List<String> logFileLocs = getFilePathList(logFileNames);
        List<String> scriptFileNames = new ArrayList<>(Arrays.asList("mock_scripts/mock_script_single_search_instruction.txt"));
        List<String> scriptFileLocs = getFilePathList(scriptFileNames);

        // make a directory with the output file name so that output writing will fail
        output = new File(outputName);
        if (!output.mkdirs())
        {
            fail("Error creating test directory.");
        }

        // this failure will happen in LogData
        BatchScriptController controller = new BatchScriptController(configuration,"",logFileLocs,scriptFileLocs,outputName);
        assertFalse(controller.execute());

        // this failure will happen in BatchScriptController
        BatchScriptController controller2 = new BatchScriptController(configuration,"",logFileLocs,scriptFileLocs,"invalid/"+outputName);
        assertFalse(controller2.execute());
    }

    List<String> getFilePathList(List<String> fileNames)
    {
        List<String> filePaths = new ArrayList<>();
        for (String file : fileNames)
        {
            URL logFileLoc = getClass().getClassLoader().getResource(file);

            if (null == logFileLoc)
            {
                fail("Test resource \""+ file +"\" was not found.");
            }
            filePaths.add(logFileLoc.getPath());
        }
        return filePaths;
    }

    // helper function to read data from output files
    List<String> getOutput(String fileLoc)
    {
        File outputFile = new File(fileLoc);
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
        return outputLines;
    }

}
