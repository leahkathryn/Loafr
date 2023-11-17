package com.analyze;

import java.util.ArrayList;

public class Analyzer {

    //logdata is logobject now

    private ArrayList<AnalysisTask> queue = new ArrayList<AnalysisTask>();

    public LogData analyze(LogObject analyzeLogObject) { //return output (new logData), analysistask already implemented check design dock for specifics
        LogData temp = analyzeLogObject;
        for (AnalysisTask task : queue) {
            temp = task.execute(temp);
        }
        return temp;
    }

    public void addToQueue(AnalysisTask newTask) {
        queue.add(newTask);
    }
}
