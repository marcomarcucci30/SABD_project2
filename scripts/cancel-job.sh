#!/bin/bash
JOB_ID=$(flink list | grep Queries |sed 's/: /\n/g' | sed -n 2p)
export JOB_ID
flink cancel "$JOB_ID"