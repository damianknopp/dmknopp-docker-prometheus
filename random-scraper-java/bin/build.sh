#!/bin/bash

#rm -rf libs
##./bin/download-jars.sh
#mvn clean package
#rm -rf modules-out
#mkdir -p modules-out
#cp target/random*-fat.jar modules-out
#jlink --module-path modules-out/:libs/ --add-modules random.scraper --output tmp

mvn clean package
docker build . -t random-scraper-java
