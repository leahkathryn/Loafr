# Loafr v0.2

## Description
This implementation of Loafr is capable of searching a log file for a specific attribute and sorting log file entries by a specific attribute. This version of Loafr also supports batch processing.

### Searching
A search query in a Loafr script will have this format: search \<optional attribute type\> \</regular expression/\>  

If an attribute type is supplied, the regular expression will be matched against only that attribute within each log entry.  
If an attribute type is not supplied, the regular expression will be matched against every attribute type within each log entry.  
If an attribute within a log entry matches the regular expression, the log entry will appear in the output file.   

Attribute types:  
- timeStamp: YYYY-MM-DD HH:MM:SS
- event: the name of the event  
    example: Pre-Launch_Check
- dataID: the name of the data point  
    example: Battery_Level
- dataType: primitive data type of a data point  
    When the dataType attribute type is specified, all log entries with a data point with a matching data type will appear in the output file.
- dataValue: the value of a data point  
    When the dataValue attribute type is specified, the name of a data point must be supplied that is an exact match to a defined data identifier.  
### Regular Expressions  
At this time, Loafr accepts regular expression patterns that follow the syntax defined in the [Java Pattern Class Documentation](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html). The Pattern engine is similar to the pattern matching in Perl 5, and the link describes the differences. 

### Batch Processing  
Batch processing in Loafr v0.2 is an opportunity to execute multiple scripts on multiple log files with a single Loafr command. It is possible to run multiple scripts on a single log file, a single script on multiple log files, or multiple scripts on multiple log files. It is also possible to merge all input log files into a single log file for processing. Log files can be merged by log entry, so that all log entries from all log files are intermixed and ordered chronologically; the keyword for this option is "event". Alternatively, the log files can be merged while preserving the order of log entries within the original log file; the keyword for this option is "log". In this case, all log entries from the first log file that was processed will precede all log entries from the second log file that was processed, and so on.   
The batch processing output will be written into a new directory that can be specified by the user in the initial Loafr start-up command with the "-o" flag, or if an output location is not provied the output will be written to the default output location specified in the configuration file. Each individual output file will be named with this format "log_file_name_script_file_name". If the log files have been merged, the output file will be named with this format "merged_log_data_script_file_name".


## Instructions to build Loafr
1. In the terminal, navigate to Loafr directory
2. In the terminal, run: gradle build  
This will automatically run all unit tests.

To view the test results:  
1. Navigate to Loafr/build/reports/tests/test
2. Launch index.html in any browser
3. View results

## Instructions to run End-to-End Search Scenario  
1. In the terminal, navigate to Loafr directory
2. In the terminal, run: gradle test --tests com.control.SearchScenarioTest  
3. Navigate to Loafr/build/reports/tests/test
4. Launch index.html in any browser
5. View results

## Instructions to run End-to-End Batch Processing Scenario
1. In the terminal, navigate to Loafr directory
2. In the terminal, run: gradle test --tests com.control.BatchScenarioTest
3. Navigate to Loafr/build/reports/tests/test
4. Launch index.html in any browser
5. View results  
This test case merges 3 log files chronologically by log entries, and then searches the log entries for an event type. The output contains log entries of the specified event type from all input log files, ordered chronologically. 

## Instructions to run Loafr    
1. After building Loafr, navigate to Loafr/build/libs/
2. The Loafr executable JAR: Loafr-Loafr-0.2.jar
3. The JAR can run from any directory.
4. To run the JAR: java -jar Loafr-Loafr-0.2.jar \<arguments - instructions below\>  

### Loafr Command Line Arguments  
To run a script on a log file and the output will be written to your current working directory:  
-s \<script location\> -l \<log file location\>  
To run a script on a log file and the output will be written to the specified location:  
-s \<script location\> -l \<log file location\> -o \<output file location\>  
    If this option is used, either a file name for the output file in the current working directory can be provided, or an absolute path to a different file location can be provided.  
The flags -s (script) and -l (log file), and corresponding values, are required. The flag -o (output file) and corresponding value is optional.  
Batch processing with Loafr v0.2:  
To run multiple scripts on multiple log files and generate (number of scripts) x (number of log files) output files:  
-b -s \<script location1\> \<script location2\> ... -l \<log file location1\> \<log file location2\> ...
To run a single script on multiple log files and merge the log files:  
-b -s \<script location1\> -l \<log file location1\> \<log file location2\> -m \<event | log\>

**Loafr Resources**  
Sample scripts and log files are available in Loafr/src/main/resources  
Scripts:     
    Loafr/src/main/resources/search_script.txt  
    Loafr/src/main/resources/sort_script.txt    
Log files:   
    Loafr/src/main/resources/log_file_10_events.txt  
    Loafr/src/main/resources/log_file_20_events.txt

These are the same log file and script that are used in the End-to-End Search Scenario.


