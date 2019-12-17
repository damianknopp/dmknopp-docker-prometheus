#!/bin/bash
# log into the first running container
docker exec -i -t $(docker ps -a -f status=running -q) sh 
