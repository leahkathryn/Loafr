package com;

import com.input.Configuration;
import com.control.ControllerFactory;
import com.control.Controller;

import java.net.URL;

/**
 * Loafr execution begins in this class.
 *      First, the configuration file is parsed into a Configuration.
 *      The Controller is created in the ControllerFactory based on the
 *      command line arguments. The Controller is executed, and control flow
 *      of Loafr continues in the Controller.
 *
 * @author Leah Lehmeier
 */
public class Loafr
{
    private static String resourceName = "sample_config_file.xml";
    private static ClassLoader classLoader = Loafr.class.getClassLoader();
    private static URL configurationFileLoc = classLoader.getResource(resourceName);

    public static void main(String[] args)
    {
        ControllerFactory controllerFactory = new ControllerFactory();
        Configuration configuration = new Configuration();
        if (false == configuration.parseConfigFile(configurationFileLoc))
        {
            // ErrorHandler already sent message
            System.exit(0);
        }
        Controller controller;
        if (null == (controller = controllerFactory.getController(args,configuration)))
        {
            // ErrorHandler already sent message
            System.exit(0);
        }
        ErrorHandler.setController(controller);
        controller.execute();
    }
}
