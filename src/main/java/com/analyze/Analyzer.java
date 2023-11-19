package com.analyze;

import java.util.ArrayList;

import com.input.LogData;

public class Analyzer {


    private ArrayList<AnalysisTask> queue = new ArrayList<AnalysisTask>();

    //Takes in LogData to execute a task. Returns LogData with results.
    public LogData analyze(LogData analyzeLogObject) {
        for (AnalysisTask task : queue) {
            task.execute(analyzeLogObject);
        }
        return analyzeLogObject;
    }

    //Adds a task to the queue. The task will be applied to a LogData object.
    public void addToQueue(AnalysisTask newTask) {
        queue.add(newTask);
    }
}
