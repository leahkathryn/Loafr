package com.control;

import com.input.Configuration;

/**
 * Interface implemented by the SimpleScriptController and
 *      BatchScriptController classes.
 *
 * @author Leah Lehmeier
 */
public interface Controller
{
    void execute();
    void alertOutput(String message);
}
