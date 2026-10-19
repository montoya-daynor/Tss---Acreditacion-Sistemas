#!/usr/bin/env bash
if [ -d "C:/Program Files/Java/jdk-21.0.12" ]; then
    export JAVA_HOME="C:/Program Files/Java/jdk-21.0.12"
    export PATH="$JAVA_HOME/bin:$PATH"
fi

if [ -f .env ]; then
    export $(grep -v '^#' .env | xargs)
fi

./mvnw spring-boot:run
