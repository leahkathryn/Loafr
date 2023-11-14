package com;

import com.input.Configuration;
import com.control.ControllerFactory;
import com.control.Controller;

import java.nio.file.Path;

public class Loafr
{
    private static String configurationFileLoc = "";

    public static void main(String[] args)
    {
        ControllerFactory controllerFactory = new ControllerFactory();
        Configuration configuration = new Configuration(configurationFileLoc);
        //configuration.parseConfigFile();
        Controller controller = controllerFactory.getController(args,configuration);
    }
}
