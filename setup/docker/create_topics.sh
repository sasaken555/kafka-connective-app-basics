#!/bin/sh
set -x

kafka-topics.sh --bootstrap-server localhost:29092 --create --topic ticket-transaction --partitions 3 --replication-factor 1
kafka-topics.sh --bootstrap-server localhost:29092 --create --topic valid-ticket-transaction --partitions 3 --replication-factor 1
kafka-topics.sh --bootstrap-server localhost:29092 --create --topic calculated-ticket-transaction --partitions 3 --replication-factor 1
kafka-topics.sh --bootstrap-server localhost:29092 --list

