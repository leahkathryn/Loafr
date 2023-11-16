package com.analyze;

public class Search implements AnalysisTask {

    private AttributeType attributeType;
    private DataID dataID;
    private String regex;

    public Search(AttributeType type, DataID data, String reg) {
        attributeType = type;
        dataID = data;
        regex = reg;
    }

    public Search(AttributeType type, String reg) {
        attributeType = type;
        regex = reg;
    }

    public Search(String reg) {
        regex = reg;
    }

    @Override
    public void execute(LogObject logObject) {

    }
}
