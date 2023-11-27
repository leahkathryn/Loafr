package com.control;

import com.input.Configuration;

import java.net.URL;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * This test class tests the public method getController of the ControllerFactory class.
 * Test cases:
 *      Test 1: Argument list is empty, return null
 *      Test 2: First argument is not a defined flag, return null
 *      Test 3: Provide output location, the provided location is set as the returned Controller's output location
 *      Test 4: Do not provide output location, Configuration default is set as the returned Controller's output location
 *      Test 5: The first argument is "-b", a BatchScriptController is returned
 *      Test 6: The first argument is any accepted flag other than -b, a SimpleScriptController is returned
 *
 *      The following test cases describe conditions when batch processing is not requested:
 *      Test 7: The argument list has less than 4 arguments, return null
 *      Test 8: The argument list has more than 4 arguments, return null
 *      Test 9: The argument list does not contain the "-l" flag, return null
 *      Test 10: The argument list does not contain the "-s" flag, return null
 *      Test 11: The argument list contains the "-s" flag but no script file location, return null
 *      Test 12: The argument list contains the "-l" flag but no log file location, return null
 *
 *      The following test cases describe conditions when batch processing is requested:
 *      Test 13: The argument list does not contain the "-l" flag, return null
 *      Test 14: The argument list does not contain the "-s" flag, return null
 *      Test 15: The argument list contains the "-s" flag but no script file location, return null
 *      Test 16: The argument list contains the "-l" flag but no log file location, return null
 *      Test 17: The argument list contains the "-m" flag but no merge instruction, return null
 *
 * @author Leah Lehmeier
 */
public class ControllerFactoryTest
{
    static Configuration configuration;
    static ControllerFactory factory;

    @BeforeAll
    static void beforeAll()
    {
        URL configurationFileLoc = ControllerFactoryTest.class.getClassLoader().getResource("sample_config_file.xml");
        if (null == configurationFileLoc)
        {
            fail("Test resource \"sample_config_file.xml\" was not found.");
        }
        configuration = new Configuration();
        if (Boolean.FALSE == configuration.parseConfigFile(configurationFileLoc))
        {
            fail("Configuration file parsing failure.");
        }
        factory = new ControllerFactory();
    }

    /* * *
     * Test 1
     * * */
    @Test
    void GetController_ArgumentListIsEmpty_ReturnNull()
    {
        Controller controller = factory.getController(new String[]{""},configuration);
        assertNull(controller);
    }

    /* * *
     * Test 2
     * * */
    @Test
    void GetController_FirstArgumentIsUndefinedFlag_ReturnNull()
    {
        // undefined flag with leading "-"
        Controller controller = factory.getController(new String[]{"-undefinedFlag"},configuration);
        assertNull(controller);

        // undefined argument type
        controller = factory.getController(new String[]{"undefinedFlag"},configuration);
        assertNull(controller);

        // single "-"
        controller = factory.getController(new String[]{"-"},configuration);
        assertNull(controller);

        // undefined flag with leading "-" and other valid arguments
        controller = factory.getController(new String[]{"-undefinedFlag","-s","scriptLocation","-l","logLocation"},configuration);
        assertNull(controller);

        // single "-" and other valid arguments
        controller = factory.getController(new String[]{"-","-s","scriptLocation","-l","logLocation"},configuration);
        assertNull(controller);
    }

    /* * *
     * Test 3
     * * */
    @Test
    void GetController_ArgumentListContainsOutputLocation_ControllerHasOutputLocation()
    {
        Controller controller = factory.getController(new String[]{"-s","scriptLoc","-l","logLoc","-o","outputLoc"},configuration);
        assertNotNull(controller);
        assertInstanceOf(SimpleScriptController.class,controller);
        assertEquals("outputLoc",controller.getOutputFileLoc());
    }

    /* * *
     * Test 4
     * * */
    @Test
    void GetController_ArgumentListDoesNotContainOutputLocation_ControllerHasDefaultOutputLocation()
    {
        Controller controller = factory.getController(new String[]{"-s","scriptLoc","-l","logLoc"},configuration);
        assertNotNull(controller);
        assertInstanceOf(SimpleScriptController.class,controller);
        assertEquals(configuration.getDefaultOutputLocation(),controller.getOutputFileLoc());
    }

    /* * *
     * Test 5
     * * */
    @Test
    void GetController_FirstArgumentIsBatchFlagValidArguments_ReturnBatchScriptController()
    {
        Controller controller = factory.getController(new String[]{"-b","-s","scriptLoc1","-l","logLoc1","logLoc2","logLoc3"},configuration);
        assertNotNull(controller);
        assertInstanceOf(BatchScriptController.class,controller);
    }

