package com.control;

import com.input.Configuration;
import com.input.LogData;
import com.interpret.Interpreter;
import com.interpret.Script;

import java.util.List;

public class BatchScriptController implements Controller
{
    private Configuration configuration;
    private String flag;
    private List<String> logFileLoc;
    private List<LogData> logObjectList;
    private List<String> scriptFileLoc;
    private List<Script> scriptList;
    private Interpreter interpreter;
    private String outputFileLoc;

    public BatchScriptController(Configuration configuration, String flag, List<String> logFileLoc, List<String> scriptFileLoc, String outputFileLoc)
    {
        this.configuration = configuration;
        this.flag = flag;
        this.logFileLoc = logFileLoc;
        this.scriptFileLoc = scriptFileLoc;
        this.outputFileLoc = outputFileLoc;
    }

    @Override
    public void execute(){}

    @Override
    public void alertOutput(String message)
    {
        // temp implementation
        System.out.println(message);
    }

    private void mergeByLogObject() {}

    private void mergeByLogEvent() {}
}
