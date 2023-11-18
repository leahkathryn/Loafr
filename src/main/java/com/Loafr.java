package com;

import com.input.Configuration;
import com.control.ControllerFactory;
import com.control.Controller;

import java.net.URL;
import java.io.File;

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
    private static String configurationFileLoc = "C:\\Users\\leahk\\OneDrive\\Documents\\GitHub\\Loafr\\src\\test\\resources\\sample_config_file.xml";

    public static void main(String[] args)
    {
        ControllerFactory controllerFactory = new ControllerFactory();
        Configuration configuration = new Configuration();
        configuration.parseConfigFile(configurationFileLoc);
        Controller controller = controllerFactory.getController(args,configuration);
        ErrorHandler.setController(controller);
        controller.execute();
    }
}
