# Portrait cache GPU / memory budget

**MEGA-COACH-107** · Last updated: 2026-11-16

Preset portraits are vector-style `Canvas` draws (no bitmap atlas). Budget intent:

| Surface | Max simultaneous | Notes |
|---------|------------------|--------|
| Roster tab | 20 headshots | 40dp thumbnails via `PresetHeadshot` |
| Player card strip | 1 | 56dp default |
| Coach staff rows | 8 | Coach cards omit portrait per product lock |

**Cache:** `PresetPortraitRenderer.specFor(presetId)` is O(1) map lookup; no GPU texture cache.

**Accessibility:** blank `presetId` uses initials fallback ([`PortraitAccessibilityFallbackPolicy`](../android/core/src/main/kotlin/dev/novux/dynasty/cbb/core/portrait/PortraitAccessibilityFallbackPolicy.kt)).