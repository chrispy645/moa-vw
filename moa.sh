#!/bin/bash

java -Xmx8g -cp moa-modifications/src/main/java/:dist/moa-vw.jar:$MOA_HOME/moa.jar:$WEKA_HOME/weka.jar \
   -javaagent:$MOA_HOME/sizeofag.jar moa.gui.GUI

#java -Xmx8g -cp $MOA_EXP_HOME/moa.jar:dist/moa-vw.jar:$WEKA_HOME/weka.jar \
#	-javaagent:$MOA_HOME/sizeofag.jar moa.gui.GUI
