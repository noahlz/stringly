#! /bin/sh 
rev=`git rev-parse --short HEAD`
echo "Running benchmark against local HEAD ($rev)"
set -x
lein perf | tee bench-$rev.log
