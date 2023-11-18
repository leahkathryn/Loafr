package com.analyze;

import com.input.LogData;

public interface AnalysisTask {
     <T> LogData execute(LogData l);
}
