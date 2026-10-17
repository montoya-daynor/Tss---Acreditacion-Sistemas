@echo off
if exist "C:\Program Files\Java\jdk-21.0.12" (
    set "JAVA_HOME=C:\Program Files\Java\jdk-21.0.12"
    set "PATH=C:\Program Files\Java\jdk-21.0.12\bin;%PATH%"
)
set DB_URL=jdbc:postgresql://ep-royal-bread-b6nzwcdw-pooler.c-2.sa-east-1.aws.neon.tech/neondb?sslmode=require
set DB_USERNAME=neondb_owner
set DB_PASSWORD=npg_0GIBtsj2Pkxh
.\mvnw.cmd spring-boot:run
