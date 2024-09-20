#!/bin/bash
set -u -e  
# Compile the Java files
javac Main.java Splash.java
# Run the program
java Main