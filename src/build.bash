#!/bin/bash
set -u -e  
# Compile the Java files
javac -cp "lib/postgresql-42.7.4.jar" Main.java  playerScreen.java PSQLConn.java Splash.java PlayerService.java
# Run the program
java -cp "lib/postgresql-42.7.4.jar" Main