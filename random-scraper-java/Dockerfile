FROM alpine:latest as builder
RUN apk --no-cache add openjdk11-jdk openjdk11-jmods
RUN mkdir -p /opt/proj
WORKDIR /opt/proj
RUN /usr/lib/jvm/java-11-openjdk/bin/jlink \
	--module-path "${JAVA_HOME}"/jmods \
	--verbose \
	--add-modules java.base,java.logging,java.xml,jdk.unsupported \
	--compress 2 \
	--no-header-files \
    --strip-debug \
    --no-man-pages \
	--output /opt/proj/jdk-minimal

FROM alpine:latest
WORKDIR /opt
ENV JAVA_HOME=/opt/jdk-minimal
ENV PATH="$PATH:$JAVA_HOME/bin"
COPY --from=builder /opt/proj/jdk-minimal /opt/jdk-minimal
COPY target/random-scraper-1.0-SNAPSHOT-fat.jar /opt/proj/mods/
ENTRYPOINT ["/opt/jdk-minimal/bin/java", "--module-path", "/opt/proj/mods", "--module", "random.scraper", "-conf", "'{\"http\":{\"port\":8081}}'"]