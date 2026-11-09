#!/usr/bin/env python3
"""Fail CI if player-facing strings contain engineering / dev jargon."""
from __future__ import annotations

import re
import sys
from pathlib import Path

STRINGS_XML = Path(__file__).resolve().parents[1] / "android/app/src/main/res/values/strings.xml"

# Substrings banned inside <string> values (case-insensitive). Not applied to @name.
BANNED_IN_VALUES = [
    "default pack",
    "embedded-default",
    "embedded default",
    "bootstrap",
    "scaffold",
    "mvp",
    "fallback",
    "transaction",
    "placeholder",
    "ships later",
    "in-game audio ships",
    "app shell",
    "checklist",
    "phase ",
    "tbd",
    "stub",
    "unnamed",
    "unavailable",
    "update the app",
]

# Allow explicit exceptions (full string name in strings.xml).
ALLOW_STRING_NAMES: set[str] = set()


def main() -> int:
    if not STRINGS_XML.is_file():
        print(f"Missing {STRINGS_XML}", file=sys.stderr)
        return 1

    text = STRINGS_XML.read_text(encoding="utf-8")
    pattern = re.compile(
        r'<string\s+name="([^"]+)"[^>]*>([^<]*)</string>',
        re.DOTALL,
    )
    errors: list[str] = []
    for name, value in pattern.findall(text):
        if name in ALLOW_STRING_NAMES:
            continue
        decoded = value.replace("\\'", "'").replace("\\n", "\n")
        lower = decoded.lower()
        for banned in BANNED_IN_VALUES:
            if banned in lower:
                errors.append(f'{name}: contains "{banned}" → "{decoded}"')

    if errors:
        print("Player-facing string lint failed:", file=sys.stderr)
        for line in errors:
            print(f"  - {line}", file=sys.stderr)
        print(
            "\nSee planning/branding.md § UI copy (in-game).",
            file=sys.stderr,
        )
        return 1

    print(f"OK: {STRINGS_XML.name} ({len(pattern.findall(text))} strings)")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
