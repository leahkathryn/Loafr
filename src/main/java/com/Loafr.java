package com;

import com.input.Configuration;
import com.control.ControllerFactory;
import com.control.Controller;

import java.nio.file.Path;
import java.nio.file.Paths;

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
//    private static Path resourceDirectory = Paths.get("src","test","resources","sample_config_file.xml");
//    private static String configurationFileLoc = resourceDirectory.toFile().getAbsolutePath();
    private static String configurationFileLoc = "C:\\Users\\leahk\\OneDrive\\Documents\\GitHub\\Loafr\\src\\test\\resources\\sample_config_file.xml";

    public static void main(String[] args)
    {
        ControllerFactory controllerFactory = new ControllerFactory();
        Configuration configuration = new Configuration();
        if (false == configuration.parseConfigFile(configurationFileLoc))
        {
            System.exit(0);
        }
        Controller controller = controllerFactory.getController(args,configuration);
        ErrorHandler.setController(controller);
        controller.execute();
    }
}
