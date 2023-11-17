package com;

import com.input.Configuration;
import com.control.ControllerFactory;
import com.control.Controller;

import java.nio.file.Path;

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
    // update this String before testing
    private static String configurationFileLoc = "";

    public static void main(String[] args)
    {
        ControllerFactory controllerFactory = new ControllerFactory();
        Configuration configuration = new Configuration(configurationFileLoc);
        configuration.parseConfigFile();
        Controller controller = controllerFactory.getController(args,configuration);
        ErrorHandler.setController(controller);
        controller.execute();
    }
}
