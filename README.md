# Loafr

## Instructions to run:    
Update the configurationFileLoc attribute in the Loafr class to match the location on your system  
Update the default output location in the configuration file (in test/resources) to match the location on your system  
**Before we turn in the project we will make these steps unnecessary**  

On the command line in your Loafr directory "gradle build"  
Look in Loafr/build/libs/ and you will see our JAR: Loafr-Loafr-0.1  
You can copy the JAR where ever you want or run from this directory  
To run: java -jar Loafr-Loafr-0.1.jar -s \<test script location> -l \<log file location>  
To make it easy I just copied and pasted the script and log file to my C:\  
Note: log_file_1 and log_file_3 seem to work but I haven't checked log_file_2. If you get an error from running the program, check the log file against the configuration file first to see if the log file has an error. 

Helpful links to learn how we can organize our unit tests: 
https://www.baeldung.com/java-junit-test-suite
https://docs.gradle.org/current/userguide/java_testing.html
