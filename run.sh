#!/usr/bin/env sh
# Compiles and starts the game. Needs JDK 17 or newer.
set -e
cd "$(dirname "$0")"
CP="lib/jbox2d-cityengine.jar"
rm -rf build
javac --release 17 -encoding UTF-8 -cp "$CP" -sourcepath src -d build src/ollie/Game.java
java -cp "build:$CP" ollie.Game "$@"
