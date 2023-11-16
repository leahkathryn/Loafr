package com.analyze;

import java.util.List;

public class Search implements AnalysisTask {

    private AttributeType attributeType;
    private DataID dataID;
    private String regex;
    private List<LogEvents> events;
    private List<LogEvents> outputEvents;
    private LogEvents temp;


    public Search(AttributeType type, DataID data, String reg) {
        attributeType = type;
        dataID = data;
        regex = reg;
    }

    public Search(AttributeType type, String reg) {
        attributeType = type;
        //dataID = ?;
        regex = reg;
    }

    public Search(String reg) {
        //attributeType = ?;
        //dataID = ?;
        regex = reg;
    }

    //what functions can i use with logevents,
    @Override
    public void execute(LogObject logObject) {
        events = logObject.getLogEvents();
        for (int i = 0; i < events.size(); i++) {
            temp = events.get(i);
            if (temp.get(dataID) == regex) {
                outputEvents.add(temp);
            }
        }
    }
}
