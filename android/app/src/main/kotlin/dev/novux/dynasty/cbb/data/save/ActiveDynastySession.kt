package dev.novux.dynasty.cbb.data.save

import dev.novux.dynasty.cbb.core.save.LoadedDynasty

/** In-memory active dynasty until in-game shell exists (Phase 4+). */
object ActiveDynastySession {
    @Volatile
    var current: LoadedDynasty? = null
        private set

    fun set(loaded: LoadedDynasty) {
        current = loaded
    }

    fun clear() {
        current = null
    }
}
