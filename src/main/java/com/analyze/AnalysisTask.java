package com.analyze;

import com.input.DataID;
import com.input.LogData;
import com.input.LogEvent;

public interface AnalysisTask {
     <T> LogData execute(LogData l);

     // getter functions used during unit testing
     LogEvent.AttributeType getAttributeType();
     DataID getDataID();
     String getRegex();
}
