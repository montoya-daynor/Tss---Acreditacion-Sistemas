#!/usr/bin/env bash
if [ -d "C:/Program Files/Java/jdk-21.0.12" ]; then
    export JAVA_HOME="C:/Program Files/Java/jdk-21.0.12"
    export PATH="$JAVA_HOME/bin:$PATH"
fi

export DB_URL="jdbc:postgresql://ep-royal-bread-b6nzwcdw-pooler.c-2.sa-east-1.aws.neon.tech/neondb?sslmode=require"
export DB_USERNAME="neondb_owner"
export DB_PASSWORD="npg_0GIBtsj2Pkxh"

./mvnw spring-boot:run
