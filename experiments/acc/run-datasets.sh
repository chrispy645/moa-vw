#!/bin/bash

mkdir output-datasets

TOTAL_INSTANCES=1000000
MEM_INT=1000
SAMPLE_INT=1000


declare -a configs=(
    "EvaluatePrequential -l (functions.SGDMultiClass -r 0.5) -s (generators.AgrawalGenerator) -i $TOTAL_INSTANCES -f $SAMPLE_INT -f $MEM_INT"
    "EvaluatePrequential -l (functions.SGDMultiClass -r 0.5) -s (generators.HyperplaneGenerator) -i $TOTAL_INSTANCES -f $SAMPLE_INT -f $MEM_INT"
    "EvaluatePrequential -l (functions.SGDMultiClass -r 0.5) -s (generators.LEDGenerator) -i $TOTAL_INSTANCES -f $SAMPLE_INT -f $MEM_INT"
    "EvaluatePrequential -l (functions.SGDMultiClass -r 0.5) -s (generators.RandomRBFGenerator) -i $TOTAL_INSTANCES -f $SAMPLE_INT -f $MEM_INT"
    "EvaluatePrequential -l (functions.SGDMultiClass -r 0.5) -s (generators.RandomTreeGenerator) -i $TOTAL_INSTANCES -f $SAMPLE_INT -f $MEM_INT"
    "EvaluatePrequential -l (functions.SGDMultiClass -r 0.5) -s (generators.SEAGenerator) -i $TOTAL_INSTANCES -f $SAMPLE_INT -f $MEM_INT"
    "EvaluatePrequential -l (functions.SGDMultiClass -r 0.5) -s (generators.STAGGERGenerator) -i $TOTAL_INSTANCES -f $SAMPLE_INT -f $MEM_INT"
    "EvaluatePrequential -l (functions.SGDMultiClass -r 0.5) -s (generators.WaveformGenerator) -i $TOTAL_INSTANCES -f $SAMPLE_INT -f $MEM_INT"
)

declare -a outputs=(
    "output-datasets/sgd_agrawal.txt"
    "output-datasets/sgd_hyperplane.txt"
    "output-datasets/sgd_led.txt"
    "output-datasets/sgd_rbf.txt"
    "output-datasets/sgd_rtg.txt"
    "output-datasets/sgd_sea.txt"
    "output-datasets/sgd_stagger.txt"
    "output-datasets/sgd_waveform.txt"
)

for i in {0..7}; do
    java -Xmx6g -cp ../../moa-modifications/src/main/java/:../../dist/moa-vw.jar:$MOA_HOME/moa.jar \
    -javaagent:$MOA_HOME/sizeofag.jar moa.DoTask \
        "${configs[$i]}" > "${outputs[$i]}"
done

for NUM_BITS in 3 6 9 12 15 18; do
    declare -a configs=(
        "EvaluatePrequential -l (functions.VWSimple -L hinge -b $NUM_BITS -M oaa) -s (generators.AgrawalGenerator) -i $TOTAL_INSTANCES -f $SAMPLE_INT -f $MEM_INT"
        "EvaluatePrequential -l (functions.VWSimple -L hinge -b $NUM_BITS -M oaa) -s (generators.HyperplaneGenerator) -i $TOTAL_INSTANCES -f $SAMPLE_INT -f $MEM_INT"
        "EvaluatePrequential -l (functions.VWSimple -L hinge -b $NUM_BITS -M oaa) -s (generators.LEDGenerator) -i $TOTAL_INSTANCES -f $SAMPLE_INT -f $MEM_INT"
        "EvaluatePrequential -l (functions.VWSimple -L hinge -b $NUM_BITS -M oaa) -s (generators.RandomRBFGenerator) -i $TOTAL_INSTANCES -f $SAMPLE_INT -f $MEM_INT"
        "EvaluatePrequential -l (functions.VWSimple -L hinge -b $NUM_BITS -M oaa) -s (generators.RandomTreeGenerator) -i $TOTAL_INSTANCES -f $SAMPLE_INT -f $MEM_INT"
        "EvaluatePrequential -l (functions.VWSimple -L hinge -b $NUM_BITS -M oaa) -s (generators.SEAGenerator) -i $TOTAL_INSTANCES -f $SAMPLE_INT -f $MEM_INT"
        "EvaluatePrequential -l (functions.VWSimple -L hinge -b $NUM_BITS -M oaa) -s (generators.STAGGERGenerator) -i $TOTAL_INSTANCES -f $SAMPLE_INT -f $MEM_INT"
        "EvaluatePrequential -l (functions.VWSimple -L hinge -b $NUM_BITS -M oaa) -s (generators.WaveformGenerator) -i $TOTAL_INSTANCES -f $SAMPLE_INT -f $MEM_INT"
    )
    declare -a outputs=(
        "output-datasets/vw_${NUM_BITS}_oaa_agrawal.txt"
        "output-datasets/vw_${NUM_BITS}_oaa_hyperplane.txt"
        "output-datasets/vw_${NUM_BITS}_oaa_led.txt"
        "output-datasets/vw_${NUM_BITS}_oaa_rbf.txt"
        "output-datasets/vw_${NUM_BITS}_oaa_rtg.txt"
        "output-datasets/vw_${NUM_BITS}_oaa_sea.txt"
        "output-datasets/vw_${NUM_BITS}_oaa_stagger.txt"
        "output-datasets/vw_${NUM_BITS}_oaa_waveform.txt"
    )
    for i in {0..7}; do
        java -Xmx6g -cp ../../moa-modifications/src/main/java/:../../dist/moa-vw.jar:$MOA_HOME/moa.jar \
        -javaagent:$MOA_HOME/sizeofag.jar moa.DoTask \
            "${configs[$i]}" > "${outputs[$i]}"
    done
done
