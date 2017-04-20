#!/bin/bash
javac -classpath vlcj-3.8.0/jna-3.5.2.jar:vlcj-3.8.0/platform-3.5.2.jar:vlcj-3.8.0/slf4j-api-1.7.10.jar:vlcj-3.8.0/vlcj-3.8.0.jar *.java
echo "Running..."
java -classpath .;vlcj-3.8.0/jna-3.5.2.jar:vlcj-3.8.0/platform-3.5.2.jar:vlcj-3.8.0/slf4j-api-1.7.10.jar:vlcj-3.8.0/vlcj-3.8.0.jar Main


