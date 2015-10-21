#!/bin/bash

if [ -z "$MOA_HOME" ]; then
	echo "MOA_HOME env variable is not set!"
	exit 1
fi

ant clean
cd moa-modifications
./build.sh
cd ..
ant -noclasspath -lib moa-modifications/src/main/java/:$MOA_HOME/moa.jar:$WEKA_HOME/weka.jar exejar -Dpackage=moa-vw
