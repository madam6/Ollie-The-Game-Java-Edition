@echo off
rem Compiles and starts the game. Needs JDK 17 or newer.
cd /d "%~dp0"
set CP=lib\jbox2d-cityengine.jar
if exist build rmdir /s /q build
javac --release 17 -encoding UTF-8 -cp "%CP%" -sourcepath src -d build src\ollie\Game.java || exit /b 1
java -cp "build;%CP%" ollie.Game %*
