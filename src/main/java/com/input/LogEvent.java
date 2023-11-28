package com.input;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ErrorHandler;
import com.control.ControllerFactory;
import com.input.DataID;
import com.input.DataType;

import javax.xml.crypto.Data;

/**
 * @author Yichen Li
 */
public class LogEvent
{
    private Timestamp timeStamp;
    private String eventType;
    private HashMap<DataID, List<?>> dataIDMap;

    //getters
    public HashMap<DataID, List<?>> getDataIDMap() {
        return dataIDMap;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public String getEventType() {
        return eventType;
    }

    // setters
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setDataIDMap(HashMap<DataID, List<?>> dataIDMap) {
        this.dataIDMap = dataIDMap;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    private Boolean tryParseBoolean(String str)
    {
        if (str.equalsIgnoreCase("true"))
        {
            return Boolean.TRUE;
        }
        if (str.equalsIgnoreCase("false"))
        {
            return Boolean.FALSE;
        }
        return null;
    }

    public <T> HashMap<DataID, List<?>> convertInputToDataMap(List<String> inputData, Event event)
    {
        HashMap<DataID,List<?>> dataIDToValues = new HashMap<>();
        // Get the list of DataIDs defined for this event by the configuration file
        List<DataID> validDataIDList = event.getDataIDList();

        // Loafr fails if the input has the incorrect number of data values
        if (inputData.size() != validDataIDList.size())
        {
            ErrorHandler.logError("Failure parsing event from log file: incorrect number of data values" +
                    " for event "+ event.name +".\nLoafr exiting...");
            return dataIDToValues; // empty
        }

        for (int i = 0; i < inputData.size(); i++)
        {
            DataID dataID = validDataIDList.get(i);
            List<T> values = new ArrayList<>();

            // Parse the data from the string and store it in list 'values'
            if (!createDataValueList(dataID.type,inputData.get(i),values))
            {
                return new HashMap<>(); // empty
            }

            dataIDToValues.put(dataID,values);
        }

        return dataIDToValues;
    }

    /**
     * This function parses a String into a list of values. The parameter 'type'
     *      defines the primitive data type of the value. Sometimes the String will
     *      represent a single value, and sometimes it will represent a list of values.
     *      If the first character in the String is '[' then
     *      the String is a list of values that must be parsed into individual Strings before
     *      converting each value to the specified primitive data type (parameter 'type').
     *
     * @param type         a DataType enum type that represents the primitive
     *                     data type of this value.
     * @param stringValue  the String that will be parsed into a value of the defined data type
     * @param output       the list that will store the values
     * @return             Boolean value to indicate success or failure
     * @author             Leah Lehmeier
     */
    private <T> boolean createDataValueList(DataType type, String stringValue, List<T> output)
    {
        // parse each value
        if (stringValue.startsWith("["))
        {
            // if the list is not closed with a ']', this is a syntax error
            if (!stringValue.endsWith("]"))
            {
                ErrorHandler.logError("Failure parsing event from log file: syntax error, missing ']'.\nLoafr exiting...");
                return false;
            }
            // if the list is blank, this is a syntax error
            stringValue = stringValue.substring(1,stringValue.length()-1);
            if (stringValue.isBlank())
            {
                ErrorHandler.logError("Failure parsing event from log file: syntax error, blank data list within brackets [].\nLoafr exiting...");
                return false;
            }
            // Separate each value in the String by space and store them in a list.
            String[] values = stringValue.split(" ");
            List<String> stringValueList = new ArrayList<>();
            // Remove leading/trailing whitespace and add to ArrayList
            for (String str : values)
            {
                stringValueList.add(str.strip());
            }
            // convert from String to data type
            for (String str : stringValueList)
            {
                T value;
                if (null == (value = parseStringToDataValue(type,str)))
                {
                    return false;
                }
                output.add(value);
            }
        }
        else
        {
            T value;
            if (null == (value = parseStringToDataValue(type,stringValue.strip())))
            {
                return false;
            }
            output.add(value);
        }
        return true;
    }

    private <T> T parseStringToDataValue(DataType type, String str)
    {
        T value;

        switch(type)
        {
            case STRING:
                value = (T) str;
                break;
            case CHAR:
                if (str.length() > 1)
                {
                    value = null;
                    break;
                }
                else
                {
                    value = (T) ((Character)str.charAt(0));
                }
                break;
            case INTEGER:
                try {
                    value = (T) Integer.valueOf(str);
                } catch (NumberFormatException ex) {
                    value = null;
                }
                break;
            case FLOAT:
                try {
                    value = (T) Float.valueOf(str);
                } catch (NumberFormatException ex) {
                    value = null;
                }
                break;
            case DOUBLE:
                try {
                    value = (T) Double.valueOf(str);
                } catch (NumberFormatException ex) {
                    value = null;
                }
                break;
            case BOOLEAN:
                value = (T) tryParseBoolean(str);
                break;
            default:
                value = null;
        }
        if (null == value)
        {
            ErrorHandler.logError("Failure parsing event from log file: incorrect data type provided.\nLoafr exiting...");
        }
        return value;
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

        public static AttributeType fromString(String str)
        {
            for (AttributeType a : values()) {
                if (a.keyword.equals(str)) {
                    return a;
                }
            }
            return null;
        }
    }
}
