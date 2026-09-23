#!/usr/bin/env bash
# Build debug APK without Android Studio.
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
export ANDROID_HOME="${ANDROID_HOME:-$HOME/Library/Android/sdk}"
export ANDROID_SDK_ROOT="$ANDROID_HOME"
export PATH="$ANDROID_HOME/platform-tools:$PATH"
export JAVA_HOME="${JAVA_HOME:-$(brew --prefix openjdk@21)/libexec/openjdk.jdk/Contents/Home}"

cd "$ROOT"
./gradlew :app:assembleDebug "$@"
echo "APK: $ROOT/app/build/outputs/apk/debug/app-debug.apk"
if adb get-state >/dev/null 2>&1; then
  adb install -r "$ROOT/app/build/outputs/apk/debug/app-debug.apk"
else
  echo "No adb device — connect phone with USB debugging to install."
fi
