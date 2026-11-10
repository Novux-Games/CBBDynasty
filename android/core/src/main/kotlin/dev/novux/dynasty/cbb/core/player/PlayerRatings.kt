package dev.novux.dynasty.cbb.core.player

/** Headline ratings — [planning/player-cards.md#ratings] (0–99). */
data class PlayerRatings(
    val ovr: Int,
    val off: Int,
    val def: Int,
    val shootingInside: Int = ovr,
    val shootingMid: Int = ovr,
    val shootingThree: Int = ovr,
    val handling: Int = ovr,
    val passing: Int = ovr,
    val rebounding: Int = ovr,
    val perimeterDefense: Int = ovr,
    val interiorDefense: Int = ovr,
    val stealing: Int = ovr,
    val blocking: Int = ovr,
    val potentialOverall: Int = ovr,
    val proPotential: Int = ovr,
) {
    init {
        require(ovr in RATING_RANGE)
        require(off in RATING_RANGE)
        require(def in RATING_RANGE)
    }

    companion object {
        val RATING_RANGE: IntRange = 0..99

        fun headline(ovr: Int, off: Int, def: Int): PlayerRatings =
            PlayerRatings(ovr = ovr, off = off, def = def)
    }
}
