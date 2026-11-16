package dev.novux.dynasty.cbb.core.selection

/**
 * Bubble cut lines for Selection Sunday reveal ([planning/postseason.md#bubble-ui], MEGA-POST-034–035, 146–150).
 */
object SelectionSundayBubbleEngine {
    const val MEGA_ID: String = "MEGA-POST-034"
    const val NCBA_DIRECT_ID: String = "MEGA-POST-146"
    const val OPENING_CUT_ID: String = "MEGA-POST-147"
    const val NCI_CUT_ID: String = "MEGA-POST-148"
    const val HERITAGE_CUT_ID: String = "MEGA-POST-149"
    const val RECLASS_PASSOVER_ID: String = "MEGA-POST-035"

    data class BubbleLine(
        val label: String,
        val teamIn: String?,
        val teamOut: String?,
    )

    data class BubbleSnapshot(
        val fieldSummary: String,
        val lines: List<BubbleLine>,
    )

    fun build(
        input: SelectionSundayInput,
        teamName: (String) -> String,
    ): BubbleSnapshot {
        val autoBids = input.conferenceChampions
            .filter { input.postseasonEligible[it] == true }
            .toSet()
        val eligibleNonChamps = input.netRankByTeam.keys
            .filter { input.postseasonEligible[it] == true && it !in autoBids }
            .sortedBy { input.netRankByTeam.getValue(it) }
        val direct = eligibleNonChamps.take(20)
        val opening = eligibleNonChamps.drop(20).take(24)
        val ncbaSet = autoBids + direct.toSet() + opening.toSet()
        val nciPool = input.netRankByTeam.keys
            .filter { input.postseasonEligible[it] == true && it !in ncbaSet }
            .sortedBy { input.netRankByTeam.getValue(it) }
            .take(32)
        val heritagePool = input.netRankByTeam.keys
            .filter {
                input.heritageEligible[it] == true &&
                    it !in ncbaSet &&
                    it !in nciPool.toSet()
            }
            .sortedBy { input.netRankByTeam.getValue(it) }
            .take(16)
        val lines = buildList {
            if (direct.size >= 20) {
                add(
                    BubbleLine(
                        label = "NCBA direct at-large (20th vs 21st)",
                        teamIn = teamName(direct[19]),
                        teamOut = eligibleNonChamps.getOrNull(20)?.let(teamName),
                    ),
                )
            }
            if (opening.size >= 24) {
                val openingTail = eligibleNonChamps.drop(20)
                add(
                    BubbleLine(
                        label = "NCBA Opening Round (24th vs 25th among non-champs)",
                        teamIn = teamName(openingTail[23]),
                        teamOut = eligibleNonChamps.getOrNull(44)?.let(teamName),
                    ),
                )
            }
            if (nciPool.size >= 32) {
                val nciEligible = input.netRankByTeam.keys
                    .filter { input.postseasonEligible[it] == true && it !in ncbaSet }
                    .sortedBy { input.netRankByTeam.getValue(it) }
                add(
                    BubbleLine(
                        label = "NCI field (32nd vs 33rd)",
                        teamIn = teamName(nciPool[31]),
                        teamOut = nciEligible.getOrNull(32)?.let(teamName),
                    ),
                )
            }
            if (heritagePool.size >= 16) {
                val heritageEligible = input.netRankByTeam.keys
                    .filter {
                        input.heritageEligible[it] == true &&
                            it !in ncbaSet &&
                            it !in nciPool.toSet()
                    }
                    .sortedBy { input.netRankByTeam.getValue(it) }
                add(
                    BubbleLine(
                        label = "Heritage Classic (16th vs 17th)",
                        teamIn = teamName(heritagePool[15]),
                        teamOut = heritageEligible.getOrNull(16)?.let(teamName),
                    ),
                )
            }
        }
        val summary = "NCBA ${autoBids.size + direct.size + opening.size} · NCI ${nciPool.size} · Heritage ${heritagePool.size}"
        return BubbleSnapshot(fieldSummary = summary, lines = lines)
    }
}