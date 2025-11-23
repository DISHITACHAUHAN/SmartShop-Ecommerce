@REM Maven Wrapper startup script for Windows
@echo off
set MAVEN_VERSION=3.9.5
set MAVEN_HOME=%USERPROFILE%\.m2\wrapper\maven-%MAVEN_VERSION%

if not exist "%MAVEN_HOME%\bin\mvn.cmd" (
    echo Downloading Maven...
    mkdir "%MAVEN_HOME%" 2>nul
    powershell -Command "Invoke-WebRequest -Uri 'https://dlcdn.apache.org/maven/maven-3/%MAVEN_VERSION%/binaries/apache-maven-%MAVEN_VERSION%-bin.zip' -OutFile '%TEMP%\maven.zip'"
    powershell -Command "Expand-Archive -Path '%TEMP%\maven.zip' -DestinationPath '%USERPROFILE%\.m2\wrapper' -Force"
    move "%USERPROFILE%\.m2\wrapper\apache-maven-%MAVEN_VERSION%" "%MAVEN_HOME%"
)

"%MAVEN_HOME%\bin\mvn.cmd" %*
