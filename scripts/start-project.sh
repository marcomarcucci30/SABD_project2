#!/bin/bash
cd ..
mkdir Results
cd docker || return
docker-compose up -d
cd ..
cp flink-config/flink-conf.yaml "$FLINK_HOME/conf/flink-conf.yaml"
sleep 5
start-cluster.sh
kafka-topics.sh --create --topic query --zookeeper localhost:2181 --replication-factor 2 --partitions 1 > /dev/null

kafka-topics.sh --create --topic query1_monthly_output --zookeeper localhost:2181 --replication-factor 2 --partitions 1 > /dev/null
kafka-topics.sh --create --topic query1_weekly_output --zookeeper localhost:2181 --replication-factor 2 --partitions 1 > /dev/null

kafka-topics.sh --create --topic query2_monthly_output --zookeeper localhost:2181 --replication-factor 2 --partitions 1 > /dev/null
kafka-topics.sh --create --topic query2_weekly_output --zookeeper localhost:2181 --replication-factor 2 --partitions 1 > /dev/null

kafka-topics.sh --create --topic query3_one_hour_output --zookeeper localhost:2181 --replication-factor 2 --partitions 1 > /dev/null
kafka-topics.sh --create --topic query3_two_hour_output --zookeeper localhost:2181 --replication-factor 2 --partitions 1 > /dev/null

gnome-terminal --geometry=50x15+700+340 -- bash -c "echo Consumer: Press any key to stop.;java -cp target/SABD_Project2-0.0.1-SNAPSHOT-jar-with-dependencies.jar utils.Consumer;exec bash"

flink run --parallelism 12 -d --class "queries.QueriesStart" target/SABD_Project2-0.0.1-SNAPSHOT-jar-with-dependencies.jar


echo "Waiting start producer..."
sleep 10
java -cp target/SABD_Project2-0.0.1-SNAPSHOT-jar-with-dependencies.jar utils.Producer