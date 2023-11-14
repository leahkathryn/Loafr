package input;

import com.input.DataID;
import com.input.LogData;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

public class LogEvent<T>
{
    private Timestamp timeStamp;
    private String eventType;
    private HashMap<DataID,List<T>>  dataIDMap;

    public LogEvent(String eventType, Timestamp timeStamp, HashMap<> dataIDMap)
    {
        this.eventType = eventType;
        this.timeStamp = timeStamp;
        this.dataIDMap = dataIDMap;
    }

    //getters
    public HashMap<DataID,List<T>> getDataIDMap() {
        return dataIDMap;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }
}

