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

        if (arguments.isEmpty() || (null == (firstArg = Flag.fromString(arguments.get(0)))))
        {
            ErrorHandler.logError("The argument list contains a syntax error.\n" +
                    "Usage: <-l> <log file path> <-s> <script file path> optional: <-o> <output file path>\n" +
                    "Alt for batch processing: <-b> <-l> <log file list> <-s> <script file list> optional: <-o> <output file path> <-m> <event | log>\n");
            return null;
        }

        // parse the output location from the argument list, if it exists there
        parseOutputLoc(configuration);
        // check for failure to parse output location
        if (null == outputLoc)
        {
            ErrorHandler.logError("The argument list contains a syntax error.\n" +
                    "Usage: <-l> <log file path> <-s> <script file path> optional: <-o> <output file path>\n");
            return null;
        }

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
            int index = arguments.indexOf(Flag.OUTPUTLOC.toString())+1;
            if (arguments.size() == index || arguments.get(index).startsWith("-"))
            {
                ErrorHandler.logError("The argument list contains a syntax error.\n" +
                        "Usage: <-b> <-l> <log file list> <-s> <script file list> optional: <-o> <output file path> <-m> <event | log>\n");
                outputLoc = null;
            }
            else
            {
                outputLoc = arguments.remove(index);
                arguments.remove(Flag.OUTPUTLOC.toString());
            }

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
        // remove the "-b" flag
        arguments.remove(0);
        // merging strategy specifier
        String merge = "";
        List<String> logLocList = new ArrayList<>();
        List<String> scriptLocList = new ArrayList<>();

        if (arguments.contains(Flag.LOGLOC.toString()))
        {
            int index = arguments.indexOf(Flag.LOGLOC.toString())+1;
            // add file locations to the list until the next flag is encountered or the argument list ends
            while (index < arguments.size() && !arguments.get(index).startsWith("-"))
            {
                logLocList.add(arguments.get(index));
                index++;
            }
        }

        if (arguments.contains(Flag.SCRIPTLOC.toString()))
        {
            int index = arguments.indexOf(Flag.SCRIPTLOC.toString())+1;
            // add file locations to the list until the next flag is encountered or the argument list ends
            while (index < arguments.size() && !arguments.get(index).startsWith("-"))
            {
                scriptLocList.add(arguments.get(index));
                index++;
            }
        }

        if (arguments.contains(Flag.MERGE.toString()))
        {
            int index = arguments.indexOf(Flag.MERGE.toString())+1;
            // if the "-m" flag is the last argument in the list, or if the following argument is another flag, fail
            if (arguments.size() == index || arguments.get(index).startsWith("-"))
            {
                ErrorHandler.logError("The argument list contains a syntax error.\n" +
                        "Usage: <-b> <-l> <log file list> <-s> <script file list> optional: <-o> <output file path> <-m> <event | log>\n");
                return null;
            }
            else
            {
                merge = arguments.get(index);
            }
        }

        if (logLocList.isEmpty() || scriptLocList.isEmpty())
        {
            ErrorHandler.logError("The argument list contains a syntax error.\n" +
                    "Usage: <-b> <-l> <log file list> <-s> <script file list> optional: <-o> <output file path> <-m> <event | log>\n");
            return null;
        }

        return new BatchScriptController(configuration,merge,logLocList,scriptLocList,outputLoc);
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
        // check that the com.input is the correct size to contain required arguments
        if (arguments.size() < 4)
        {
            ErrorHandler.logError("The argument list is too short.\n" +
                    "Usage: <-l> <log file path> <-s> <script file path> optional: <-o> <output file path>\n");
            return null;
        }
        else if (arguments.size() > 4)
        {
            ErrorHandler.logError("The argument list is too long.\n" +
                    "Usage: <-l> <log file path> <-s> <script file path> optional: <-o> <output file path>\n");
            return null;
        }

        String logLoc = "";
        String scriptLoc = "";

        if (arguments.contains(Flag.LOGLOC.toString()))
        {
            int index = arguments.indexOf(Flag.LOGLOC.toString())+1;
            // if the "-l" flag is the last argument in the list, or if the following argument is another flag, fail
            if (arguments.size() == index || arguments.get(index).startsWith("-"))
            {
                ErrorHandler.logError("The argument list contains a syntax error.\n" +
                        "Usage: <-l> <log file list> <-s> <script file list> optional: <-o> <output file path> <-m> <event | log>\n");
                return null;
            }
            logLoc = arguments.get(index);
        }

        if (arguments.contains(Flag.SCRIPTLOC.toString()))
        {
            int index = arguments.indexOf(Flag.SCRIPTLOC.toString())+1;
            // if the "-l" flag is the last argument in the list, or if the following argument is another flag, fail
            if (arguments.size() == index || arguments.get(index).startsWith("-"))
            {
                ErrorHandler.logError("The argument list contains a syntax error.\n" +
                        "Usage: <-l> <log file list> <-s> <script file list> optional: <-o> <output file path> <-m> <event | log>\n");
                return null;
            }
            scriptLoc = arguments.get(index);
        }

        if (logLoc.isBlank() || scriptLoc.isBlank())
        {
            ErrorHandler.logError("The argument list contains a syntax error.\n" +
                    "Usage: <-l> <log file path> <-s> <script file path> optional: <-o> <output file path>");
            return null;
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

        public static Flag fromString(String str)
        {
            for (Flag f : values()) {
                if (f.flag.equals(str)) {
                    return f;
                }
            }
            return null;
        }
    }
}
