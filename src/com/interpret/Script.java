package com.interpret;

import com.analyze.Analyzer;
import com.input.LogObject;

public class Script implements AnalysisManager
{
    private Analyzer analyzer;

    @Override
    public LogObject executeAnalyzer(LogObject logObject){ return null;}

    public boolean interpretScript(String fileLoc, Interpreter interpreter){ return true;}
}