    /* * *
     * Test 6
     * * */
    @Test
    void GetController_FirstArgumentIsDefinedFlagValidArguments_ReturnSimpleScriptController()
    {
        Controller controller = factory.getController(new String[]{"-s","scriptLoc","-l","logLoc"},configuration);
        assertNotNull(controller);
        assertInstanceOf(SimpleScriptController.class,controller);
        // verify Controller fields are initialized with correct information
        assertEquals(configuration.getDefaultOutputLocation(),controller.getOutputFileLoc());
        assertEquals("scriptLoc",((SimpleScriptController)controller).getScriptFileLoc());
        assertEquals("logLoc",((SimpleScriptController)controller).getLogFileLoc());
    }

    /* * *
     * Test 7
     * * */
    @Test
    void GetController_ParseSimpleScriptArguments_LessThanFourInArgumentList_ReturnNull()
    {
        Controller controller = factory.getController(new String[]{"-s","scriptLoc","logLoc"},configuration);
        assertNull(controller);
    }

    /* * *
     * Test 8
     * * */
    @Test
    void GetController_ParseSimpleScriptArguments_MoreThanFourInArgumentList_ReturnNull()
    {
        Controller controller = factory.getController(new String[]{"-s","scriptLoc","-l","logLoc","extra"},configuration);
        assertNull(controller);
    }

    /* * *
     * Test 9
     * * */
    @Test
    void GetController_ParseSimpleScriptArguments_ArgumentListDoesNotContainLogFlag_ReturnNull()
    {
        Controller controller = factory.getController(new String[]{"-s","scriptLoc","-s","logLoc"},configuration);
        assertNull(controller);
    }

    /* * *
     * Test 10
     * * */
    @Test
    void GetController_ParseSimpleScriptArguments_ArgumentListDoesNotContainScriptFlag_ReturnNull()
    {
        Controller controller = factory.getController(new String[]{"-l","scriptLoc","-l","logLoc"},configuration);
        assertNull(controller);
    }

    /* * *
     * Test 11
     * * */
    @Test
    void GetController_ParseSimpleScriptArguments_ArgumentListDoesNotContainScriptLoc_ReturnNull()
    {
        // skip straight to next flag
        Controller controller = factory.getController(new String[]{"-s","-l","logLoc","extra"},configuration);
        assertNull(controller);
        // script flag is last argument in list
        controller = factory.getController(new String[]{"extra","-l","logLoc","-s"},configuration);
        assertNull(controller);
    }

    /* * *
     * Test 12
     * * */
    @Test
    void GetController_ParseSimpleScriptArguments_ArgumentListDoesNotContainLogLoc_ReturnNull()
    {
        // skip straight to next flag
        Controller controller = factory.getController(new String[]{"-l","-s","scriptLoc","extra"},configuration);
        assertNull(controller);
        // script flag is last argument in list
        controller = factory.getController(new String[]{"extra","-s","scriptLoc","-l"},configuration);
        assertNull(controller);
    }

    /* * *
     * Test 13
     * * */
    @Test
    void GetController_ParseBatchArguments_ArgumentListDoesNotContainLogFlag_ReturnNull()
    {
        Controller controller = factory.getController(new String[]{"-b","-s","scriptLoc1","scriptLoc2"},configuration);
        assertNull(controller);
    }

    /* * *
     * Test 14
     * * */
    @Test
    void GetController_ParseBatchArguments_ArgumentListDoesNotContainScriptFlag_ReturnNull()
    {
        Controller controller = factory.getController(new String[]{"-b","-l","logLoc1","logLoc2"},configuration);
        assertNull(controller);
    }

    /* * *
     * Test 15
     * * */
    @Test
    void GetController_ParseBatchArguments_ArgumentListDoesNotContainScriptLoc_ReturnNull()
    {
        // skip straight to next flag
        Controller controller = factory.getController(new String[]{"-b","-s","-l","logLoc1","logLoc2","logLoc3","-m","event"},configuration);
        assertNull(controller);
        // script flag is last argument in list
        controller = factory.getController(new String[]{"-b","-m","event","-l","logLoc1","logLoc2","logLoc3","-s"},configuration);
        assertNull(controller);
    }

    /* * *
     * Test 16
     * * */
    @Test
    void GetController_ParseBatchArguments_ArgumentListDoesNotContainLogLoc_ReturnNull()
    {
        // skip straight to next flag
        Controller controller = factory.getController(new String[]{"-b","-l","-s","scriptLoc1","scriptLoc2","scriptLoc3"},configuration);
        assertNull(controller);
        // script flag is last argument in list
        controller = factory.getController(new String[]{"-b","-s","scriptLoc1","scriptLoc2","scriptLoc3","-l"},configuration);
        assertNull(controller);
    }

    /* * *
     * Test 17
     * * */
    @Test
    void GetController_ParseBatchArguments_ArgumentListDoesNotContainMergeInstruction_ReturnNull()
    {
        // skip straight to next flag
        Controller controller = factory.getController(new String[]{"-b","-l","logLoc1","logLoc2","-m","-s","scriptLoc1"},configuration);
        assertNull(controller);
        // script flag is last argument in list
        controller = factory.getController(new String[]{"-b","-l","logLoc1","logLoc2","-s","scriptLoc1","-m"},configuration);
        assertNull(controller);
    }
}
