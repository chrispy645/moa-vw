#!/bin/bash

mkdir output-time-memory

TOTAL_INSTANCES=1000000
MEM_INT=100
SAMPLE_INT=100

for iter in {1..5}; do
    declare -a configs=(
        "EvaluatePrequential -l functions.VWSimple -s (ArffFileStream -f m.arff) -i $TOTAL_INSTANCES -f $SAMPLE_INT -q $MEM_INT"
        "EvaluatePrequential -l functions.VWSimple -s (ArffFileStream -f m_sparse.arff) -i $TOTAL_INSTANCES -f $SAMPLE_INT -q $MEM_INT"
        "EvaluatePrequential -l (functions.VWSimple -m) -s (MetaArffFileStream -f m_meta.arff) -i $TOTAL_INSTANCES -f $SAMPLE_INT -q $MEM_INT"
    )
    declare -a outputs=(
        "output-time-memory/m.txt.${iter}"
        "output-time-memory/m_sparse.txt.${iter}"
        "output-time-memory/m_meta.txt.${iter}"
    )
    for i in {0..2}; do
        java -Xmx6g -cp ../../moa-modifications/src/main/java/:../..//dist/moa-vw.jar:$MOA_HOME/moa.jar:$WEKA_HOME/weka.jar \
        -javaagent:$MOA_HOME/sizeofag.jar moa.DoTask \
            "${configs[$i]}" > "${outputs[$i]}"
    done
done
