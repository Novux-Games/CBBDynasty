#!/usr/bin/env bash
# Play update requires versionCode > prior open-test build (MEGA-QA-136).
set -euo pipefail

repo_root="$(cd "$(dirname "$0")/.." && pwd)"
baseline_file="$repo_root/android/play_open_test_baseline_version_code.txt"
if [[ ! -f "$baseline_file" ]]; then
  echo "Missing baseline file: $baseline_file" >&2
  exit 1
fi

baseline="$(tr -d ' \n\r' <"$baseline_file")"
current="$(bash "$repo_root/scripts/resolve_app_version_code.sh")"

if [[ -z "$current" || -z "$baseline" ]]; then
  echo "Could not parse version codes (baseline=$baseline current=$current)" >&2
  exit 1
fi

if [[ "$current" -le "$baseline" ]]; then
  echo "versionCode $current must be > Play open-test baseline $baseline" >&2
  exit 1
fi

echo "Play update version bump OK: $current > baseline $baseline (MEGA-QA-136)"
