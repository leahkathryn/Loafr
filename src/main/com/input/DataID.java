package com.input;

import com.input.DataType;
public class DataID {
    String name;
    DataType type;

    public static DataID constructDataID(){
        DataID newDataID = new DataID();
        return newDataID;
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

