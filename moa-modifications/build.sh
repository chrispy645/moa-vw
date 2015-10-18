#!/bin/bash

if [ -z "$MOA_HOME" ]; then
  echo "Error! Make sure environment variable MOA_HOME is set to the directory that contains moa.jar"
else
  javac -Xlint:deprecation -cp $MOA_HOME/moa.jar \
	src/main/java/moa/classifiers/*.java src/main/java/moa/tasks/*.java src/main/java/moa/streams/*.java
fi
