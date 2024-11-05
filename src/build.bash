#!/bin/bash
set -u -e  
# Compile the Java files
javac Main.java  PlayerScreen.java Splash.java PlayerService.java UDPTransmit.java UDPReceive.java ImageCountdown.java Music.java
# Run the program
java -cp "../lib/postgresql-42.7.4.jar":. Main
