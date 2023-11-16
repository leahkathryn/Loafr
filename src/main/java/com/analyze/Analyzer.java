package com.analyze;

import java.util.ArrayList;

public class Analyzer {
    //private AnalysisTask queue[];
    //questions i have
    //where do i find output file location
    //how do i interact with logevent
    //how do i create logobject
    private LogObject output;

    private ArrayList<AnalysisTask> queue = new ArrayList<AnalysisTask>();

    public analyze(LogObject analyzeLogObject) {
        AnalysisTask temp = new Search(type, data, reg);
        temp.execute(analyzeLogObject);
        output = temp.outputEvents;
    }

    public addToQueue(AnalysisTask newTask) {
        queue.add(newTask);
    }
}
