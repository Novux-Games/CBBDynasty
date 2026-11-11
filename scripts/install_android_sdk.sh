#!/usr/bin/env bash
# Idempotent Android SDK bootstrap for Cloud Agents and local dev (API 36).
set -euo pipefail

SDK_ROOT="${ANDROID_HOME:-${ANDROID_SDK_ROOT:-$HOME/Android/Sdk}}"
CMDLINE_ZIP_URL="${ANDROID_CMDLINE_TOOLS_URL:-https://dl.google.com/android/repository/commandlinetools-linux-13114758_latest.zip}"
BUILD_TOOLS="${ANDROID_BUILD_TOOLS:-36.0.0}"

mkdir -p "$SDK_ROOT/cmdline-tools"
if [[ ! -x "$SDK_ROOT/cmdline-tools/latest/bin/sdkmanager" ]]; then
  tmp="$(mktemp -d)"
  curl -fsSL "$CMDLINE_ZIP_URL" -o "$tmp/cmdline-tools.zip"
  unzip -q "$tmp/cmdline-tools.zip" -d "$tmp"
  rm -rf "$SDK_ROOT/cmdline-tools/latest"
  mv "$tmp/cmdline-tools" "$SDK_ROOT/cmdline-tools/latest"
  rm -rf "$tmp"
fi

export ANDROID_HOME="$SDK_ROOT"
export ANDROID_SDK_ROOT="$SDK_ROOT"
export PATH="$SDK_ROOT/cmdline-tools/latest/bin:$SDK_ROOT/platform-tools:$PATH"

for _ in $(seq 1 100); do echo y; done | sdkmanager --licenses >/dev/null || true
sdkmanager --install \
  "platform-tools" \
  "platforms;android-36" \
  "build-tools;${BUILD_TOOLS}"

repo_root="$(cd "$(dirname "$0")/.." && pwd)"
props="$repo_root/android/local.properties"
echo "sdk.dir=$SDK_ROOT" >"$props"

echo "Android SDK ready at $SDK_ROOT"
