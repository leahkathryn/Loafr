package com.analyze;

import java.util.ArrayList;

import com.input.LogData;
import com.input.LogEvent;

public class Analyzer
{
    private ArrayList<AnalysisTask> queue = new ArrayList<AnalysisTask>();

    //Takes in LogData to execute a task. Returns LogData with results.
    public LogData analyze(LogData analyzeLogObject) {
        LogData outputLogData = new LogData();
        for (LogEvent event : analyzeLogObject.getEventList())
        {
            outputLogData.addLogEvent(event);
        }
        for (AnalysisTask task : queue) {
            outputLogData = task.execute(outputLogData);
        }
        return outputLogData;
    }

    //Adds a task to the queue. The task will be applied to a LogData object.
    public void addToQueue(AnalysisTask newTask) {
        queue.add(newTask);
    }
}
