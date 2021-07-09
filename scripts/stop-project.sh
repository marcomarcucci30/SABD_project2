#!/bin/bash
sh cancel-job.sh
kill "$(cat ../flink-config/pid_values)"
sh delete-files.sh
cd ..
cd docker || return
docker-compose down
cd .. || return
stop-cluster.sh