package com.control;

import com.ErrorHandler;
import com.input.Configuration;
import com.input.LogData;
import com.interpret.Interpreter;
import com.interpret.Script;

/**
 * This implementation of the Controller class will be used when Loafr
 *      is provided a single script and a single log file as input.
 *      The script file will be parsed and interpreted. The log file will be parsed.
 *      The script will be executed on the log file. The output from the script
 *      execution will write itself either to the user-specified file location,
 *      or if none was provided, to the default output file location.
 *
 * @author Leah Lehmeier
 */
public class SimpleScriptController implements Controller
{
    private Configuration configuration;
    private String logFileLoc;
    private LogData logData;
    private String scriptFileLoc;
    private Script script;
    private Interpreter interpreter;
    private String outputFileLoc;

    public SimpleScriptController(Configuration configuration, String logFileLoc, String scriptFileLoc, String outputFileLoc)
    {
        this.configuration = configuration;
        this.logFileLoc = logFileLoc;
        this.scriptFileLoc = scriptFileLoc;
        this.outputFileLoc = outputFileLoc;
    }

    // getter functions for testing purposes
    @Override
    public String getOutputFileLoc()
    {
        return outputFileLoc;
    }

    public String getLogFileLoc()
    {
        return logFileLoc;
    }

    public String getScriptFileLoc()
    {
        return scriptFileLoc;
    }

    @Override
    public boolean execute()
    {
        // create interpreter for this Loafr execution
        interpreter = new Interpreter(configuration);
        // SimpleScriptController creates one instance of Script
        script = new Script();
        // SimpleScriptController creates one instance of LogData for the input log file
        logData = new LogData();

        // attempt script interpretation
        if(!script.interpretScript(scriptFileLoc,interpreter))
        {
            ErrorHandler.logError("Loafr exiting...");
            return false;
        }
        // attempt log file parsing
        if(!logData.parseLogFile(logFileLoc,configuration))
        {
            ErrorHandler.logError("Loafr exiting...");
            return false;
        }
        // attempt output write
        LogData output_logData = script.executeAnalyzer(logData);
        // attempt output file writing
        if(!output_logData.writeLogData(outputFileLoc,configuration))
        {
            // Should we implement a condition to retry through command line input?
            ErrorHandler.logError("Loafr exiting...");
            return false;
        }
        return true;
    }

    @Override
    public void alertOutput(String message)
    {
        System.out.println(message);
    }
}
