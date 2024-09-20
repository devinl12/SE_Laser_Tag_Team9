#!/bin/bash
set -u -e  
# Compile the Java files
javac -cp "lib/postgresql-42.7.4.jar" *.java
# Run the program
java -cp "lib/postgresql-42.7.4.jar" Main