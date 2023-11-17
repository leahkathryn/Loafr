package com.input;

import java.sql.Timestamp;
import java.util.HashMap;

public class LogEvent<T> {
    private Timestamp timeStamp;
    private String eventType;


    /**
     * default constructor
     * I prefer use this and set other values in other classes.
     */
    public LogEvent(){
    }

    /**
     * Getters and setters.
     */
    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }


    /**
     * Method to identify data types and convert strings to appropriate data types
     * @param str some string,
     * @return the datatype, which is defined in the enumeration.
     */
    private T convertStringToDataType(String str) {
        String regex = "^[-0-9.\\[\\]]+$";
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

    /**
     * Helper method to find if a string is true or false.
     * @param str the input string.
     * @return if this is a boolean value.
     */
    private boolean tryParseBoolean(String str) {
        str = str.toLowerCase();
        return str.equalsIgnoreCase("true") || str.equalsIgnoreCase("false");
    }



    /**
     * The method used for change from string to Hashmap. Which is data1, data2, data3 ...
     * @param str The string being changed.
     * @return return the transformed hashmap.
     */
    // Method to identify data types and convert strings to a Map<String, Object>
    public HashMap<String, Object> convertStringToDataMap(String str) {
        T convertedValue = convertStringToDataType(str);
        if (convertedValue != null) {
            HashMap<String, Object> dataMap = new HashMap<>();
            dataMap.put("value", str);
            dataMap.put("dataType", convertedValue);
            return dataMap;
        }
        return null;
    }


    /**
     * The requested nested enum.
     */
    public enum AttributeType{
        TIMESTAMP("timeStamp"),
        EVENT("event"),
        DATAID("dataID"),
        DATATYPE("dataType"),
        DATAVALUE("dataValue");

        public String keyword;

        AttributeType(String keyword) {
            this.keyword = keyword;
        }

        @Override
        public String toString() {
            return this.keyword;
        }


    }
}
