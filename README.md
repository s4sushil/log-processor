# log-processor
Batch process custom log file and generate Alerts in Database

The program:

Takes the input file path as input argument. If not found than it process the default file in the classpath.

Flag any long events that take longer than 4ms with a column in the database called "alert" and Writes the found event in HSQL

Program can handle very large files due to TaskExecutor implementation.

To run - install Gradle and go till the project path and run command : gradle bootRun -Pargs=$fileName

example run -> gradle bootRun -Pargs=/Users/sushilchoudhary/Downloads/test.log

if the file is not passed than it will run with default file.

example run -> gradle bootRun