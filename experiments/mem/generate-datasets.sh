#!/bin/bash

if [ -z "$WEKA_HOME" ]; then
	echo "Ensure you have set the environment variable WEKA_HOME!"
	exit 1
fi

if [ -z "$MOA_HOME" ]; then
	echo "Ensure you have set the environment variable MOA_HOME!"
	exit 1
fi

echo "Generating massive ARFF..."
python generate-massive-arff.py > m.arff
echo "Generating sparse version..."
java -cp $WEKA_HOME/weka.jar -Xmx5g weka.filters.unsupervised.instance.NonSparseToSparse -i m.arff > m_sparse.arff
echo "Generating meta version..."
java -cp ../../dist/moa-vw.jar:$MOA_HOME/moa.jar:$WEKA_HOME/weka.jar moavw.ArffToVWArff m.arff > m_meta.arff
