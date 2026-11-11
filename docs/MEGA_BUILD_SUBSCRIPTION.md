# MEGA build queue subscription

**Purpose:** One Cloud Agent turn per **open** `MEGA-*` row until the queue is empty, then **stop** the timer subscription.

## Artifacts

| File | Role |
|------|------|
| [`MEGA_BUILD_QUEUE.json`](MEGA_BUILD_QUEUE.json) | Ordered open items (P0 `FAIL` first) |
| [`MEGA_BUILD_QUEUE_STATE.json`](MEGA_BUILD_QUEUE_STATE.json) | `index`, `completedIds`, `subscriptionName` |
| [`planning/tools/mega_queue.py`](../planning/tools/mega_queue.py) | Build / current / complete / stats |
| Timer name | `mega-build-queue` (`cursor-subscriptions-subscribe_timer`) |

## On each timer wake (mandatory)

1. `git pull` on `main` (or your executor branch) so queue/state are current.
2. `python3 planning/tools/mega_queue.py resync`
3. If output is **`QUEUE_EMPTY`**: call `cursor-subscriptions-list_subscriptions`, then `cursor-subscriptions-unsubscribe` for timer **`mega-build-queue`** → **end turn** (no more work).
4. Else implement **only** the current item (`mega_queue.py current`):
   - Read evidence paths in the shard row; read locked planning spec.
   - **Full product — nothing deferred.** Ship real behavior, not stubs.
   - Test (unit + device/emulator when UI).
   - Set shard table **Status** → `PASS` or `SHIP` (same row `id`).
5. `python3 planning/tools/mega_queue.py complete --id <MEGA-…>`
6. If **`QUEUE_EMPTY`** after complete → **unsubscribe** `mega-build-queue` and stop.
7. Else **re-subscribe** timer (same name, `once: true`, `delaySeconds: 60` minimum — platform limit) with this file’s wake prompt (see below).
8. Commit, push; open/update PR as usual.

## Timer prompt (copy for `subscribe_timer`)

```text
MEGA BUILD QUEUE — next item.

Follow docs/MEGA_BUILD_SUBSCRIPTION.md exactly.
Run: python3 planning/tools/mega_queue.py resync
If QUEUE_EMPTY: list_subscriptions + unsubscribe mega-build-queue and STOP.
Else implement current MEGA item (full product, no deferrals), update shard Status to PASS/SHIP, run mega_queue.py complete --id <id>, test, commit push.
If still items remain after complete: subscribe_timer name mega-build-queue once true delaySeconds 60 with this same instruction block.
If QUEUE_EMPTY after complete: unsubscribe mega-build-queue and STOP.
```

## Refresh queue after manual shard edits

```bash
python3 planning/tools/mega_queue.py build
python3 planning/tools/mega_queue.py resync
```

## Policy

- Do **not** batch multiple MEGA ids in one timer wake unless the user overrides.
- Do **not** mark `complete` until Status in the shard is honestly `PASS`/`SHIP`/`DONE`.
- When unsubscribed, add a line to `MEGA_BUILD_QUEUE_STATE.json` field `stoppedAt` (ISO time) if you edit state by hand.
