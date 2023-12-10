package com.control;

import com.ErrorHandler;
import com.input.Configuration;
import com.input.LogData;
import com.input.LogEvent;
import com.interpret.Interpreter;
import com.interpret.Script;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.nio.file.Path;

/**
 * This implementation of the Controller class will be used when Loafr
 *      is provided the batch processing flag and any number of script files and log files.
 *      The script files will be parsed and interpreted. The log files will be parsed.
 *      If a merge instruction is provided, the log file data will be merged and processed
 *      to create a single output file per script. The output location provided will
 *      be used to create a new directory. The output from the script
 *      execution will write itself to this directory, each output file named as
 *      "log_file_name_script_file_name".
 *
 * @author Leah Lehmeier
 */
public class BatchScriptController implements Controller
{
    private Configuration configuration;
    private String merge;
    private List<String> logFileLoc;
    private List<LogData> logDataList;
    private List<String> scriptFileLoc;
    private List<Script> scriptList;
    private Interpreter interpreter;
    private String outputFileLoc;

    public BatchScriptController(Configuration configuration, String flag, List<String> logFileLoc, List<String> scriptFileLoc, String outputFileLoc)
    {
        this.configuration = configuration;
        this.merge = flag;
        this.logFileLoc = logFileLoc;
        this.scriptFileLoc = scriptFileLoc;
        this.outputFileLoc = outputFileLoc;
    }

    @Override
    public String getOutputFileLoc()
    {
        return outputFileLoc;
    }

    @Override
    public boolean execute()
    {
        // create interpreter for this Loafr execution
        interpreter = new Interpreter(configuration);
        scriptList = new ArrayList<>();
        logDataList = new ArrayList<>();

        // parse the script files into Script instances
        List<String> processedScripts = new ArrayList<>();
        if(!processScripts(processedScripts))
        {
            return false;
        }

        // parse the log files into LogData objects
        List<String> processedLogFiles = new ArrayList<>();
        if(!processLogFiles(processedLogFiles))
        {
            return false;
        }

        // if there is a merge instruction, merge the LogData objects or error for an invalid instruction
        if (!merge.isBlank())
        {
            if (merge.equalsIgnoreCase("event"))
            {
                mergeByLogEvent();
            }
            else if (merge.equalsIgnoreCase("log"))
            {
                mergeByLogData();
            }
            else
            {
                ErrorHandler.logError("An invalid log file merge instruction has been provided.\nLoafr exiting...");
                return false;
            }
            processedLogFiles.clear();
            processedLogFiles.add("merged_log_data");
        }

        // execute script analyzers and write output to files
        if (!generateOutput(processedScripts,processedLogFiles))
        {
            return false;
        }

        return true;
    }

    @Override
    public void alertOutput(String message)
    {
        // temp implementation
        System.out.println(message);
    }

    /**
     * This function will initiate the parsing and interpretation of all script files in the
     * scriptFileLoc list. If a script file fails to parse or has invalid syntax, execution continues
     * with the next script file in the list. If all provided scripts are rejected, the function
     * returns false and Loafr execution will stop.
     *
     * @param processedScripts  a list of script file locations that were not rejected
     * @return                  boolean value indicating success or critical failure
     */
    private boolean processScripts(List<String> processedScripts)
    {
        int numSkippedScripts = 0;
        for (String scriptFile : scriptFileLoc)
        {
            Script script = new Script();
            // attempt script interpretation
            if(!script.interpretScript(scriptFile,interpreter))
            {
                numSkippedScripts++;
            }
            else
            {
                scriptList.add(script);
                processedScripts.add(scriptFile);
            }
        }
        if (scriptList.isEmpty())
        {
            ErrorHandler.logError("All provided scripts have been rejected.\nLoafr exiting...");
            return false;
        }
        if (numSkippedScripts > 0)
        {
            ErrorHandler.logError("Number of scripts rejected due to parsing or interpretation failure: " + numSkippedScripts);
        }
        return true;
    }

    /**
     * This function will initiate the parsing of all log files in the
     * logFileLoc list. If a log file fails to parse or has invalid syntax, execution continues
     * with the next log file in the list. If all provided log files are rejected, the function
     * returns false and Loafr execution will stop.
     *
     * @param processedLogFiles  a list of log file locations that were not rejected
     * @return                   boolean value indicating success or critical failure
     */
    private boolean processLogFiles(List<String> processedLogFiles)
    {
        int numSkippedLogFiles = 0;
        for (String logFile : logFileLoc)
        {
            LogData logData = new LogData();
            // attempt log file parsing
            if(!logData.parseLogFile(logFile,configuration))
            {
                numSkippedLogFiles++;
            }
            else
            {
                logDataList.add(logData);
                processedLogFiles.add(logFile);
            }
        }
        if (logDataList.isEmpty())
        {
            ErrorHandler.logError("All provided log files have been rejected.\nLoafr exiting...");
            return false;
        }
        if (numSkippedLogFiles > 0)
        {
            ErrorHandler.logError("Number of log files rejected due to parsing failure: " + numSkippedLogFiles);
        }
        return true;
    }

