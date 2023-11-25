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
 *      Argument list is empty, return null
 *      First argument is not a recognized flag, return null
 *      Provide output location, the provided location is set as Controller's output location
 *      Do not provide output location, Configuration default is set as Controller's output location
 *      The first argument is "-b", a BatchScriptController is returned
 *      The first argument is any accepted flag other than -b, a SimpleScriptController is returned
 *
 *      The following test cases describe conditions when batch processing is not requested:
 *      The argument list has more than 4 arguments, return null
 *      The argument list has less than 4 arguments, return null
 *      The argument list does not contain the "-l" flag, return null
 *      The argument list does not contain the "-s" flag, return null
 *      The argument list contains the "-l" flag but no log file location, return null
 *      The argument list contains the "-s" flag but no script file location, return null
 *
 *      The following test cases describe conditions when batch processing is requested:
 *      The argument list does not contain the "-l" flag, return null
 *      The argument list does not contain the "-s" flag, return null
 *      The argument list contains the "-l" flag but no log file location, return null
 *      The argument list contains the "-s" flag but no script file location, return null
 *      The argument list contains the "-m" flag but no merge instruction, return null
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

    // test case naming convention: MethodName_StateUnderTest_ExpectedBehavior
    @Test
    void GetController_ArgumentListIsEmpty_ReturnNull()
    {
        Controller controller = factory.getController(new String[]{""},configuration);
        assertNull(controller);
    }

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

    @Test
    void GetController_ArgumentListContainsOutputLocation_ControllerHasOutputLocation()
    {
        Controller controller = factory.getController(new String[]{"-s","scriptLoc","-l","logLoc","-o","outputLoc"},configuration);
        assertNotNull(controller);
        assertInstanceOf(SimpleScriptController.class,controller);
        assertEquals("outputLoc",controller.getOutputFileLoc());
    }

    @Test
    void GetController_ArgumentListDoesNotContainOutputLocation_ControllerHasDefaultOutputLocation()
    {
        Controller controller = factory.getController(new String[]{"-s","scriptLoc","-l","logLoc"},configuration);
        assertNotNull(controller);
        assertInstanceOf(SimpleScriptController.class,controller);
        assertEquals(configuration.getDefaultOutputLocation(),controller.getOutputFileLoc());
    }

    @Test
    void GetController_FirstArgumentIsBatchFlagValidArguments_ReturnBatchScriptController()
    {
        Controller controller = factory.getController(new String[]{"-b","-s","scriptLoc1","-l","logLoc1","logLoc2","logLoc3"},configuration);
        assertNotNull(controller);
        assertInstanceOf(BatchScriptController.class,controller);
    }

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

    @Test
    void GetController_ParseSimpleScriptArguments_LessThanFourInArgumentList_ReturnNull()
    {
        Controller controller = factory.getController(new String[]{"-s","scriptLoc","logLoc"},configuration);
        assertNull(controller);
    }

    @Test
    void GetController_ParseSimpleScriptArguments_MoreThanFourInArgumentList_ReturnNull()
    {
        Controller controller = factory.getController(new String[]{"-s","scriptLoc","-l","logLoc","extra"},configuration);
        assertNull(controller);
    }

    @Test
    void GetController_ParseSimpleScriptArguments_ArgumentListDoesNotContainLogFlag_ReturnNull()
    {
        Controller controller = factory.getController(new String[]{"-s","scriptLoc","-s","logLoc"},configuration);
        assertNull(controller);
    }

    @Test
    void GetController_ParseSimpleScriptArguments_ArgumentListDoesNotContainScriptFlag_ReturnNull()
    {
        Controller controller = factory.getController(new String[]{"-l","scriptLoc","-l","logLoc"},configuration);
        assertNull(controller);
    }

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

    @Test
    void GetController_ParseBatchArguments_ArgumentListDoesNotContainLogFlag_ReturnNull()
    {
        Controller controller = factory.getController(new String[]{"-b","-s","scriptLoc1","scriptLoc2"},configuration);
        assertNull(controller);
    }

    @Test
    void GetController_ParseBatchArguments_ArgumentListDoesNotContainScriptFlag_ReturnNull()
    {
        Controller controller = factory.getController(new String[]{"-b","-l","logLoc1","logLoc2"},configuration);
        assertNull(controller);
    }

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
