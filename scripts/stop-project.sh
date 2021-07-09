#!/bin/bash
sh cancel-job.sh
sh delete-files.sh
cd ..
cd docker || return
docker-compose down
cd .. || return
stop-cluster.sh