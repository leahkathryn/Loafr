package com.input;


import org.junit.jupiter.api.Test;

import static com.input.DataType.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Notes: in this class, we suppose the configuration is absolutely correct.
 * Because that kind of error were taking care by another class, which is configuration test.
 * Please check there for more details.
 */
public class LogDataTest
{

    private Configuration config = new Configuration();
    private List<String> inputData = new ArrayList<>();
    static List<DataID> testDataIDList = new ArrayList<>();
    private Event event;




    // test case naming convention: MethodName_StateUnderTest_ExpectedBehavior
    @Test
    void MethodName_StateUnderTest_ExpectedBehavior() {
        System.out.println("**--- Test method1 executed ---**");
    }


    /**
     * Below are tests I completed. Test0 - 9 are test about input (current has 7 tests),
     * Test10 - 19 are test about output (current has 3 tests).
     * More tests are subjected to be added here.
     */
    @Test
    void test0() {
        URL configurationFileLoc = getClass().getClassLoader().getResource("sample_config_file.xml");
        this.inputData = new ArrayList<>(Arrays.asList("true", "0"));
        this.event = new Event("System_Initialization", Arrays.asList(new DataID("Status", BOOLEAN),
                new DataID("Error_Code", INTEGER)));
        if (configurationFileLoc == null) {
            fail("Test resource \"sample_config_file.xml\" was not found.");
        }
        boolean isParsed = config.parseConfigFile(configurationFileLoc);
        assertEquals(true, isParsed);
        System.out.println("**--- Test test0 executed ---**");
    }


    /**
     * Test if the logfile is not valid, Loafr will not crash.
     */
    @Test
    void Test0_invalid_logData_position(){
        URL configurationFileLoc = getClass().getClassLoader().getResource("sample_config_file.xml");
        Configuration configuration = new Configuration();
        configuration.parseConfigFile(configurationFileLoc);
        String someRandomString = "good_morning_everybody_this_is_meaningless";
        LogData logData = new LogData();
        logData.parseLogFile(someRandomString,configuration);
        System.out.println("**--- Test0_invalid_logData_position executed ---**");
    }


    /**
     * Test a valid logfile and a valid configuration.
     */
    @Test
    void Test1_parse_log_file_regular(){
        URL configurationFileLoc = getClass().getClassLoader().getResource("sample_config_file.xml");
        String path1 = "./src/test/resources/log_file_1";
        Configuration configuration = new Configuration();
        configuration.parseConfigFile(configurationFileLoc);
        LogData logData = new LogData();
        boolean parsingStatus = logData.parseLogFile(path1, configuration);
        assertNotNull(logData);
        assertEquals(10,logData.getEventList().size());
        assertTrue(parsingStatus);
        assertFalse(logData.getEventList().isEmpty());
        System.out.println("**--- Test1_parse_log_file_regular executed ---**");
    }


    /**
     * Test a valid logfile and a valid configuration.
     */
    @Test
    void Test2_configuration_not_correct(){
        String path1 = "./src/test/resources/log_file_1";
        LogData logData = new LogData();
        boolean parsingStatus = logData.parseLogFile(path1, new Configuration());
        assertNotNull(logData);
        assertEquals(0,logData.getEventList().size());
        assertFalse(parsingStatus);
        assertTrue(logData.getEventList().isEmpty());
        System.out.println("**--- Test2_configuration_not_correct executed ---**");
    }


