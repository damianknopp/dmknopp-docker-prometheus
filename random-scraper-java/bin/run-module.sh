#!/bin/bash

module_name="random.scraper"
rm -rf ./modules-out
mvn clean package
mkdir -p ./modules-out
cp target/random*-fat.jar modules-out/
#PORT=8081
java --module-path $JAVA_HOME/jmods:./modules-out --module "${module_name}" -conf '{"http":{"port":8081}}'
