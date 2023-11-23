package com.control;

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

    @Override
    public void execute()
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
            System.exit(0);
        }
        // attempt log file parsing
        if(!logData.parseLogFile(logFileLoc,configuration))
        {
            System.exit(0);
        }
        // attempt output write
        LogData output_logData = script.executeAnalyzer(logData);
        // attempt output file writing
        if(!output_logData.writeLogData(outputFileLoc,configuration))
        {
            // Should we implement a condition to retry through command line input?
            System.exit(0);
        }
    }

    @Override
    public void alertOutput(String message)
    {
        System.out.println(message);
    }
}
