package com.analyze;

import com.input.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * All tests assume log file and config file are valid, correct, and are parsed correctly
 */
public class SortTest {

    private static LogData logDataEvent = new LogData();
    private static LogData logDataDataID = new LogData();
    private static DataID dataID = new DataID("Error_Code", DataType.INTEGER);

    @BeforeAll
    static void setupTests(){
        Configuration config = new Configuration();
        URL configurationFileLoc = SortTest.class.getClassLoader().getResource("sample_config_file.xml");

        String logFileLoc = SortTest.class.getClassLoader().getResource("log_file_3").toString().substring(5);
        config.parseConfigFile(configurationFileLoc);


        logDataEvent.parseLogFile(logFileLoc, config);
        LogEvent logEventElem;

        for (int i = 0; i < logDataEvent.getEventList().size(); i++) {
            logEventElem = logDataEvent.getEventList().get(i);
            for (DataID curDataID : logEventElem.getDataIDMap().keySet()){
                if (curDataID.getName().equals(dataID.getName())){
                    logDataDataID.addLogEvent(logEventElem);
                }
            }
        }
    }


    @Test
    void execute_sortByTimeStampAscending_Successful(){
        Sort sort = new Sort(LogEvent.AttributeType.TIMESTAMP, true);
        URL configurationFileLoc = getClass().getClassLoader().getResource("sample_config_file.xml");
        String logFileLoc = getClass().getClassLoader().getResource("log_file_3").toString().substring(5);
        Configuration config = new Configuration();
        LogData testLogData = new LogData();
        config.parseConfigFile(configurationFileLoc);
        testLogData.parseLogFile(logFileLoc, config);

        LogData ret = sort.execute(testLogData);

        for (int i = 0; i < ret.getEventList().size() - 1; i++){
            if (ret.getEventList().get(i).getTimeStamp().compareTo(ret.getEventList().get(i + 1).getTimeStamp()) > 0){
                fail();
            }
        }
    }

    @Test
    void execute_sortByTimeStampDescending_Successful(){
        Sort sort = new Sort(LogEvent.AttributeType.TIMESTAMP, false);
        URL configurationFileLoc = getClass().getClassLoader().getResource("sample_config_file.xml");
        String logFileLoc = getClass().getClassLoader().getResource("log_file_3").toString().substring(5);
        Configuration config = new Configuration();
        LogData testLogData = new LogData();
        config.parseConfigFile(configurationFileLoc);
        testLogData.parseLogFile(logFileLoc, config);

        LogData ret = sort.execute(testLogData);

        for (int i = 0; i < ret.getEventList().size() - 1; i++){
            if (ret.getEventList().get(i).getTimeStamp().compareTo(ret.getEventList().get(i + 1).getTimeStamp()) < 0){
                System.out.println(ret.getEventList().get(i).getTimeStamp());
                System.out.println(ret.getEventList().get(i + 1).getTimeStamp());
                fail();
            }
        }
    }

    @Test
    void execute_sortByEventAscending_Successful(){
        Sort sort = new Sort(LogEvent.AttributeType.EVENT, true);

        URL configurationFileLoc = getClass().getClassLoader().getResource("sample_config_file.xml");
        String logFileLoc = getClass().getClassLoader().getResource("log_file_3").toString().substring(5);
        Configuration config = new Configuration();
        LogData testLogData = new LogData();
        config.parseConfigFile(configurationFileLoc);
        testLogData.parseLogFile(logFileLoc, config);

        LogData ret = sort.execute(testLogData);

        for (int i = 0; i < ret.getEventList().size() - 1; i++){
            if (ret.getEventList().get(i).getEventType().compareTo(ret.getEventList().get(i + 1).getEventType()) > 0){
                fail();
            }
            if (ret.getEventList().get(i).getTimeStamp().compareTo(ret.getEventList().get(i + 1).getTimeStamp()) > 0){
                if (ret.getEventList().get(i).getEventType().equals(ret.getEventList().get(i + 1).getEventType()))
                    fail();
            }
        }
    }

