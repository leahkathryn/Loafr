# Loafr

## Description
This implementation of Loafr is capable of searching a log file for a specific attribute. The log entries that match the attribute will be written into an output file. 

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
2. In the terminal, run:  
       Mac / Linux: ./gradlew test --tests com.control.SearchScenarioTest  
       Windows: gradlew.bat test --tests "com.control.SearchScenarioTest"  
3. Navigate to Loafr/build/reports/tests/test
4. Launch index.html in any browser
5. View results

## Instructions to run Loafr    
1. After building Loafr, navigate to Loafr/build/libs/
2. The Loafr executable JAR: Loafr-Loafr-0.1.jar
3. The JAR can run from any directory.
4. To run the JAR: java -jar Loafr-Loafr-0.1.jar \<arguments - instructions below\>  

### Loafr Command Line Arguments  
To run a script on a log file and the output will be written to your current working directory:  
-s \<script location\> -l \<log file location\>  
To run a script on a log file and the output will be written to the specified location:  
-s \<script location\> -l \<log file location\> -o \<output file location\>  
    If this option is used, either a file name for the output file in the current working directory can be provided, or an absolute path to a different file location can be provided.  
The flags -s (script) and -l (log file), and corresponding values, are required. The flag -o (output file) and corresponding value is optional.  
Batch processing is not available in the current version (0.1) of Loafr.  

**Loafr Resources**  
A sample script and log file are available in Loafr/src/main/resources  
Script: Loafr/src/main/resources/test_script.txt  
Log file: Loafr/src/main/resources/test_log_file.txt

These are the same log file and script that are used in the End-to-End Search Scenario.


