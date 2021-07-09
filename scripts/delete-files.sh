#!/bin/bash
cd ..
rm -r Results/*
rm -r flink-config/pid-values
echo "Files deleted!"
cd scripts || exit