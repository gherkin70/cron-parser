### Cron Expression Parser

#### To run in a terminal:

1. Install Java jdk (minimum version 8)
   1. Ensure JAVA_HOME environment variable is set and pointing to root Java directory
2. Install Kotlin command line compiler:
   1. https://kotlinlang.org/docs/command-line.html
3. From the root directory
   1. compile into a jar with this command:
   
    `kotlinc src/main/kotlin/org/cron -include-runtime -d Application.jar`
   2. Run with this command, passing a valid cron expression as an argument:

   `java -jar Application.jar "* * * * * /usr/bin/find"`

#### Import and run in Intellij:
1. Install Java jdk (minimum version 8)
2. Download IntelliJ: https://www.jetbrains.com/idea/download
3. Open existing project in IntelliJ (as gradle project) and select cron-parser 
   1. In project structure, ensure the SDK is set to the root directory of the Java jdk
4. In Application.kt select play -> modify run configuration -> add cron expression in 'program arguments' -> run ApplicationKt