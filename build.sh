#!/bin/bash

ant clean
#ant -noclasspath -lib $MOA_EXP_HOME/moa.jar:$WEKA_HOME/weka.jar exejar -Dpackage=moa-vw
cd moa-modifications
./build.sh
cd ..
ant -noclasspath -lib moa-modifications/src/main/java/:$MOA_HOME/moa.jar:$WEKA_HOME/weka.jar exejar -Dpackage=moa-vw
