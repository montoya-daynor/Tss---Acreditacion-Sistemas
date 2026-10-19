@echo off
if exist "C:\Program Files\Java\jdk-21.0.12" (
    set "JAVA_HOME=C:\Program Files\Java\jdk-21.0.12"
    set "PATH=C:\Program Files\Java\jdk-21.0.12\bin;%PATH%"
)
if exist .env (
    for /f "usebackq tokens=1,* delims==" %%A in (".env") do (
        set "%%A=%%B"
    )
)
.\mvnw.cmd spring-boot:run
