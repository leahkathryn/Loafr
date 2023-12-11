package com.analyze;

import com.ErrorHandler;
import com.input.DataID;
import com.input.LogData;
import com.input.LogEvent;
import com.input.LogEvent.AttributeType;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;

/**
 * The Sort class provides the sorting functionality for Loafr. When a Sort object is instantiated, the execute method
 * can be called to sort a list of LogEvents within a LogData object in ascending or descending order and by timestamp,
 * by event name, or by the value of a list associated to a given data element (refered to in the code as a DataID). The
 * method execute() and its helper functions follow section 4.2.19 of the design document and requirements 35 and 36.
 *
 * @author Jeremiah Hockett
 */
public class Sort implements AnalysisTask{
    private AttributeType attributeType;
    private DataID dataID = null;
    private boolean direction;                      // True for ascending, False for descending

    @Override
    public AttributeType getAttributeType() {
        return attributeType;
    }

    @Override
    public DataID getDataID() {
        return dataID;
    }

    @Override
    public String getRegex() {
        return null;
    }

    public Sort(AttributeType attributeType, DataID dataID, boolean direction) {
        this.attributeType = attributeType;
        this.dataID = dataID;
        this.direction = direction;
    }

    public Sort(AttributeType attributeType, boolean direction) {
        this.attributeType = attributeType;
        this.direction = direction;
    }

    public Sort(AttributeType attributeType) {
        this.attributeType = attributeType;
        this.direction = true;
    }

    /**
     * Sort by a particular attribute from ascending or descending order. If sorted by dataID's, all LogEvents objects
     * without the specified dataID will be absent from the sorted list. This method utilizes iterative merge sort to sort
     * LogEvent object
     *
     * Assumes config file, log file, and output file location are valid and parsed correctly.
     *
     * @param l - the input LogData that its attribute eventList will be sorted.
     * @param <T> - Java generic
     * @return a LogData object with all the LogEvent of the input, except they are sorted
     */
    @Override
    public <T> LogData execute(LogData l) {
        List<LogEvent> logEventList = l.getEventList();
        List<LogEvent> sortableLogEvents;

        sortableLogEvents = getSortableLogEvents(logEventList);

        if (sortableLogEvents == null){
            return null;
        }

        int startL;
        int endL;
        int endR;

        for (int j = 1; j < sortableLogEvents.size(); j*=2){
            for (int k = 0; k < sortableLogEvents.size() - 1; k+=2*j){
                startL = k;
                endL = min(startL + j - 1, sortableLogEvents.size() - 1);
                endR = min(startL + 2*j - 1, sortableLogEvents.size()-1);

                merge(sortableLogEvents, startL, endL, endR);
            }
        }

        LogData logData = new LogData();
        if (!direction){
            for (int elem = sortableLogEvents.size() - 1; elem >= 0; elem--){
                logData.addLogEvent(sortableLogEvents.get(elem));
            }
            return logData;
        } else {
            for (LogEvent sortableLogEvent : sortableLogEvents) {
                logData.addLogEvent(sortableLogEvent);
            }
        }

        return logData;
    }

    /**
     * If sorting by DataIDs, remove all LogEvents that do not contain the dataID that is specified. Otherwise, return the
     * original list.
     * @param logEventList - the input list to be "filtered"
     * @return a list of LogEvents
     */
    private List<LogEvent> getSortableLogEvents(List<LogEvent> logEventList) {
        List<LogEvent> sortableList = new ArrayList<>();
        if (attributeType == null){
            ErrorHandler.logError("Failure sorting log events: the requested attribute type is not implemented by Loafr." +
                    "\nLoafr exiting...");
            return null;
        }
        if (dataID == null && attributeType == AttributeType.DATAID) {
            ErrorHandler.logError("Failure sorting log events: attempt to sort by data element failed as data " +
                    "Element has not been specified. \nLoafr exiting...");
            return null;
        }
        switch (attributeType) {
            case TIMESTAMP:           // Revisit switch case statement later?
            case EVENT:
                return logEventList;
            case DATAID:
                if (!logEventList.isEmpty()){
                    for (LogEvent logEventElem : logEventList) {
                        for (DataID curDataID : logEventElem.getDataIDMap().keySet()) {
                            if (curDataID.getName().equals(dataID.getName())) {
                                sortableList.add(logEventElem);
                            }
                        }
                    }
                    return sortableList;
                } else {
                    return logEventList;
                }
            default:
                return null;
        }
    }

