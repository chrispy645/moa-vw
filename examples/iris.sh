#!/bin/bash

# train a model (uses prequential eval??)
vw -d ../datasets/iris.txt --ect 3 -f iris.model --holdout_off -c -k --passes 20

# make predictions
# -i = model, -t = do no learning
vw -d ../datasets/iris.txt -t -i iris.model -p iris.preds --quiet


