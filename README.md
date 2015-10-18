# MoaVW

"Vowpal Wabbit" (an Elmer Fudd inspired malapropism of "vorpal rabbit"), is a machine learning library designed for online 
learning on large datasets. It can deal with an extremely large feature space by utilising the "hashing trick", where feature
names are mapped to indices of a fixed-size weight vector. This package is a wrapper that allows users to invoke Vowpal Wabbit
from MOA. This makes it easily accessible for researchers that use MOA to evaluate its performance in conjunction with other
algorithms or datasets.

## Installation

This wrapper requires the following:
* [MOA](http://sourceforge.net/projects/moa-datastream/files/MOA/2014%20November/moa-release-2014%20.11.zip/download).
  This is the 2014.11 release - this wrapper has not been yet tested on the latest pre-release version of MOA.
  Ensure that the environment variable `MOA_HOME` is set and that it contains `moa.jar`.
* Java 8, but 7 might work as well. Make sure `JAVA_HOME` is set as well.
* [ant](http://ant.apache.org/) to be able to build the package.
* Various programs required to compile VW like GCC and Boost. See the README file [here](https://github.com/JohnLangford/vowpal_wabbit).
  

Currently, this wrapper only supports a somewhat recent version of VW. The script `build_vw.sh` (in this directory) will attempt
to clone the supported version of [vowpal-wabbit](https://github.com/JohnLangford/vowpal_wabbit), and build the JNI library
required for this wrapper to interface with VW. This repository does currently come with a pre-built JNI library
(located in `vw_test/lib`) but this was compiled on my OS platform (Mac OS X).

Once `build_vw.sh` has been run, execute `build.sh`, which will build the .jar file for the wrapper. Then, all you need to do
is execute `moa.sh`.

## Usage

The classifier, `VWSimple`, can be found under `moa.classifiers.functions`. It implements a subset of the features offered by
VW - partly because some features are a bit more complicated or that they only make sense in the context of multiple passes
through the dataset or VW's native format.

![](https://raw.githubusercontent.com/chrispy645/moa-vw/master/images/gui.png)

## Caveats

* Prequential evaluation is currently only supported with `VWSimple`. The reason for this is that the VW object in Java needs
to be gracefully shut down, and this is only implemented in the prequential evaluation class.

* Due to the ARFF format, this wrapper is probably not ideal for extremely large feature spaces. The wrapper does have a "meta" option but this is a bit of a hack, and the pre-release version of MOA might already have something that is better.
