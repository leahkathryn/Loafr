package input;

import com.input.DataID;
import com.input.LogData;

import java.sql.Timestamp;
import java.util.HashMap;
public class LogEvent {
    Timestamp timeStamp;
    HashMap<DataID, LogData>  hashmap;


    //setter for Hashmap
    public static HashMap<DataID, LogData> setHashmap(DataID dataID, LogData logData){
        HashMap<DataID,LogData > hashMap;
        hashMap = new HashMap<>();
        hashMap.put(dataID,logData);
        return hashMap;
    }

    public static LogEvent createEvent(Timestamp timeStamp, HashMap hashmap){
        LogEvent logEvent = new LogEvent();
        logEvent.hashmap = hashmap;
        logEvent.timeStamp = timeStamp;
        return logEvent;
    }

    //getters
    public HashMap<DataID, LogData> getHashmap() {
        return hashmap;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }


}

