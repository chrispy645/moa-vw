#!/bin/bash

# train
vw --loss_function logistic --link logistic --holdout_off ../datasets/iris_2class.txt --invert_hash /tmp/iris.model
cat /tmp/iris.model | less
