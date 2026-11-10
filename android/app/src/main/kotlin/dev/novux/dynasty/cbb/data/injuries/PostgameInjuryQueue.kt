package dev.novux.dynasty.cbb.data.injuries

import dev.novux.dynasty.cbb.core.injuries.GameSimInjuryEvent
import java.util.concurrent.ConcurrentHashMap

/** Pending postgame injury sheet payloads — cleared after user dismisses. */
class PostgameInjuryQueue {
    private val pending = ConcurrentHashMap<String, List<GameSimInjuryEvent>>()

    fun enqueue(dynastyId: String, events: List<GameSimInjuryEvent>) {
        if (events.isEmpty()) return
        pending[dynastyId] = (pending[dynastyId].orEmpty() + events).distinctBy { it.playerId }
    }

    fun peek(dynastyId: String): List<GameSimInjuryEvent> = pending[dynastyId].orEmpty()

    fun clear(dynastyId: String) {
        pending.remove(dynastyId)
    }
}
