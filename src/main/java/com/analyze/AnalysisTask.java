package com.analyze;

import com.input.LogData;

public interface AnalysisTask {
    void <T> execute(LogData l);
}
