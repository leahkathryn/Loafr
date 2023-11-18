package com.control;

import com.ErrorHandler;
import com.input.Configuration;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Creates a new Controller based on the command line input to Loafr.
 *
 * @author Leah Lehmeier
 */
public class ControllerFactory
{
    private List<String> arguments;
    private String outputLoc;

    /**
     * This function parses Loafr's command line input and instatiates a new
     *      Controller based on that input. If an error occurs,
     *      the ErrorHandler will display the error message
     *      to the user and immediately end Loafr execution.
     *
     * @param args           Loafr command line arguments
     * @param configuration  the configuration file information stored in
     *                       a Configuration class instance.
     * @return               the new Controller
     */
    public Controller getController(String[] args, Configuration configuration)
    {
        arguments = new ArrayList<>(Arrays.asList(args));

        Flag firstArg = null;
        try {
            firstArg = Flag.valueOf(arguments.get(0));
        }
        catch (IllegalArgumentException e)
        {
            ErrorHandler.logError("The argument list contains a syntax error.\n" +
                    "Usage: <-l> <log file path> <-s> <script file path> optional: <-o> <output file path>\n" +
                    "Alt for batch processing: <-b> <-l> <log file list> <-s> <script file list> optional: <-o> <output file path> <-m> <event | log>\n");
        }

        parseOutputLoc(configuration);

        switch(firstArg)
        {
            case BATCH:
                return parseBatchArguments(configuration);
            default:
                return parseSimpleScriptArguments(configuration);
        }
    }

    /**
     * This function checks the input for a user-specified output file
     *      location. This will be the Controller's output location to
     *      write analysis output. If there is no file location, the
     *      default location defined in the configuration file will be
     *      assigned to the Controller.
     *
     * @param configuration  the configuration file information stored in
     *                       a Configuration class instance.
     */
    private void parseOutputLoc(Configuration configuration)
    {
        // check the input for an output file location
        if (arguments.contains(Flag.OUTPUTLOC.toString()))
        {
            outputLoc = arguments.remove(arguments.indexOf(Flag.OUTPUTLOC.toString())+1);
            arguments.remove(Flag.OUTPUTLOC.toString());
        }
        else
        {
            outputLoc = configuration.getDefaultOutputLocation();
        }
    }

    /**
     * If the command line arguments have specified that Loafr should perform
     *      batch processing, this function will parse the remaining command
     *      line arguments and create a new BatchScriptController.
     *
     * @param configuration  the configuration file information stored in
     *                       a Configuration class instance.
     * @return               BatchScriptController
     */
    private Controller parseBatchArguments(Configuration configuration)
    {
        arguments.remove(0);
        String flag = "";
        List<String> logLocList = new ArrayList<>();
        List<String> scriptLocList = new ArrayList<>();

        if (arguments.contains(Flag.LOGLOC.toString()))
        {
            int index = arguments.indexOf(Flag.LOGLOC.toString())+1;
            // add file locations to the list until the next flag is encountered or the argument list ends
            while (!arguments.get(index).startsWith("-") && index < arguments.size())
            {
                logLocList.add(arguments.get(index));
                index++;
            }
        }

        if (arguments.contains(Flag.SCRIPTLOC.toString()))
        {
            int index = arguments.indexOf(Flag.SCRIPTLOC.toString())+1;
            // add file locations to the list until the next flag is encountered or the argument list ends
            while (!arguments.get(index).startsWith("-") && index < arguments.size())
            {
                scriptLocList.add(arguments.get(index));
                index++;
            }
        }

        if (arguments.contains(Flag.MERGE.toString()))
        {

            int index = arguments.indexOf(Flag.MERGE.toString())+1;
            if (arguments.get(index).startsWith("-"))
            {
                ErrorHandler.logError("The argument list contains a syntax error.\n" +
                        "Usage: <-b> <-l> <log file list> <-s> <script file list> optional: <-o> <output file path> <-m> <event | log>\n");
            }
            else
            {
                flag = arguments.get(index);
            }
        }

        if (logLocList.isEmpty() || scriptLocList.isEmpty())
        {
            ErrorHandler.logError("The argument list contains a syntax error.\n" +
                    "Usage: <-b> <-l> <log file list> <-s> <script file list> optional: <-o> <output file path> <-m> <event | log>\n");
        }

        return new BatchScriptController(configuration,flag,logLocList,scriptLocList,outputLoc);
    }

    /**
     * If the command line arguments have not specified that Loafr should perform
     *      a particular type of execution, this function will parse the command
     *      line arguments and create a new SimpleScriptController.
     *
     * @param configuration  the configuration file information stored in
     *                       a Configuration class instance.
     * @return               SimpleScriptController
     */
    private Controller parseSimpleScriptArguments(Configuration configuration)
    {
        arguments.remove(0);
        // check that the com.input is the correct size to contain required arguments
        if (arguments.size() < 4)
        {
            ErrorHandler.logError("The argument list is too short.\n" +
                    "Usage: <-l> <log file path> <-s> <script file path> optional: <-o> <output file path>\n");
        }
        else if (arguments.size() > 4)
        {
            ErrorHandler.logError("The argument list is too long.\n" +
                    "Usage: <-l> <log file path> <-s> <script file path> optional: <-o> <output file path>\n");
        }

        String logLoc = "";
        String scriptLoc = "";

        if (arguments.contains(Flag.LOGLOC.toString()))
        {
            logLoc = arguments.remove(arguments.indexOf(Flag.LOGLOC.toString())+1);
            arguments.remove(Flag.LOGLOC.toString());
        }

        if (arguments.contains(Flag.SCRIPTLOC.toString()))
        {
            scriptLoc = arguments.remove(arguments.indexOf(Flag.SCRIPTLOC.toString())+1);
            arguments.remove(Flag.SCRIPTLOC.toString());
        }

        if (logLoc.isBlank() || scriptLoc.isBlank() || !arguments.isEmpty())
        {
            ErrorHandler.logError("The argument list contains a syntax error.\n" +
                    "Usage: <-l> <log file path> <-s> <script file path> optional: <-o> <output file path>");
        }

        return new SimpleScriptController(configuration,logLoc,scriptLoc,outputLoc);
    }

    private enum Flag
    {
        BATCH("-b"),
        MERGE("-m"),
        LOGLOC("-l"),
        SCRIPTLOC("-s"),
        OUTPUTLOC("-o");

        private String flag;

        Flag(String flag)
        {
            this.flag = flag;
        }

        @Override
        public String toString()
        {
            return flag;
        }
    }
}
