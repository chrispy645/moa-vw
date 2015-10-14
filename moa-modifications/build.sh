#!/bin/bash

if [ -z "$MOA_HOME" ]; then
  echo "Error! Make sure environment variable MOA_HOME is set to the directory that contains moa.jar"
else
  javac -cp $MOA_HOME/moa.jar src/main/java/moa/classifiers/*.java src/main/java/moa/tasks/*.java
fi
