OSS-1314
========

To run this project the JVM needs some long arguments.
To make this easier, we have provided a script that starts up the project: `run.sh`.
This will execute our project on a test project.
The script uses the `java` command. Because our project requires Java 7, this `java` command must point to Java 7.

To use our daemon on a different project, use the following command (with the current directory being the root folder, which contains the junit-r4.11 directory):
```
java -classpath junit-r4.11/bin/:junit-r4.11/lib/hamcrest-core-1.3.jar -javaagent:junit-r4.11/lib/ossrewriter-1.0.jar kuleuven.group6.Daemon <testSuiteClass> <sourceBinDirectory> <testBinDirectory>
```

Compiling the project with Eclipse
----------------------------------

The project folder is `junit-r4.11`. This project requires Java 7, which must be specified on the project.

Our project includes a test project, which is used to test the daemon.
This sample project requires a few settings.

Two folders need to be set as source folder:
- src/main/java/kuleuven/group6/tests/testsubject/source  `(1)`
- src/main/java/kuleuven/group6/tests/testsubject/tests  `(2)`

These also need their own bin folder. To do this, configure the following:
- Go to the `Java Build Path` tab in the settings of the project.
- Under libraries, add the following:
  - junit-r4.11/lib/hamcrest-core-1.3.jar
  - junit-r4.11/lib/ossrewriter-1.0.jar
- Under source:
  - Check `Allow output folders for source folders`
  - To use the provided `run.sh` script, use the following output folders: 
    - For the source folder (1), specify the output folder: `junit-r4.11/testsubjectbin/source`
    - For the tests folder (2), specify the output folder: `junit-r4.11/testsubjectbin/tests`
    
Report and class diagram
------------------------
Our report and the full class diagram is located in this directory. 
They are `Report group 6.pdf` and `ClassDiagram.png`, respectively.
