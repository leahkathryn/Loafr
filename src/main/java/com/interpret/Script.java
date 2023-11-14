package com.interpret;

import com.analyze.Analyzer;
import com.input.LogData;

public class Script implements AnalysisManager
{
    private Analyzer analyzer;

    @Override
    public LogData executeAnalyzer(LogData logObject){ return null;}

    public boolean interpretScript(String fileLoc, Interpreter interpreter){ return true;}
}
