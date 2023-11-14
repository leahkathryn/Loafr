package com.control;

import com.input.Configuration;
import com.input.LogObject;
import com.interpret.Interpreter;
import com.interpret.Script;

public class SimpleScriptController implements Controller
{
    private Configuration configuration;
    private String logFileLoc;
    private LogObject logObject;
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
    public void execute(){}

    @Override
    public void setConfiguration(Configuration configuration)
    {
        this.configuration = configuration;
    }

    @Override
    public void alertOutput(String message)
    {
        // temp implementation
        System.out.println(message);
    }
}
