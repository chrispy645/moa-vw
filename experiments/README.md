## Experiments

To reproduce the experiments run on the data generators, as presented in the provided tech report, run the script `run-datasets.sh`. Make sure that it is run from this directory, and that the environment variable `$MOA_HOME` is set to the directory where `moa.jar` is located. When this has completed, run `RScript plot-datasets.R` (requires R to be installed) and this will output a .pdf in the directory with the learning curves.
