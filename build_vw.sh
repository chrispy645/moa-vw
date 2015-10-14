#!/bin/bash

pushd .

mkdir /tmp/vw_tmp
cd /tmp/vw_tmp
echo "cloning repo..."
git clone https://github.com/JohnLangford/vowpal_wabbit.git
cd vowpal_wabbit
echo "resetting to commit 3c2aa55e74d2f9b6ffeca7455f95775264b92bb7 (new version not supported yet)..."
git reset --hard 3c2aa55e74d2f9b6ffeca7455f95775264b92bb7
cd vw_test
echo "build JNI lib..."
make
cp target/vw_jni.lib

popd

echo "copying lib over to vw_test/target/"
cp /tmp/vw_tmp/vowpal_wabbit/java/target/vw_jni.lib vw_test/target/
