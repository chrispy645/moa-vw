#!/bin/bash

# train a model (uses prequential eval??)
vw -d ../datasets/diabetes_numeric.txt -f diabetes.model

# make predictions
# -i = model, -t = do no learning
vw -d ../datasets/diabetes_numeric.txt -t -i diabetes.model -p diabetes.preds --quiet

