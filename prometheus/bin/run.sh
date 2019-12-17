#!/bin/bash
# if running prometheus locally
# --add-host="localhost:172.17.0.1"
docker run  \
 	-p 9090:9090 \
 	-v $PWD/prometheus.yml:/etc/prometheus/prometheus.yml \
 	-v $PWD/prometheus.rules.yml:/etc/prometheus.rules.yml \
	--rm -it prom/prometheus
