package com.control;

import com.input.Configuration;

public interface Controller
{
    void execute();
    void setConfiguration(Configuration configuration);
    void alertOutput(String message);
}