    /**
     * This function will first create an output directory for all output files to be written to.
     * The output directory name can be provided by the user, or the default file location from
     * the Loafr configuration will be used as the directory name.
     * Then the output file names are generated form the names of the log file and script that create
     * the analysis. The format is "log_file_name_script_file_name". If the log files have been merged,
     * the output file is name "merged_log_data_script_file_name".
     * Each Script in scriptList is executed on each LogData in logDataList, and each output LogData
     * will write itself into the output directory. If a Script fails to execute or an output LogData
     * fails to write itself to the output directory, execution will continue. If all output fails,
     * the function returns false and Loafr execution stops.
     *
     * @param processedLogFiles  a list of log file locations that are used to create output
     *                           file names
     * @param processedScripts   a list of script file locations are used to create output
     *                           file names.
     * @return                   boolean value indicating success or critical failure
     */
    private boolean generateOutput(List<String> processedScripts, List<String> processedLogFiles)
    {
        // create output directory
        /** If Loafr was real, the default file location could be used as the directory name **/
        if (outputFileLoc.equals("."))
        {
            outputFileLoc = "loafr_output";
        }
        /****************************************/
        File output = new File(outputFileLoc);
        if (!output.mkdir()) {
            ErrorHandler.logError("Unable to create output directory: " + outputFileLoc);
            return false;
        }

        // create list of output file names
        Path p;
        String logFileName;
        String scriptFileName;
        String fileName;
        List<String> outputFileNames = new ArrayList<>();
        for (String logFile : processedLogFiles)
        {
            logFileName = removeFileExtension(getFileName(logFile), true);
            for (String scriptFile : processedScripts)
            {
                scriptFileName = removeFileExtension(getFileName(scriptFile), true);
                fileName = logFileName + "_" + scriptFileName;
                outputFileNames.add(outputFileLoc + File.separator + fileName);
            }
        }

        int numFailedOutput = 0;
        int outputFileNameIndex = 0;
        LogData outputLogData;
        for (LogData logData : logDataList)
        {
            if (scriptList.size() > 1)
            {
                for (Script script : scriptList)
                {
                    outputLogData = script.executeAnalyzer(logData);
                    if (!outputLogData.writeLogData(outputFileNames.get(outputFileNameIndex),configuration))
                    {
                        numFailedOutput++;
                    }
                    outputFileNameIndex++;
                }
            }
            else
            {
                outputLogData = scriptList.get(0).executeAnalyzer(logData);
                if (!outputLogData.writeLogData(outputFileNames.get(outputFileNameIndex),configuration))
                {
                    numFailedOutput++;
                }
                outputFileNameIndex++;
            }
        }
        if (scriptList.size()*logDataList.size() == numFailedOutput)
        {
            ErrorHandler.logError("Failed to generate output files.\nLoafr exiting...");
            return false;
        }
        else if(numFailedOutput > 0)
        {
            ErrorHandler.logError("Failed to generate " + numFailedOutput + " output files out of " + scriptList.size()*logDataList.size() + " attempted.");
        }
        return true;
    }

    /**
     * This function will merge the LogData objects into a single LogData object.
     * The LogEvents of each individual LogData will be listed in the original ordering
     * from the original LogData. The LogEvents are listed in the same order as the LogData
     * were parsed from the log files: the LogEvents from the first parsed file will precede
     * all the events from the second parsed log file, and so on.
     */
    private void mergeByLogData()
    {
        // consider adding a way to separate the log data instances in the output file
        LogData mergedLogData = new LogData();
        for (LogData logData : logDataList)
        {
            for (LogEvent event : logData.getEventList())
            {
                mergedLogData.addLogEvent(event);
            }
        }
        logDataList.clear();
        logDataList.add(mergedLogData);
    }

    /**
     * This function will merge the LogData objects into a single LogData object.
     * The LogEvents of each individual LogData will be listed chronologically
     * according to their timestamps in the new LogData. The chronological
     * ordering happens in the insertLogEvent method.
     */
    private void mergeByLogEvent()
    {
        LogData mergedLogData = new LogData();
        for (LogData logData : logDataList)
        {
            for (LogEvent event : logData.getEventList())
            {
                insertLogEvent(event,mergedLogData);
            }
        }

        logDataList.clear();
        logDataList.add(mergedLogData);
    }

    /**
     * This function will insert a LogEvent into a list of LogEvents based on the
     * chronological ordering of the timestamp attribute.
     *
     * @param eventToInsert  the LogEvent being inserted
     * @param mergedLogData  the LogData object which has the LogEvent list to insert
     *                       the LogEvent into
     */
    private void insertLogEvent(LogEvent eventToInsert, LogData mergedLogData)
    {
        List<LogEvent> eventList = mergedLogData.getEventList();
        if (eventList.isEmpty())
        {
            eventList.add(eventToInsert);
            return;
        }

        for (int i = 0; i < eventList.size(); i++)
        {
            LogEvent event = eventList.get(i);

            if (eventToInsert.getTimeStamp().compareTo(eventList.get(i).getTimeStamp()) < 0
                || eventToInsert.getTimeStamp().compareTo(event.getTimeStamp()) == 0)
            {
                eventList.add(i,eventToInsert);
                return;
            }

            if (eventList.size()-1 == i)
            {
                eventList.add(eventToInsert);
                return;
            }

            if (eventToInsert.getTimeStamp().compareTo(event.getTimeStamp()) > 0)
            {
                continue;
            }
        }
    }

    /**
     * This function removes a file extension from a file name, if it exists.
     * https://www.baeldung.com/java-filename-without-extension
     *
     * @param fileName             the file name to remove an extension from
     * @param removeAllExtensions  a boolean value indicating whether or not to remove
     *                             all file extensions, or just the last file extension.
     * @return                     String file name without a file extension
     */
    private String removeFileExtension(String fileName, boolean removeAllExtensions) {
        if (fileName == null || fileName.isBlank()) {
            return fileName;
        }

        String extPattern = "(?<!^)[.]" + (removeAllExtensions ? ".*" : "[^.]*$");
        return fileName.replaceAll(extPattern, "");
    }

    /**
     * This function returns the file name at the end of a file path.
     *
     * @param path    a String that may represent an absolute path
     * @return        String file name without parent directories
     */
    private String getFileName(String path)
    {
        if (path == null || path.isBlank()) {
            return path;
        }
        String name = new File(path).getName();
        return name;
    }
}
