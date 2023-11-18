package com.analyze;

import java.util.ArrayList;

public class Analyzer {

    //logdata is logobject now

    private ArrayList<AnalysisTask> queue = new ArrayList<AnalysisTask>();

    public LogData analyze(LogData analyzeLogObject) { //return output (new logData), analysistask already implemented check design dock for specifics
        for (AnalysisTask task : queue) {
            task.execute(analyzeLogObject);
        }
        return analyzeLogObject;
    }

    public void addToQueue(AnalysisTask newTask) {
        queue.add(newTask);
    }
}
