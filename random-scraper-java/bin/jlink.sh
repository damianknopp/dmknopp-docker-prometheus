#!/bin/bash

#module_name="random.scraper"
#jlink --module-path "${JAVA_HOME}"/jmods/:./modules-out --add-modules "${module_name}" --output scraper-bin

jlink \
	--module-path "${JAVA_HOME}"/jmods \
	--verbose \
	--add-modules java.base,java.logging,java.xml,jdk.unsupported \
	--compress 2 \
	--no-header-files \
	--output jdk-minimal