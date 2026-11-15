#!/usr/bin/env sh
set -eu
BASE_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
MAVEN_VERSION=3.9.9
WRAPPER_DIR="$BASE_DIR/.mvn/wrapper"
MAVEN_HOME="$WRAPPER_DIR/apache-maven-$MAVEN_VERSION"
if command -v mvn >/dev/null 2>&1; then
  exec mvn "$@"
fi
if [ ! -x "$MAVEN_HOME/bin/mvn" ]; then
  ARCHIVE="$WRAPPER_DIR/apache-maven-$MAVEN_VERSION-bin.zip"
  if command -v curl >/dev/null 2>&1; then
    curl -fsSL "https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/$MAVEN_VERSION/apache-maven-$MAVEN_VERSION-bin.zip" -o "$ARCHIVE"
  elif command -v wget >/dev/null 2>&1; then
    wget -q "https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/$MAVEN_VERSION/apache-maven-$MAVEN_VERSION-bin.zip" -O "$ARCHIVE"
  else
    echo 'Maven is not installed and neither curl nor wget is available.' >&2
    exit 1
  fi
  unzip -q "$ARCHIVE" -d "$WRAPPER_DIR"
  rm -f "$ARCHIVE"
fi
exec "$MAVEN_HOME/bin/mvn" "$@"
