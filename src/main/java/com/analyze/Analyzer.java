package com.analyze;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import com.input.LogData;
import com.input.LogEvent;

public class Analyzer
{
    private List<AnalysisTask> queue = new ArrayList<AnalysisTask>();

    // getter function used during unit testing
    public List<AnalysisTask> getTaskQueue()
    {
        return Collections.unmodifiableList(queue);
    }

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
