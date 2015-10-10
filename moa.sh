#!/bin/bash

MOA_EXP_HOME=~/github/moa-2014.11-SNAPSHOT-sources/dist/

java -Xmx8g -cp $MOA_EXP_HOME/moa.jar:dist/moa-vw.jar:$WEKA_HOME/weka.jar \
	-javaagent:$MOA_HOME/sizeofag.jar moa.gui.GUI
