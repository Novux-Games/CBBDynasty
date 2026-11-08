# Strategy: schemes, gameplan, halftime

**UI:** Strategy hub in [`game-frame.md`](game-frame.md).  
**Sim:** [`simulation-engine.md`](simulation-engine.md) reads prep + halftime overrides.  
**Last updated:** 2025-11-08

---

## Offensive scheme presets (7)

| Preset | Identity (sim tendency) |
|--------|-------------------------|
| **Balanced** | Neutral shot mix, average pace |
| **Motion** | Cuts, screens, eg, lower iso, moderate pace |
| **Drive** | Rim pressure, foul draw, kick-outs |
| **Set** | Quick-hitter actions, half-court sets, lower pace variance |
| **Early** | Transition, push after misses/makes, higher pace |
| **Perimeter** | High 3PA rate, spacing, drive & kick |
| **Post** | Interior touches, PF/C usage bump |

---

## Defensive scheme presets (7)

| Preset | Identity |
|--------|----------|
| **Man-to-Man** | Standard; matchup-driven |
| **2-3 Zone** | Pack paint, contest threes from wings |
| **3-2 Zone** | Pressure perimeter, weak interior |
| **1-3-1 Zone** | Trap edges, lengthy, gamble passing lanes |
| **Box-and-One** | Four protect star, one chases primary scorer |
| **Full-court press** | Turnover chase; fatigue cost |
| **Switching man** | P&R switch-all; perimeter switchability matters |

---

## Gameplan fields (locked for planning)

### Pre-game (before tip)

| Field | Source |
|-------|--------|
| **Offensive scheme** | Preset table above |
| **Defensive scheme** | Preset table above |
| **Starting five + rotation** | [`positions-and-lineup.md`](positions-and-lineup.md) |
| **Usage** | Per-player usage targets (Strategy hub) |
| **Primary defensive assignment** | Map opponent **slot** (PG–C) to your defender; default = best match from lineup |
| **Deny primary scorer** | On/off — extra pressure on assignment; higher foul + fatigue risk ([`simulation-engine.md`](simulation-engine.md)) |
| **Late-game foul strategy** | **Standard** / **Foul early** / **Protect lead** — endgame possession decisions |

No separate **pace slider** — **pace comes from offensive preset** + opponent scheme interaction (tuning JSON).

Coach **default** O/D schemes (season baseline, not per-game): [`coach-card.md`](coach-card.md#strategy).

### Halftime (in-season)

When game clock hits **half**, user may **adjust as coach** before second half sim resumes:

| Halftime adjustment | Allowed |
|---------------------|---------|
| Change **offensive** or **defensive** scheme | Yes |
| **Rotation / minutes** emphasis | Yes |
| **Usage** tweaks | Yes |
| Swap **starters** for second half (legal slots) | Yes |

Second half sim uses updated prep; first half stats and score **fixed**.

**Watch flow:** possessions run at **2 seconds** wall-clock until half; pause for halftime UI; resume at 2s/possession for second half.

### Strategy edits & week save (**design lock** — confirmed)

Whether strategy edits are blocked on certain days:

| When | Edit lineup / rotation / usage / scheme? |
|------|-------------------------------------------|
| Any time before **that game’s** sim | Yes |
| **Halftime** of watched game | Yes (second half only) |
| After game sim, before **week advance** | Yes (replay same game allowed — new prep) |
| **After week advance** | That week’s games locked; prep applies to **next** games |

Nothing special about “Tuesday vs Thursday” within the week.

---

## Game preview (no games played)

Per your spec — see [`game-frame.md`](game-frame.md):

- **Win %** and **predicted score** from **team ratings** + **RPI** (résumé index), **not** from games played this season.
- Works in **preseason** and **week zero** of schedule.

---

## Watch sim timing

| Setting | Value |
|---------|--------|
| Wall-clock between possession lines | **2 seconds** (default); user **scaling speed** multiplies this |
| **Sim to end** | Same engine, no per-line delay ([`simulation-engine.md`](simulation-engine.md#playback-watch-speed-sim-to-end)) |
| In-sim time per possession | **3–30 s** game clock; **30 s** shot clock cap ([`simulation-engine.md`](simulation-engine.md)) |
| Halftime | Coach adjustments, then resume |

---

## Document history

| Date | Change |
|------|--------|
| 2025-11-08 | O/D presets, gameplan + halftime, preview/watch notes |
| 2025-11-08 | Strategy edit / week-advance rules marked design lock |
| 2025-11-08 | Ship **7** offensive and **7** defensive presets |
| 2025-11-08 | Full gameplan: matchup assignment, deny-star, late-game fouls |
