package com.input;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.input.DataID;
import com.input.DataType;

import javax.xml.crypto.Data;

public class LogEvent<T> {
    private Timestamp timeStamp;
    private String eventType;
    private HashMap<DataID, List<T>> dataIDMap;


    // This is a constructor for the LogEvent class.
    // Constructor that infers data types for the values starting from the third parameter
    public LogEvent(String eventType, Timestamp timeStamp, T... values) {
        this.eventType = eventType;
        this.timeStamp = timeStamp;
        this.dataIDMap = new HashMap<>();

        int index = 0;
        for (int i = 2; i < values.length; i++) {
            DataType dataType = inferDataType(values[i]);
            DataID dataID = new DataID("DataID_" + index, dataType);
            List<T> dataList = extractValuesIntoList(values[i]);
            dataIDMap.put(dataID, dataList);
            index++;
        }
    }

    public LogEvent(){
    }

    //getters
    public HashMap<DataID, List<T>> getDataIDMap() {
        return dataIDMap;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setDataIDMap(HashMap<DataID, List<T>> dataIDMap) {
        this.dataIDMap = dataIDMap;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }


    // Method to infer the data type
    private DataType inferDataType(T value) {
        if (value instanceof Integer) {
            return DataType.INTEGER;
        } else if (value instanceof Long) {
            return DataType.LONG;
        } else if (value instanceof Float) {
            return DataType.FLOAT;
        } else if (value instanceof Double) {
            return DataType.DOUBLE;
        } else if (value instanceof Boolean) {
            return DataType.BOOLEAN;
        } else if (value instanceof String) {
            return DataType.STRING;
        } else if (value instanceof Character) {
            return DataType.CHAR;
        }
        // Add additional checks if needed for other types
        return null; // or throw an exception for unsupported types
    }


    private List<T> extractValuesIntoList(T value) {
        List<T> dataList = new ArrayList<>();

        if (value instanceof List) {
            dataList.addAll((List<T>) value);
        } else if (value instanceof Object[]) {
            for (Object obj : (Object[]) value) {
                dataList.add((T) obj);
            }
        } else {
            dataList.add(value);
        }
        return dataList;
    }

    // Method to identify data types and convert strings to appropriate data types
    private T convertStringToDataType(String str) {
        String regex = "^[-0-9.\\[\\]]+$";
        //1. check if it contains a non-number char, if so, it is a char or string.
        if (str.matches(regex))
        {
            if(!str.contains(".")){
                if(str.length() > 32){
                    return (T) DataType.LONG;
                }
                else{
                    return (T) DataType.INTEGER;
                }
            }
            else{
                if(str.length() > 9){
                    return (T)DataType.DOUBLE;
                }
                else{
                    return (T)DataType.FLOAT;
                }
            }
        } else{
            if(str.length() == 1){
                return  (T) DataType.CHAR;
            }
            else{
                if(tryParseBoolean(str)){
                    return (T) DataType.BOOLEAN;
                }
                else{
                    return  (T) DataType.STRING;
                }
            }
        }
    }


    private boolean tryParseBoolean(String str) {
        return str.equalsIgnoreCase("true") || str.equalsIgnoreCase("false");
    }

    private boolean tryParseChar(String str) {
        return str.length() == 1;
    }



    // Method to convert a String array to a List<Map<String, Object>> while identifying data types
    public List<HashMap<String, Object>> stringArrayToHashMapList(String[] stringArray) {
        List<HashMap<String, Object>> resultList = new ArrayList<>();
        for (int i = 0; i < stringArray.length; i++) {
            HashMap<String, Object> dataMap = convertStringToDataMap(stringArray[i]);
            if (dataMap != null) {
                dataMap.put("value", stringArray[i]);
                T dataType = convertStringToDataType(stringArray[i]);
                dataMap.put("dataType", dataType);
                resultList.add(dataMap);
            } else {
                System.out.println("Null value emerged!");
            }
        }
        return resultList;
    }


    // Method to identify data types and convert strings to a Map<String, Object>
    private HashMap<String, Object> convertStringToDataMap(String str) {
        T dataType = convertStringToDataType(str);
        T convertedValue = convertStringToDataType(str);
        if (dataType != null && convertedValue != null) {
            HashMap<String, Object> dataMap = new HashMap<>();
            dataMap.put("value", str);
            dataMap.put("dataType", convertedValue);
            return dataMap;
        }
        return null;
    }

}
