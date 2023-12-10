package com.interpret;

import com.ErrorHandler;
import com.input.Configuration;
import com.input.DataID;
import com.input.LogEvent;
import com.analyze.AnalysisTask;
import com.analyze.Search;
import com.analyze.Sort;
import com.analyze.Filter;
import com.analyze.Range;

import java.util.List;

/**
 * Responsible for interpreting the text input from the script file into AnalysisTask instances.
 *
 * @author Leah Lehmeier
 */
public class Interpreter
{
    private Configuration configuration;
    private String taskKeyWord;

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
        this.taskKeyWord = taskKeyWord;
        switch (taskKeyWord)
        {
            case "search":
                return parseSearchInstruction(instructions);
            case "sort":
                return parseSortInstruction(instructions);
            case "filter":
                return parseFilterInstruction(instructions);
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
                    "Insufficient input is provided to a Search command.");
            return null;
        }

        Parameters param;
        if (null == (param = getParameters(instructions,0)))
        {
            return null;
        }

        if (instructions.size()-1 > param.index)
        {
            ErrorHandler.logError("Failure interpreting script: " +
                    "There is a syntax error within a Search command.");
            return null;
        }

        return new Search(param.attributeType,param.dataID,param.regex);
    }

    /**
     * This function will generate a new Sort analysis task. The Sort constructor that is
     *      used will depend on the length of the instructions list. Input and syntax are verified.
     *      Validity of the sort direction argument is not verified.
     *
     * @param instructions  a list of String input to be parsed into parameters for the Sort
     *
     * @return              Sort instance
     */
    private AnalysisTask parseSortInstruction(List<String> instructions)
    {
        AnalysisTask task;
        LogEvent.AttributeType attributeType;
        DataID dataID = null;
        String directionString = "";
        // in a refactored version, the conversion from string to bool might happen in Sort
        Boolean directionBool = true; // default

        // check that the instruction list is not empty before interpretation
        if (instructions.isEmpty())
        {
            ErrorHandler.logError("Failure interpreting script: " +
                    "Insufficient input is provided to a Sort command.");
            return null;
        }
        int index = 0;
        // valid instructions begin with the attribute type to sort by
        if (null == (attributeType = getAttributeType(instructions.get(index))))
        {
            // ErrorHandler already sent message
            return null;
        }
        index++;

        // if the attribute type to sort by is the data value of a DataID,
        // the DataID name will be provided in the script. The provided name will need to
        // be checked for validity against the list in Configuration.
        if (LogEvent.AttributeType.DATAVALUE == attributeType)
        {
            // when the AttributeType is DATAVALUE, a valid DataID name must follow
            if (instructions.size()-1 == index)
            {
                ErrorHandler.logError("Failure interpreting script: " +
                        "There is a syntax error within a Sort command: missing arguments.");
                return null;
            }

            if (null == (dataID = getDataID(instructions.get(index))))
            {
                // ErrorHandler already sent message
                return null;
            }
            index++;
        }

        if (instructions.size()-1 == index)
        {
            directionString = instructions.get(index);
        }

        // in a refactoring of Loafr, this functionality might happen in the Sort class
        if (!directionString.isBlank())
        {
            if (null == (directionBool = parseSortingDirection(directionString)))
            {
                ErrorHandler.logError("Failure interpreting script: " +
                        "There is a syntax error within a Sort command: invalid sort direction." +
                        "\nUsage: sort <attribute type> <optional: data element> <direction: ascending | descending");
                return null;
            }
        }

        if (instructions.size()-1 > index)
        {
            ErrorHandler.logError("Failure interpreting script: " +
                    "There is a syntax error within a Sort command: too many arguments.");
            return null;
        }

        return new Sort(attributeType,dataID,directionBool);
    }

    /**
     * This function will generate a new Filter analysis task. Input and syntax are verified.
     *      Regular expression syntax is not verified.
     *
     * @param instructions  a list of String input to be parsed into parameters for the Filter
     *
     * @return              Filter instance
     */
    private AnalysisTask parseFilterInstruction(List<String> instructions)
    {
       // Ex: filter <attribute type> /regex/ RANGE <attribute type> /regex/ <attribute type> /regex/
        Filter filter;
        LogEvent.AttributeType attributeType = null;
        DataID dataID = null;
        String regex = null;

        // check that the instruction list is not empty before interpretation
        if (instructions.isEmpty())
        {
            ErrorHandler.logError("Failure interpreting script: " +
                    "Insufficient input is provided to a Filter command.");
            return null;
        }

        Parameters param1 = getParameters(instructions,0);
        filter = new Filter(param1.attributeType,param1.dataID,param1.regex);

        if (param1.index < instructions.size()-1)
        {
            if (instructions.get(param1.index+1).equalsIgnoreCase("range"))
            {

                Parameters param2 = getParameters(instructions,param1.index+2);
                Parameters param3 = getParameters(instructions,param2.index+1);
                Range range = new Range(param2.attributeType,param2.dataID,param2.regex,
                                        param3.attributeType,param3.dataID,param3.regex);
                filter.setRange(range);
            }
        }

        return filter;
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
        // if the regex is empty, log an error and return null
        if (regex.isBlank())
        {
            ErrorHandler.logError("Failure interpreting script: " +
                    "No arguments were provided for a Search command.");
            return null;
        }

        // remove regex delimiters, if present
        if (regex.charAt(0) == '/' && regex.charAt(regex.length()-1) == '/')
        {
            regex = regex.substring(1,regex.length()-1).strip();
            // check again for empty regex
            if (regex.isBlank())
            {
                ErrorHandler.logError("Failure interpreting script: " +
                        "No arguments were provided for a Search command.");
                return null;
            }
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
                    "There is a syntax error or unrecognized argument within a Search command.");
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
            ErrorHandler.logError("Failure interpreting script: " +
                    "There is a syntax error or unrecognized argument within a Search command.");
            return null;
        }
        return match;
    }

    private Boolean parseSortingDirection(String direction)
    {
        Boolean bool = null;
        if (direction.equalsIgnoreCase("ascending"))
        {
            bool = true;
        }
        else if (direction.equalsIgnoreCase("descending"))
        {
            bool = false;
        }
        return bool;
    }

    /**
     * This function will collect the attribute type, DataID type, and regular expression
     * provided in a search or filter instruction and store the information in a Parameters object.
     * The only field the the instruction must contain is the regular expression.
     * The current index within the instructions list (the index of the regular expression)
     * is saved in the Parameters object.
     *
     * @param instructions  the list of instructions that represents a line of a script
     * @param index         the index in the list of instructions that should be interpreted next
     * @return              a Parameters object
     */
    private Parameters getParameters(List<String> instructions, int index)
    {
        Parameters param = new Parameters();

        if (instructions.size() <= index)
        {
            ErrorHandler.logError("Failure interpreting script: " +
                    "There is insufficient input in a "+taskKeyWord+" command.");
            return null;
        }

        if (null != (param.attributeType = getAttributeType(instructions.get(index))))
        {
            // if the first argument is not an attribute type, it is expected to be a regular expression
            index++;
            if (LogEvent.AttributeType.DATAVALUE == param.attributeType)
            {
                if (instructions.size() <= index)
                {
                    ErrorHandler.logError("Failure interpreting script: " +
                            "There is a syntax error within a "+taskKeyWord+" command.");
                    return null;
                }

                if (null == (param.dataID = getDataID(instructions.get(index))))
                {
                    // ErrorHandler already sent message
                    return null;
                }
                index++;
            }
        }

        if (instructions.size() <= index)
        {
            ErrorHandler.logError("Failure interpreting script: " +
                    "There is a syntax error within a "+taskKeyWord+" command.");
            return null;
        }
        if (null == (param.regex = getRegEx(instructions.get(index))))
        {
            return null;
        }

        param.index = index;
        return param;
    }

    /**
     * This nested class is a data structure that enables all of the
     * class attributes to be returned from a single function.
     */
    private class Parameters
    {
        protected LogEvent.AttributeType attributeType = null;
        protected DataID dataID = null;
        protected String regex = null;
        protected int index = 0;
    }
}
