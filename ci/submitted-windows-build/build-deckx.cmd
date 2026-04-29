@echo off
setlocal

rem Build script for DeckX.
rem This script does not require Maven to be installed manually.
rem It downloads Apache Maven only if the expected Maven folder is not already present.

set "SCRIPT_DIR=%~dp0"
set "PROJECT_DIR=%SCRIPT_DIR%deckx"
set "POM_FILE=%PROJECT_DIR%\pom.xml"

set "MAVEN_VERSION=3.9.15"
set "MAVEN_FOLDER=apache-maven-%MAVEN_VERSION%"
set "MAVEN_DIR=%SCRIPT_DIR%%MAVEN_FOLDER%"
set "MAVEN_ZIP=%SCRIPT_DIR%%MAVEN_FOLDER%-bin.zip"
set "MAVEN_URL=https://archive.apache.org/dist/maven/maven-3/%MAVEN_VERSION%/binaries/%MAVEN_FOLDER%-bin.zip"

if not exist "%POM_FILE%" (
    echo Project pom.xml was not found.
    echo Expected: %POM_FILE%
    exit /b 1
)

where java >nul 2>nul
if errorlevel 1 (
    echo Java was not found on PATH.
    echo Java 21 is required to build and run this project.
    exit /b 1
)

if not exist "%MAVEN_DIR%\bin\mvn.cmd" (
    echo Portable Maven was not found.
    echo Downloading Apache Maven %MAVEN_VERSION%...

    powershell -NoProfile -ExecutionPolicy Bypass -Command ^
        "Invoke-WebRequest -Uri '%MAVEN_URL%' -OutFile '%MAVEN_ZIP%'"

    if errorlevel 1 (
        echo Maven download failed.
        exit /b 1
    )

    echo Extracting Apache Maven...
    powershell -NoProfile -ExecutionPolicy Bypass -Command ^
        "Expand-Archive -Path '%MAVEN_ZIP%' -DestinationPath '%SCRIPT_DIR%' -Force"

    if errorlevel 1 (
        echo Maven extraction failed.
        exit /b 1
    )
) else (
    echo Portable Maven already exists. Skipping Maven download.
)

if not exist "%MAVEN_DIR%\bin\mvn.cmd" (
    echo Maven setup failed.
    echo Expected: %MAVEN_DIR%\bin\mvn.cmd
    exit /b 1
)

echo Building DeckX...
call "%MAVEN_DIR%\bin\mvn.cmd" -f "%POM_FILE%" clean package

if errorlevel 1 (
    echo Build failed.
    exit /b 1
)

echo.
echo Build completed successfully.
echo Runnable jar:
echo %PROJECT_DIR%\target\DeckX-1.0-SNAPSHOT.jar

endlocal