package dev.novux.dynasty.cbb.core.brackets

import dev.novux.dynasty.cbb.core.selection.NcbaSelectionEntry

/**
 * Classic four-region S-curve (1→E, 2→MW, 3→S, 4→W, 5→W, 6→S, …) — [planning/selection-engine.md#seeds-5364--regions].
 */
object NcbaSCurveEngine {
    fun regionForBracketSeed(bracketSeed: Int): NcbaRegionId {
        require(bracketSeed in 1..64) { "bracket seed must be 1–64" }
        val line = (bracketSeed - 1) / 4
        val pos = (bracketSeed - 1) % 4
        val regionIndex = if (line % 2 == 0) pos else 3 - pos
        return NcbaRegionId.serpentineOrder[regionIndex]
    }

    fun assignEntrants(field: List<NcbaSelectionEntry>): List<NcbaMainBracketEntrant> {
        require(field.size == 64) { "NCBA main bracket requires 64 teams" }
        val sorted = field.sortedBy { it.ncbaSeed }
        val bracketSeedByTeam = sorted.mapIndexed { index, entry -> entry.teamId to index + 1 }.toMap()
        val byRegion = sorted
            .groupBy { entry ->
                regionForBracketSeed(bracketSeedByTeam.getValue(entry.teamId))
            }
        return NcbaRegionId.serpentineOrder.flatMap { region ->
            val entries = byRegion[region].orEmpty().sortedBy { bracketSeedByTeam.getValue(it.teamId) }
            entries.mapIndexed { index, entry ->
                NcbaMainBracketEntrant(
                    teamId = entry.teamId,
                    ncbaSeed = entry.ncbaSeed,
                    regionId = region,
                    regionalSeed = index + 1,
                )
            }
        }
    }
}
