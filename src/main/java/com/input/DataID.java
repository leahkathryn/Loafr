package com.input;

public class DataID {
    String name;
    DataType type;

    public DataID(String name, DataType type){
        this.name = name;
        this.type = type;
    }

    public DataID (){
        name = "";
        type = null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(DataType type) {
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public DataType getType() {
        return this.type;
    }
}
