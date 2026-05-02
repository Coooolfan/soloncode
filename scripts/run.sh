#!/bin/bash
set -e

cd "$(dirname "$0")/.."

./gradlew :soloncode-cli:shadowJar -q

JAR="soloncode-cli/build/libs/soloncode-cli.jar"

exec java --add-opens java.base/sun.misc=ALL-UNNAMED -jar "$JAR" "$@"