    @Test
    void execute_sortByEventDescending_Successful(){
        Sort sort = new Sort(LogEvent.AttributeType.EVENT, false);

        URL configurationFileLoc = getClass().getClassLoader().getResource("sample_config_file.xml");
        String logFileLoc = getClass().getClassLoader().getResource("log_file_3").toString().substring(5);
        Configuration config = new Configuration();
        LogData testLogData = new LogData();
        config.parseConfigFile(configurationFileLoc);
        testLogData.parseLogFile(logFileLoc, config);

        LogData ret = sort.execute(testLogData);

        for (int i = 0; i < ret.getEventList().size() - 1; i++){
            if (ret.getEventList().get(i).getEventType().compareTo(ret.getEventList().get(i + 1).getEventType()) < 0){
                fail();
            }
            if (ret.getEventList().get(i).getTimeStamp().compareTo(ret.getEventList().get(i + 1).getTimeStamp()) < 0){
                if (ret.getEventList().get(i).getEventType().equals(ret.getEventList().get(i + 1).getEventType()))
                    fail();
            }
        }
    }
    @Test
    void execute_sortByDataIDAscending_Successful(){
        Sort sort = new Sort(LogEvent.AttributeType.DATAID, dataID, true);

        URL configurationFileLoc = getClass().getClassLoader().getResource("sample_config_file.xml");
        String logFileLoc = getClass().getClassLoader().getResource("log_file_3").toString().substring(5);
        Configuration config = new Configuration();
        LogData testLogData = new LogData();
        config.parseConfigFile(configurationFileLoc);
        testLogData.parseLogFile(logFileLoc, config);
        LogData ret = sort.execute(testLogData);

        for (int i = 0; i < ret.getEventList().size() - 1; i++){
            for (DataID curDataID : ret.getEventList().get(i).getDataIDMap().keySet()){
                if (curDataID.getName().equals(dataID.getName())){
                    if (compareToDataIDByType(ret.getEventList().get(i).getDataIDMap().get(curDataID), ret.getEventList().get(i+1).getDataIDMap().get(curDataID)) > 0){
                        fail();
                    }
                }
            }
        }
    }

    @Test
    void execute_sortByDataIDDescending_Successful(){
        Sort sort = new Sort(LogEvent.AttributeType.DATAID, dataID, false);

        URL configurationFileLoc = getClass().getClassLoader().getResource("sample_config_file.xml");
        String logFileLoc = getClass().getClassLoader().getResource("log_file_3").toString().substring(5);
        Configuration config = new Configuration();
        LogData testLogData = new LogData();
        config.parseConfigFile(configurationFileLoc);
        testLogData.parseLogFile(logFileLoc, config);
        LogData ret = sort.execute(testLogData);

        for (int i = 0; i < ret.getEventList().size() - 1; i++){
            for (DataID curDataID : ret.getEventList().get(i).getDataIDMap().keySet()){
                if (curDataID.getName().equals(dataID.getName())){
                    if (compareToDataIDByType(ret.getEventList().get(i).getDataIDMap().get(curDataID), ret.getEventList().get(i+1).getDataIDMap().get(curDataID)) < 0){
                        fail();
                    }
                }
            }
        }
    }

    @Test
    void execute_InvalidAttributeType_Failure(){
        Sort sort = new Sort(null, true);
        URL configurationFileLoc = getClass().getClassLoader().getResource("sample_config_file.xml");
        String logFileLoc = getClass().getClassLoader().getResource("log_file_3").toString().substring(5);
        Configuration config = new Configuration();
        LogData testLogData = new LogData();
        config.parseConfigFile(configurationFileLoc);
        testLogData.parseLogFile(logFileLoc, config);

        LogData ret = sort.execute(testLogData);

        if (ret != null){
            fail();
        }
    }
    @Test
    void execute_SortByDataIDWhenSpecifiedDataIDIsNull_Failure(){
        Sort sort = new Sort(LogEvent.AttributeType.DATAID, true);
        URL configurationFileLoc = getClass().getClassLoader().getResource("sample_config_file.xml");
        String logFileLoc = getClass().getClassLoader().getResource("log_file_3").toString().substring(5);
        Configuration config = new Configuration();
        LogData testLogData = new LogData();
        config.parseConfigFile(configurationFileLoc);
        testLogData.parseLogFile(logFileLoc, config);

        LogData ret = sort.execute(testLogData);

        if (ret != null){
            fail();
        }
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
            default:
                return 0;
        }
    }
}
