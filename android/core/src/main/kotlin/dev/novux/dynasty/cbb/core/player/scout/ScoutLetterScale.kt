package dev.novux.dynasty.cbb.core.player.scout

/** 0–99 → letter bands — [planning/recruiting-engine.md#hidden-ratings--letter-grades]. */
object ScoutLetterScale {
    private val bands = listOf(
        92 to "A",
        88 to "A-",
        84 to "B+",
        78 to "B",
        72 to "B-",
        68 to "C+",
        62 to "C",
        56 to "C-",
        50 to "D+",
        44 to "D",
        0 to "F",
    )

    fun fromRating(rating: Int, fuzzSteps: Int = 0): String {
        val clamped = rating.coerceIn(0, 99)
        val adjusted = (clamped - fuzzSteps * 4).coerceAtLeast(0)
        return bands.first { adjusted >= it.first }.second
    }
}
