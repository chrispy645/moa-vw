#!/bin/bash

# problem: need to do several passes through the data??

DATASET=iris_2class_shuf
NUM_PASSES=1

# train
vw --bfgs --loss_function logistic --link logistic --passes $NUM_PASSES -c --holdout_off ../datasets/$DATASET.txt -f /tmp/$DATASET.model

# predict (do no learning with -t)
echo "** TESTING ... **"
vw -i /tmp/$DATASET.model -t ../datasets/$DATASET.txt -p /dev/stdout --quiet
