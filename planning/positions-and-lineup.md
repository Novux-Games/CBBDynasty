# Positions & lineup

**Used in:** [`game-frame.md`](game-frame.md) (Strategy → rotation), [`simulation-engine.md`](simulation-engine.md), [`player-cards.md`](player-cards.md).  
**Last updated:** 2025-11-08

---

## Listed position (player card)

Every player has one **listed position**:

| Code | Label |
|------|--------|
| **PG** | Point guard |
| **SG** | Shooting guard |
| **SF** | Small forward |
| **PF** | Power forward |
| **C** | Center |

Shown on player card header and roster lists. **Archetypes** ([`player-archetypes.md`](player-archetypes.md)) follow listed position (guard / wing / big families).

---

## Game slots vs listed position

A game **starting five** fills five **slots**: PG, SG, SF, PF, C.

| Slot | Who can play it |
|------|-----------------|
| **PG** | Listed **PG** or **SG** |
| **SG** | Listed **SG** or **PG** |
| **SF** | Listed **SF** or **PF** |
| **PF** | Listed **PF** or **SF** |
| **C** | Listed **C** only |

**Interchange pairs**

- **Backcourt:** PG ↔ SG  
- **Frontcourt:** SF ↔ PF  
- **Center:** no interchange (unless a future rule adds “small-ball C” — not in scope)

A player in a slot may be **out of listed position** (e.g. listed PG at SG). **Simulation uses that player’s ratings in the slot they’re playing** — a **84 OVR** listed PG at **SG** should produce **better outcomes** than a **72 OVR** listed SG at SG. Interchange is about default UI, not pretending the backup guard is stronger because of the letter on his card.

Optional tiny **position comfort** nudge (e.g. listed PG at SG −0 to −2 on a subset of rolls) may exist in tuning; it must **not** flip order so 72 listed SG outplays 84 slotted at SG. Primary driver remains OVR/OFF/DEF and attributes.

---

## Default starters algorithm

**Reset to default** in Strategy hub uses **listed position only** — it never auto-slides a PG into SG (or SF into PF) to maximize OVR.

Per slot:

| Slot | Default starter |
|------|-----------------|
| **PG** | Highest **OVR** among players **listed PG** |
| **SG** | Highest **OVR** among players **listed SG** |
| **SF** | Highest **OVR** among players **listed SF** |
| **PF** | Highest **OVR** among players **listed PF** |
| **C** | Highest **OVR** among players **listed C** |

Tie-break: stable player ID order.

**Interchange is manual only** for the starting five. Eligibility still allows PG-at-SG when the **user** sets it; the game does not do that on reset/default.

### Example

| Player | Listed | OVR | Default role |
|--------|--------|-----|----------------|
| A | PG | 91 | **Starting PG** |
| B | PG | 84 | Bench PG |
| C | SG | 72 | **Starting SG** |

- Default **does not** start B at SG even though 84 > 72.
- User may move **B** from bench PG → **starting SG** (legal interchange). **Sim expects B to outperform C** in that slot.

### Empty slot

If no one is **listed** for a slot (e.g. no SG on roster), that slot stays **unfilled** in default and UI prompts to assign a starter (user may use interchange). No automatic cross-list promotion on reset.

---

## User override

- User may set **any legal** assignment in Rotation (drag slot or pick from eligible list per slot).
- Invalid: e.g. listed **C** only at **PF** — **not allowed** without PF/SF list (centers stay C slot only).
- Invalid: SF listed player at **PG** — not allowed.

Overrides apply to **next sim**; week replay rules unchanged ([`game-design.md`](game-design.md)).

---

## Rotation & bench

- **Starters** on roster tab = current default or user five for next game.
- **Reserves / bench:** everyone else; sub logic in sim uses minutes bands from Strategy ([`game-frame.md`](game-frame.md)).

---

## Sim engine

[`simulation-engine.md`](simulation-engine.md) lineup validation:

```
slotEligible(player, slot):
  PG  if player.position in {PG, SG}
  SG  if player.position in {SG, PG}
  SF  if player.position in {SF, PF}
  PF  if player.position in {PF, SF}
  C   if player.position == C
```

Matchups and archetype nudges use **slot played** optional for scheme (e.g. “defend their SG”) — TBD in strategy doc.

---

## Document history

| Date | Change |
|------|--------|
| 2025-11-08 | PG/SG/SF/PF/C + interchange + default starter algorithm |
| 2025-11-08 | Default starters: best OVR per **listed** position; interchange manual only |
| 2025-11-08 | Out-of-position slot: better player’s ratings win in sim (e.g. 84 PG at SG) |
