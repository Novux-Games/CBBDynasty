# Product Updates: November 8, 2026

> Dynasty: College Basketball now opens on Android with a studio splash and home menu, while the full season—from offseason through March—is locked in design.

### New

- **Android app shell**: Cold start shows a Novux Games card, then the game splash and **Tap to Play**. Home offers **New**, **Load**, **Settings**, and **Credits** (**Continue** appears once a save exists). Menu buttons are live; dynasty create/load comes next.
- **New dynasty wizard (design lock)**: Six-step flow—coach, archetype, offense/defense, house rules, school, contract—starting in the offseason with a coaching carousel from year two onward.
- **Season calendar (design lock)**: Nine-week offseason, one preseason week, up to three exhibitions, 32-game regular season, conference tournaments, seven national postseason weeks, then **End of Season** review.
- **March Mayhem (design lock)**: 124 teams in national play—76-team NCBA (Opening Round through the championship), 32-team NCI, and 16-team Heritage Classic—with unified seed labels and round names.
- **365-team universe (design lock)**: 2026–27 league with home city/state, NCBA NET for selection, separate Media and Coaches polls, and in-season **Top 60** draft watch fed from a preseason-generated HS/JUCO class.
- **Recruiting v1 (design lock)**: Four-week offseason offer window with NIL and letter-grade scouting—no visits; unused points carry half forward week to week.
- **Possession simulation (design lock)**: Games run possession-by-possession on a **30-second** shot clock with adjustable playback speed.
- **Implementation roadmap**: [`docs/BUILD_CHECKLIST.md`](BUILD_CHECKLIST.md) orders delivery—saves and calendar, then new dynasty, sim, league, and March.

### Improved

- **Official title**: Product and docs consistently use **Dynasty: College Basketball** (Novux Games).
- **Adaptive app icons**: Layered Android icons (canvas, foreground, background) generated from `app-logo.svg`, with corrected crop and render pipeline.
- **Coach & staff UX (design lock)**: Coach card carries job security; **School Staff** separates assistants from support; staff history is a dedicated tab.
- **Strategy presets (design lock)**: Seven offensive and seven defensive schemes locked, with clear rules for in-season edits.
- **Player & lineup model (design lock)**: PG/SG/SF/PF/C slots, manual interchange, and starters chosen by listed position; sim respects ratings when interchange players take the floor.

### Fixed

- Resolved adaptive icon assets that were derived from the wrong source mark and cropped incorrectly on Android.
- Resolved mixed v1/v2 scope language across planning docs so customer-facing specs read as a single full-product vision.
