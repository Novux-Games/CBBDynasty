# Product Updates: November 8, 2026

> We locked core gameplay loops—from possession sim to recruiting—and shipped Android-ready adaptive icon assets under the official **Dynasty: College Basketball** brand.

### New

- **Simulation engine**: Possession-based game simulation is now fully specified, including real-style NET, save/checkpoint behavior, performance targets, and test fixtures so builds match on-court outcomes.
- **In-season game frame**: Week-by-week season flow is defined—advance the calendar, play games, and hit weekly checkpoints with a clear replay window before progress saves.
- **Player cards**: Overview, stats, and card layout are documented, including season vs. career averages and NIL valuation on the player Overview.
- **Positions & lineup**: Standard PG/SG/SF/PF/C slots plus manual interchange rules; starters default to listed positions so roster management matches how lineups actually play.
- **Player archetypes & Top 60**: Generated archetypes and in-season draft projection rules tie player identity to long-term dynasty planning.
- **Recruiting engine (v1 locked)**: Four-week signing window, recruit pools, NIL letter grades, carryover rules, and a fixed in-season Top 60—no visit mini-game in v1.
- **Strategy schemes**: Fourteen presets (seven offensive, seven defensive) are design-locked, with clear rules for when you can edit strategy mid-season.
- **Coach & staff UI**: Coach card and School Staff tab specs separate on-court assistants from support staff for cleaner dynasty management screens.
- **League universe (2026–27)**: 365-team pack across 32 conferences with home-state geography driving recruiting pipelines.
- **NCBA / NCI branding**: Universe naming, early-season events, NCI selection, and postseason round labels align the sim with its fictional league identity.
- **Adaptive app icons**: Canvas, foreground, and background layers plus a generator pipeline produce Android adaptive icons from the official app logo.
- **Official product title**: Public docs and assets now use **Dynasty: College Basketball** consistently.

### Improved

- **Dashboard rankings**: Current ranking displays use NCBA and NCI tier colors so standings read clearly at a glance.
- **Weekly saves & replay**: Advancing a week triggers a save; you can replay games in the current week until the weekly checkpoint—no accidental progress lock-in.
- **Shot clock & playback**: Simulation docs now standardize a 30-second shot clock and document playback modes for readable game text.
- **Adaptive icon pipeline**: Icons are generated and cropped from `app-logo.svg` with a simplified foreground on brand background—less clutter on home screens.

### Fixed

- Resolved issues where adaptive icons used the wrong source art and cropped incorrectly on Android launchers.
- Resolved an issue where interchange lineup slots could ignore player ratings during simulation; ratings now drive performance when subs share positions.
