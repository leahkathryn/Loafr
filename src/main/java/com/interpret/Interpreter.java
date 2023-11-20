package com.interpret;

import com.ErrorHandler;
import com.input.Configuration;
import com.input.DataID;
import com.input.LogEvent;
import com.analyze.AnalysisTask;
import com.analyze.Search;

import java.util.List;

/**
 * Responsible for interpreting the text input from the script file into AnalysisTask instances.
 *
 * @author Leah Lehmeier
 */
public class Interpreter
{
    private Configuration configuration;

    public Interpreter(Configuration configuration)
    {
        this.configuration = configuration;
    }

    /**
     * This function determines which type of AnalysisTask should be generated and
     *      calls the corresponding private function that will generate the task.
     *
     * @param taskKeyWord   a string representing which type of AnalysisTask should be generated
     * @param instructions  a list of String input to be parsed into parameters for the AnalysisTask
     *
     * @return              AnalysisTask instance
     */
    public AnalysisTask getAnalysisTask(String taskKeyWord, List<String> instructions)
    {
        switch (taskKeyWord)
        {
            case "search":
                return parseSearchInstruction(instructions);
            case "sort":
            case "filter":
            default:
                return null;
        }
    }

    /**
     * This function will generate a new Search analysis task. The Search constructor that is
     *      used will depend on the length of the instructions list. Input and syntax are verified.
     *      Regular expression syntax is not verified.
     *
     * @param instructions  a list of String input to be parsed into parameters for the Search
     *
     * @return              Search instance
     */
    private AnalysisTask parseSearchInstruction(List<String> instructions)
    {
        AnalysisTask task;

        // check that the instruction list is not empty before interpretation
        if (instructions.isEmpty())
        {
            ErrorHandler.logError("Failure interpreting script: " +
                    "Insufficient input is provided to a Search command.\nLoafr exiting...");
            return null;
        }

        // the only argument supplied is a regex
        if (instructions.size() == 1)
        {
            String regex = getRegEx(instructions.get(0));
            task = new Search(regex);
        }
        else
        {
            // if the instructions have >1 arguments, the first argument will be the attribute type to search through
            LogEvent.AttributeType attributeType;
            if (null == (attributeType= getAttributeType(instructions.get(0))))
            {
                // ErrorHandler already sent message
                return null;
            }

            // if the attribute type to be searched through is the data values of a DataID,
            // the DataID name will be provided in the script. The provided name will need to
            // be checked for validity against the list in Configuration.
            if (LogEvent.AttributeType.DATAVALUE  == attributeType)
            {
                if (instructions.size() != 3)
                {
                    ErrorHandler.logError("Failure interpreting script: " +
                            "There is a syntax error within a Search command.\nLoafr exiting...");
                    return null;
                }
                task = new Search(attributeType,getDataID(instructions.get(1)),getRegEx(instructions.get(2)));
            }
            else
            {
                if (instructions.size() != 2)
                {
                    ErrorHandler.logError("Failure interpreting script: " +
                            "There is a syntax error within a Search command.\nLoafr exiting...");
                    return null;
                }
                task = new Search(attributeType,getRegEx(instructions.get(1)));
            }
        }
        return task;
    }

    /**
     * This function will parse the regular expression from forward-slash delimiters if they are present.
     *      Any leading or trailing whitespace is removed. If the regular expression is an empty String,
     *      log an error and return null.
     *
     * @param regex  the regular expression String as it exists in the script
     *
     * @return       parsed regular expression String
     */
    private String getRegEx(String regex)
    {
        // remove leading and trailing whitespace
        regex = regex.strip();
        // remove regex delimiters, if present
        if (regex.charAt(0) == '/' && regex.charAt(regex.length()-1) == '/')
        {
            regex = regex.substring(1,regex.length()-1);
        }
        // if the regex is empty, log an error and return null
        if (regex.isBlank())
        {
            ErrorHandler.logError("Failure interpreting script: " +
                    "No arguments were provided for a Search command.\nLoafr exiting...");
            return null;
        }
        return regex;
    }

    /**
     * This function will parse the attribute type String from the script into an AttributeType enum type.
     *      If the String does not represent an AttributeType, log an error and return null.
     *      log an error and return null.
     *
     * @param attributeString  the regular expression String as it exists in the script
     *
     * @return                 parsed regular expression String
     */
    private LogEvent.AttributeType getAttributeType(String attributeString)
    {
        attributeString = attributeString.trim();
        LogEvent.AttributeType attributeType;

        if (null == (attributeType = LogEvent.AttributeType.fromString(attributeString)))
        {
            ErrorHandler.logError("Failure interpreting script: " +
                    "There is a syntax error or unrecognized argument within a Search command.\nLoafr exiting...");
            return null;
        }

        return attributeType;
    }

    /**
     * This function will compare the input parameter against the 'name' attribute of each DataID
     *      that is listed in the Configuration. If a match is found, that DataID will be returned.
     *      If no match is found, the input is invalid, log an error and return null.
     *
     * @param dataIDString  the regular expression String as it exists in the script
     *
     * @return              parsed regular expression String
     */
    private DataID getDataID(String dataIDString)
    {
        dataIDString = dataIDString.trim();
        List<DataID> dataIDList = configuration.getDataIDList();
        DataID match = null;
        // search for the DataID that corresponds to the name provided in the script
        for (DataID dataID : dataIDList)
        {
            if (dataID.getName().equals(dataIDString))
            {
                match = dataID;
                break;
            }
        }
        if (null == match)
        {
            System.out.println("First exit condition: 4");
            ErrorHandler.logError("Failure interpreting script: " +
                    "There is a syntax error or unrecognized argument within a Search command.\nLoafr exiting...");
            return null;
        }
        return match;
    }
}
