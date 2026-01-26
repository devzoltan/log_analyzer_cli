@echo off
setlocal EnableDelayedExpansion

REM Nutzung:
REM   run.cmd [Logdatei] [TopN] [Level]
REM Beispiele:
REM   run.cmd sample.log 3
REM   run.cmd sample.log 5 ERROR

set "LOGFILE=%~1"
if "%LOGFILE%"=="" set "LOGFILE=sample.log"

set "TOP=%~2"
if "%TOP%"=="" set "TOP=3"

set "LEVEL=%~3"

set "OUTDIR=out"
if exist "%OUTDIR%" rmdir /s /q "%OUTDIR%"

set "FILES="
for /r "src\main\java" %%F in (*.java) do (
  set "FILES=!FILES! "%%F""
)

if "%FILES%"=="" (
  echo No Java sources found under src\main\java
  exit /b 1
)

javac -d "%OUTDIR%" %FILES%
if errorlevel 1 exit /b %errorlevel%

set "MAIN=de.devzoltan.loganalyzer.Main"

if not "%LEVEL%"=="" (
  java -cp "%OUTDIR%" %MAIN% --level %LEVEL% --top %TOP% "%LOGFILE%"
) else (
  java -cp "%OUTDIR%" %MAIN% --top %TOP% "%LOGFILE%"
)

endlocal
