package dev.novux.dynasty.cbb.core.pack

/** Conference tournament bracket size derived from membership ([MEGA-WORLD-017]). */
object PackConferenceBracketSizePolicy {
    const val MEGA_ID: String = "MEGA-WORLD-017"

    fun bracketSize(teamCount: Int): Int {
        var size = 1
        while (size < teamCount) size *= 2
        return size
    }
}