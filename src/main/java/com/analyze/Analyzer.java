package com.analyze;

import java.util.ArrayList;

public class Analyzer {
    //private AnalysisTask queue[];
    private ArrayList<AnalysisTask> queue = new ArrayList<AnalysisTask>();

    public analyze(LogObject analyzeLogObject) {
        //execute Analysistask on analyzeLogobject
    }

    public addToQueue(AnalysisTask newTask) {
        queue.add(newTask);
    }
}
