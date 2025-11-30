@echo off
setlocal enabledelayedexpansion

set ROOT=%~dp0
set SRC=%ROOT%src
set OUT=%ROOT%out

if not exist "%OUT%" mkdir "%OUT%"

REM Default args
set BOARD=%~1
set MODE=%~2
if "%BOARD%"=="" set BOARD=%ROOT%samples\board-valid.csv
if "%MODE%"=="" set MODE=27

if not exist "%BOARD%" (
  echo Board file not found: %BOARD%
  echo Usage: run.bat "path\to\board.csv" [mode]
  exit /b 1
)

echo Compiling sources to %OUT% ...
javac -d "%OUT%" "%SRC%\*.java"
if errorlevel 1 exit /b 1

echo Running: mode=%MODE% board=%BOARD%
java -cp "%OUT%" main "%BOARD%" %MODE%
endlocal
