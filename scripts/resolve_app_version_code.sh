#!/usr/bin/env bash
# Monotonic versionCode for Play uploads (MEGA-QA-136). Used by Gradle and verify scripts.
set -euo pipefail

repo_root="$(cd "$(dirname "$0")/.." && pwd)"
baseline_file="$repo_root/android/play_open_test_baseline_version_code.txt"

if [[ -n "${VERSION_CODE:-}" ]]; then
  echo "$VERSION_CODE"
  exit 0
fi

if [[ ! -f "$baseline_file" ]]; then
  echo "Missing baseline file: $baseline_file" >&2
  exit 1
fi

baseline="$(tr -d ' \n\r' <"$baseline_file")"
git_count="$(git -C "$repo_root" rev-list --count HEAD)"
attempt="${GITHUB_RUN_ATTEMPT:-1}"
attempt_offset=$(( attempt > 1 ? attempt - 1 : 0 ))
computed=$(( git_count + attempt_offset ))

if [[ "$computed" -le "$baseline" ]]; then
  computed=$(( baseline + 1 ))
fi

echo "$computed"