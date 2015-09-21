#!/bin/bash

# vw --ect 3 -c -k --nn 30 --passes 10 --holdout_off    train.vw -f train.model.vw && ls -l train.model.vw

# train
vw --ect 3 -c -k --nn 30 --passes 10 --holdout_off ../datasets/iris.txt -f /tmp/iris.model
# predict (do no learning with -t)
vw -i /tmp/iris.model -t ../datasets/iris.txt -p /dev/stdout --quiet | less
# remove cache
rm ../datasets/iris.txt.cache
