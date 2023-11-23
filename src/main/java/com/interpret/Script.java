package com.interpret;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

import com.ErrorHandler;
import com.analyze.AnalysisTask;
import com.analyze.Analyzer;
import com.input.LogData;

/**
 * Facilitates the interpretation and execution of the script provided to Loafr.
 *
 * @author Leah Lehmeier
 */
public class Script implements AnalysisManager
{
    private Analyzer analyzer;

    public Script()
    {
        analyzer = new Analyzer();
    }

    @Override
    public LogData executeAnalyzer(LogData logData)
    {
        LogData log = analyzer.analyze(logData);
        return log;
    }

    /**
     * This function facilitates parsing the script file and generating
     *      AnalysisTasks from each line of the script via the Interpreter.
     *
     * @param fileLoc      the file location of the script
     * @param interpreter  the Interpreter will generate an AnalysisTask
     *                     from each line of the script
     * @return             boolean indicating success or failure
     */
    public boolean interpretScript(String fileLoc, Interpreter interpreter)
    {
        // this list will hold the plain String of the script file
        List<String> scriptInstructions = new ArrayList<>();
        // try reading each line of the script file into a list
        if (!readScriptFile(fileLoc,scriptInstructions))
        {
            return false;
        }

        for (String instruction : scriptInstructions)
        {
            // parse each word of this line of the script into a separate String in this list
            List<String> parsedInstruction = new ArrayList<>(Arrays.asList(instruction.split(" ")));
            // remove the first word in the script instruction because it is the task keyword
            String taskKeyword = parsedInstruction.remove(0);
            // generate new analysis task
            AnalysisTask newTask = interpreter.getAnalysisTask(taskKeyword,parsedInstruction);
            if (null == newTask)
            {
                // ErrorHandler has already sent specific error
                return false;
            }
            analyzer.addToQueue(newTask);
        }
        return true;
    }

    /**
     * Parses each line of the script file into the output parameter
     *      scriptInstructions. In the case of failure, ErrorHandler
     *      sends an error message through the Controller and this
     *      function returns false.
     *
     * @param fileLoc             the file location of the script
     * @param scriptInstructions  output parameter, each item in the list
     *                            represents a line of the script file
     * @return                    boolean indicating success or failure
     */
    private boolean readScriptFile(String fileLoc, List<String> scriptInstructions)
    {
        try
        {
            File logFile = new File(fileLoc);
            Scanner myReader = new Scanner(logFile);
            // read every line of the file into the list
            while (myReader.hasNextLine()) {
                String instruction = myReader.nextLine();
                // do not add blank line to the list
                if (!instruction.isBlank())
                {
                    scriptInstructions.add(instruction);
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            ErrorHandler.logError("Failure opening script file: FileNotFoundException.\nLoafr exiting...");
            return false;
        } catch (NullPointerException e) {
            ErrorHandler.logError("Failure opening script file: NullPointerException.\nLoafr exiting...");
            return false;
        }

        // if the file was empty, send error and return false
        if (scriptInstructions.isEmpty())
        {
            ErrorHandler.logError("Failure reading script file: File may be empty.\nLoafr exiting...");
            return false;
        }

        return true;
    }
}