    /**
     * The subroutine for the execute method of the Sort class. It merges to sorted sublists into a larger sorted list that
     * is the sublist of the input list from startL inclusive to sndR inclusive.
     * @param list - the input list
     * @param startL - the starting value of the sublist
     * @param endL - the middle value of the sublist
     * @param endR - the end value of the sublist
     */
    private void merge(List<LogEvent> list, int startL, int endL, int endR){
        LogEvent [] ret = new LogEvent[endR - startL + 1];
        List<LogEvent> L = new ArrayList<>();
        List<LogEvent> R = new ArrayList<>();

        for (int m = startL; m <= endL; m++){
            L.add(list.get(m));
        }
        for (int n = endL + 1; n <= endR; n++){
            R.add(list.get(n));
        }

        int i;
        int j = 0;
        int k = 0;
        for (i = 0; j < L.size() && k < R.size(); i++) {
            if (compareObjects(L.get(j), R.get(k)) <= 0){
                ret[i] = L.get(j);
                j++;
            } else {
                ret[i] = R.get(k);
                k++;
            }
        }

        while (j < L.size()){
            ret[i] = L.get(j);
            j++;
            i++;
        } while (k < R.size()){
            ret[i] = R.get(k);
            k++;
            i++;
        }

        for (int p = 0; p < ret.length; p++){
            list.set(p+startL, ret[p]);
        }
    }

    private int compareObjects(LogEvent L, LogEvent R){
        if (attributeType == AttributeType.TIMESTAMP){
            return L.getTimeStamp().compareTo(R.getTimeStamp());
        } else if (attributeType == AttributeType.EVENT){
            return L.getEventType().compareTo(R.getEventType());
        } else if (attributeType == AttributeType.DATAID){
            DataID dataIDL = new DataID();
            DataID dataIDR = new DataID();
            for (DataID dataID0 :  L.getDataIDMap().keySet()){
                if (dataID0.getName().equals(dataID.getName()))
                    dataIDL = dataID0;
            }
            for (DataID dataID0 :  R.getDataIDMap().keySet()) {
                if (dataID0.getName().equals(dataID.getName()))
                    dataIDR = dataID0;
            }
            return compareToDataIDByType(L.getDataIDMap().get(dataIDL), R.getDataIDMap().get(dataIDR));
        } else
            return 0;
    }

    private int compareToDataIDByType(List<?> lst0, List<?> lst1) {
        switch (dataID.getType()) {
            case INTEGER:
                Integer int0 = (int) lst0.get(0);
                Integer int1 = (int) lst1.get(0);
                return int0.compareTo(int1);
            case STRING:
                String str0 = (String) lst0.get(0);
                String str1 = (String) lst1.get(0);
                return str0.compareTo(str1);
            case BOOLEAN:
                Boolean bool0 = (boolean) lst0.get(0);
                Boolean bool1 = (boolean) lst1.get(0);
                return bool0.compareTo(bool1);
            case FLOAT:
                Float float0 = (float) lst0.get(0);
                Float float1 = (float) lst1.get(0);
                return float0.compareTo(float1);
            case DOUBLE:
                Double double0 = (double) lst0.get(0);
                Double double1 = (double) lst1.get(0);
                return double0.compareTo(double1);
            case CHAR:
                Character char0 = (char) lst0.get(0);
                Character char1 = (char) lst1.get(0);
                return char0.compareTo(char1);
            case LONG:
                Long long0 = (long) lst0.get(0);
                Long long1 = (long) lst1.get(0);
                return long0.compareTo(long1);
        }
        return 0;
    }
}