    /**
     * Test a valid logfile and a valid configuration.
     * This case contains 1000 LogEvents.
     * @throws IOException if reader/writer does not work properly.
     */
    @Test
    void Test3_parse_log_file_too_long() throws IOException {
        URL configurationFileLoc = getClass().getClassLoader().getResource("sample_config_file.xml");
        String sourceFilePath = "./src/test/resources/log_file_1";
        String destinationFilePath = "./src/test/resources/copied_log_file.txt";

        File sourceFile = new File(sourceFilePath);
        File destinationFile = new File(destinationFilePath);

        BufferedReader reader = new BufferedReader(new FileReader(sourceFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(destinationFile));

        for (int i = 0; i < 100; i++) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line + "\n");
            }
            reader.close(); // Close and reopen the reader to reset it to the beginning of the file
            reader = new BufferedReader(new FileReader(sourceFile));
        }

        reader.close();
        writer.close();

        String path1 = destinationFilePath;
        Configuration configuration = new Configuration();
        configuration.parseConfigFile(configurationFileLoc);
        LogData logData = new LogData();
        boolean parsingStatus = logData.parseLogFile(path1, configuration);
        assertNotNull(logData);
        assertEquals(1000,logData.getEventList().size());
        assertTrue(parsingStatus);
        assertFalse(logData.getEventList().isEmpty());
        System.out.println("**--- Test3_parse_log_file_too_long executed ---**");
    }


    /**
     * Test a valid logfile and a valid configuration.
     * But the logfile contains strange information, e.g. year before 1970 even B.C.,
     * month 13, day 32, etc.
     */
    @Test
    void Test4_strange_dates(){
        URL configurationFileLoc = getClass().getClassLoader().getResource("sample_config_file.xml");
        String path1 = "./src/test/resources/log_file_before_1970";
        Configuration configuration = new Configuration();
        configuration.parseConfigFile(configurationFileLoc);
        LogData logData = new LogData();
        boolean parsingStatus = logData.parseLogFile(path1, configuration);
        assertNotNull(logData);
        assertEquals(10,logData.getEventList().size());
        assertTrue(parsingStatus);
        assertFalse(logData.getEventList().isEmpty());
        System.out.println("**--- Test4_year_strange executed ---**");
    }



    /**
     * Test a valid logfile and a valid configuration.
     * @throws IOException if reader/writer does not work properly.
     */
    @Test
    void Test5_parse_log_file_no_comma() throws IOException {
        URL configurationFileLoc = getClass().getClassLoader().getResource("sample_config_file.xml");
        String path1 = "./src/test/resources/log_file_1";
        String destinationFilePath = "./src/test/resources/no_comma_log_file.txt";
        File sourceFile = new File(path1);
        File destinationFile = new File(destinationFilePath);

        BufferedReader reader = new BufferedReader(new FileReader(sourceFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(destinationFile));

        String line;
        while ((line = reader.readLine()) != null) {
            // Remove commas from the line and write to the new file
            String lineWithoutCommas = line.replaceAll(",", "");
            writer.write(lineWithoutCommas + "\n");
        }

        reader.close();
        writer.close();


        Configuration configuration = new Configuration();
        configuration.parseConfigFile(configurationFileLoc);
        LogData logData = new LogData();
        boolean parsingStatus = logData.parseLogFile(destinationFilePath, configuration);
        assertNotNull(logData);
        assertEquals(0,logData.getEventList().size());
        assertTrue(parsingStatus);
        assertTrue(logData.getEventList().isEmpty());
        System.out.println("**--- Test1_parse_log_file_regular executed ---**");
    }


    /**
     * Test if the logfile is not valid, for example, no valid time stamp.
     * In this case, there are too much comma.
     * @throws IOException if reader/writer does not work properly.
     */
    @Test
    void Test6_parse_log_file_too_much_comma() throws IOException {
        URL configurationFileLoc = getClass().getClassLoader().getResource("sample_config_file.xml");
        String path1 = "./src/test/resources/log_file_1";
        String destinationFilePath = "./src/test/resources/too_much_comma_log_file.txt";
        File sourceFile = new File(path1);
        File destinationFile = new File(destinationFilePath);

        BufferedReader reader = new BufferedReader(new FileReader(sourceFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(destinationFile));

        String line;
        while ((line = reader.readLine()) != null) {
            // Remove commas from the line and write to the new file
            String lineWithoutCommas = line.replaceAll(".", ",");
            writer.write(lineWithoutCommas + "\n");
        }

        reader.close();
        writer.close();


        Configuration configuration = new Configuration();
        configuration.parseConfigFile(configurationFileLoc);
        LogData logData = new LogData();
        boolean parsingStatus = logData.parseLogFile(destinationFilePath, configuration);
        assertNotNull(logData);
        assertEquals(0,logData.getEventList().size());
        assertTrue(parsingStatus);
        assertTrue(logData.getEventList().isEmpty());
        System.out.println("**--- Test6_parse_log_file_too_much_comma executed ---**");
    }


    /**
     * This is a regular test if the destination file has been successfully written.
     */
    @Test
    void Test10_test_valid_write_logData(){
        String destinationFilePath = "./src/test/resources/log_file_wrote_test10.txt";
        URL configurationFileLoc = getClass().getClassLoader().getResource("sample_config_file.xml");
        Configuration configuration = new Configuration();
        configuration.parseConfigFile(configurationFileLoc);
        LogData logData = new LogData();
        String path1 = "./src/test/resources/log_file_1";
        boolean parsingStatus = logData.parseLogFile(path1, configuration);
        assertNotNull(logData);
        assertEquals(10,logData.getEventList().size());
        assertTrue(parsingStatus);
        assertFalse(logData.getEventList().isEmpty());
        // By this point, this has been successfully read into logData.
        // We need to export it right now.
        boolean writeResult = logData.writeLogData(destinationFilePath, configuration);
        assertNotNull(logData);
        assertTrue(writeResult);
        assertEquals(10,logData.getEventList().size());
        assertFalse(logData.getEventList().isEmpty());
        System.out.println("**--- Test10_test_valid_write_logData executed ---**");
    }


    /**
     * To test if we have a long output, if it can be successfully output.
     */
    @Test
    void Test11_test_valid_write_logData_too_long(){
        String destinationFilePath = "./src/test/resources/log_file_wrote_test11_too_long.txt";
        URL configurationFileLoc = getClass().getClassLoader().getResource("sample_config_file.xml");
        Configuration configuration = new Configuration();
        configuration.parseConfigFile(configurationFileLoc);
        LogData logData = new LogData();
        String path1 = "./src/test/resources/copied_log_file.txt";
        boolean parsingStatus = logData.parseLogFile(path1, configuration);
        assertNotNull(logData);
        assertEquals(1000,logData.getEventList().size());
        assertTrue(parsingStatus);
        assertFalse(logData.getEventList().isEmpty());
        // By this point, this has been successfully read into logData.
        // We need to export it right now.
        boolean writeResult = logData.writeLogData(destinationFilePath, configuration);
        assertNotNull(logData);
        assertTrue(writeResult);
        assertEquals(1000,logData.getEventList().size());
        assertFalse(logData.getEventList().isEmpty());
        System.out.println("**--- Test11_test_valid_write_logData_too_long executed ---**");
    }


    /**
     * This test is if an empty logData is required to output.
     */
    @Test
    void Test12_test_empty_write_logData(){
        String destinationFilePath = "./src/test/resources/log_file_wrote_test11_too_long.txt";
        URL configurationFileLoc = getClass().getClassLoader().getResource("sample_config_file.xml");
        Configuration configuration = new Configuration();
        configuration.parseConfigFile(configurationFileLoc);
        LogData logData = new LogData();
        boolean writeResult = logData.writeLogData(destinationFilePath, configuration);
        assertNotNull(logData);
        assertTrue(writeResult);
        assertEquals(0,logData.getEventList().size());
        assertTrue(logData.getEventList().isEmpty());
        System.out.println("**--- Test12_test_empty_write_logData executed ---**");
    }


    /**
     * Test if the output location is not valid.
     * It should suppose to touch a new directory or file and store the output in it.
     * It shall not crash.
     */
    @Test
    void Test13_test_invalid_output_loc(){
        String destinationFilePath = "ThisIsInvalid";
        URL configurationFileLoc = getClass().getClassLoader().getResource("sample_config_file.xml");
        Configuration configuration = new Configuration();
        configuration.parseConfigFile(configurationFileLoc);
        LogData logData = new LogData();
        String path1 = "./src/test/resources/log_file_1";
        boolean parsingStatus = logData.parseLogFile(path1, configuration);
        assertNotNull(logData);
        assertEquals(10,logData.getEventList().size());
        assertTrue(parsingStatus);
        assertFalse(logData.getEventList().isEmpty());
        // By this point, this has been successfully read into logData.
        // We need to export it right now.
        boolean writeResult = logData.writeLogData(destinationFilePath, configuration);
        assertNotNull(logData);
        assertTrue(writeResult);
        assertEquals(10,logData.getEventList().size());
        assertFalse(logData.getEventList().isEmpty());
        System.out.println("**--- Test13_test_invalid_output_loc executed ---**");
    }
}
