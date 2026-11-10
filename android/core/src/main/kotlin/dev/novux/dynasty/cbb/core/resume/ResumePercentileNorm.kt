package dev.novux.dynasty.cbb.core.resume

import kotlin.math.roundToInt

/** League percentile map to **0–100** for NET inputs — [planning/selection-engine.md]. */
object ResumePercentileNorm {
    fun toHundred(raw: Map<String, Double>): Map<String, Double> {
        if (raw.isEmpty()) return emptyMap()
        val ordered = raw.entries.sortedBy { it.value }
        val n = ordered.size
        if (n == 1) return mapOf(ordered.single().key to 50.0)
        return ordered.mapIndexed { index, (id, _) ->
            val pct = index.toDouble() / (n - 1).coerceAtLeast(1) * 100.0
            id to pct
        }.toMap()
    }

    fun to99Int(raw: Map<String, Double>): Map<String, Int> =
        toHundred(raw).mapValues { (_, v) -> v.roundToInt().coerceIn(0, 99) }
}
