package com.control;

import com.ErrorHandler;
import com.input.Configuration;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class ControllerFactory
{
    private List<String> arguments;
    private String outputLoc;

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

        switch(firstArg)
        {
            case BATCH:
                arguments.remove(0);
                parseOutputLoc(configuration);
                return parseBatchArguments(configuration);
            default:
                parseOutputLoc(configuration);
                return parseSimpleScriptArguments(configuration);
        }
    }

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

    private Controller parseBatchArguments(Configuration configuration)
    {
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

    // because there are three potential inputs to the program, and one is optional, I think that flags should be required
    private Controller parseSimpleScriptArguments(Configuration configuration)
    {
        // check that the input is the correct size to contain required arguments
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

        if (logLoc.isBlank() || scriptLoc.isBlank())
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
