#!/bin/sh
# Gradle wrapper script for UNIX systems
#APP_HOME="$(cd "$(dirname "$0")" && pwd)"
exec "./gradle/wrapper/gradle-wrapper-8.4.jar" "$@" 2>/dev/null # || exec java -jar "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" "$@" 2>/dev/null || (echo "Please run: gradle wrapper --gradle-version 8.4" && exit 1)
