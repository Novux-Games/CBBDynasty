# Player archetypes & draft projection

**Used on:** [`player-cards.md`](player-cards.md) (Overview archetype, draft line).  
**Scope:** Display + **sim tendency** tags (not a separate position).

---

## Draft projection (Overview)

| Condition | UI |
|-----------|-----|
| Player is **draft-eligible** (per ruleset year) **and** in national **Top 60** draft pool | Show **rank**: `Draft: #14` (integer 1–60) |
| Same + **optional label** (subline or tooltip) | Tier tag from table below |
| Not eligible, not ranked, or outside Top 60 | **`N/A`** (no rank, no label) |

**National Top 60** list lives in News hub ([`game-frame.md`](game-frame.md)); player card reads the same ordered pool.

### Optional rank labels (subline)

| Rank | Label |
|------|--------|
| 1–14 | Lottery |
| 15–29 | First round |
| 30–44 | Early second |
| 45–60 | Late second |
| — | Outside Top 60 → card shows **N/A** |

Display pattern (example):

```
Projected draft: #14
                 Lottery
```

Or single line: `Projected draft: #14 (Lottery)` — UI choice.

**Undrafted seniors** leaving the pool: **N/A** after draft resolves unless viewing historical card.

---

## Archetype list

Each player has **one primary archetype** on the Overview tab. Archetype is **fixed** for the season unless a development event changes it (rare; optional later).

**Positions:** players list **PG, SG, SF, PF, or C** ([`positions-and-lineup.md`](positions-and-lineup.md)); archetype rolls within that group.

### Guards (PG / SG)

| Archetype | Identity |
|-----------|----------|
| **Floor general** | Primary ballhandler; pick-and-roll orchestration, high passing |
| **Combo guard** | Score-first guard who can create and pass; between PG and SG |
| **3-point specialist** | Off-ball shooter; low usage create, high three-point tendency |
| **Lockdown defender** | Defensive stopper; lower usage, high perimeter defense |
| **Slashing guard** | Drives and finishes; gets to the rim, draws fouls |
| **Facilitator** | Distributor; assists over scoring, modest shooting |

### Forwards (SF / PF)

| Archetype | Identity |
|-----------|----------|
| **Wing scorer** | Primary scoring wing; versatile shot creation |
| **3-and-D wing** | Corner/spot-up threes, strong perimeter defense, low usage |
| **Point forward** | Secondary initiator from the forward spot; passing and handle |
| **Stretch four** | Spacing big on the perimeter; rebounding secondary |
| **Athletic wing** | Transition, cuts, finish at rim; jack-of-all-trades tendency |
| **Glue guy** | Role forward; moderate ratings, defense and hustle archetype |

### Centers (C only)

| Archetype | Identity |
|-----------|----------|
| **Rim protector** | Blocks, interior defense, lob finishes; limited range |
| **Post scorer** | Back-to-basket, hooks and paint touch |
| **Roll big** | Pick-and-roll finisher; vertical spacing |
| **Stretch five** | Outside shooting from center; lower interior D profile |
| **Paint beast** | Boards, putbacks, physical interior |

### Assignment (generation)

Rough priority when creating/rostering a player:

1. Use **listed position** (PG / SG / SF / PF / C).
2. Compare **highest attribute skew** (shooting vs handle vs defense vs rebounding).
3. Pick **one** archetype from that position’s table.

Archetype nudges **shot selection and matchup behavior** in [`simulation-engine.md`](simulation-engine.md) via tuning weights; OVR/OFF/DEF remain primary.

### UI

- Overview **Information** row: `Archetype: 3-and-D wing` (title case).
- **Ratings** tab: one-line archetype flavor text under attribute blocks.

---

## Document history

| Date | Change |
|------|--------|
| 2025-11-08 | Generated archetype catalog; Top 60 draft rank + optional labels |
