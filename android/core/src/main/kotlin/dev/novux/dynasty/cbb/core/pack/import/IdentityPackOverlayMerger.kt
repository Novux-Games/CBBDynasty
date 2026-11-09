package dev.novux.dynasty.cbb.core.pack.import

import dev.novux.dynasty.cbb.core.pack.ConferencePack
import dev.novux.dynasty.cbb.core.pack.IdentityPack
import dev.novux.dynasty.cbb.core.pack.TeamPack
import dev.novux.dynasty.cbb.core.pack.TournamentTemplatePack

/**
 * Applies TXT `mode=overlay` patches — stable ids from embedded default, display fields from import.
 */
object IdentityPackOverlayMerger {
    fun merge(base: IdentityPack, overlay: IdentityPack): IdentityPack {
        val confById = base.conferences.associateBy { it.id }.toMutableMap()
        overlay.conferences.forEach { patch ->
            val existing = confById[patch.id]
                ?: throw IllegalArgumentException("Unknown conference id in overlay: ${patch.id}")
            confById[patch.id] = mergeConference(existing, patch)
        }

        val teamById = base.teams.associateBy { it.id }.toMutableMap()
        overlay.teams.forEach { patch ->
            val existing = teamById[patch.id]
                ?: throw IllegalArgumentException("Unknown team id in overlay: ${patch.id}")
            teamById[patch.id] = mergeTeam(existing, patch)
        }

        val templateById = base.tournamentTemplates.associateBy { it.id }.toMutableMap()
        overlay.tournamentTemplates.forEach { patch ->
            val existing = templateById[patch.id]
            templateById[patch.id] = existing?.let { mergeTemplate(it, patch) } ?: patch
        }

        return base.copy(
            membershipYear = overlay.membershipYear.ifBlank { base.membershipYear },
            disclaimerAcceptedRequired = overlay.disclaimerAcceptedRequired,
            conferences = base.conferences.map { confById[it.id]!! },
            teams = base.teams.map { teamById[it.id]!! },
            tournamentTemplates = templateById.values.toList(),
            rulesetYear = overlay.rulesetYear,
            packId = overlay.packId ?: base.packId,
        )
    }

    private fun mergeConference(existing: ConferencePack, patch: ConferencePack): ConferencePack =
        existing.copy(
            displayName = patch.displayName.ifBlank { existing.displayName },
            leagueGameCount = patch.leagueGameCount,
            standingsTiebreakers = patch.standingsTiebreakers.ifEmpty { existing.standingsTiebreakers },
            tournamentTemplateId = patch.tournamentTemplateId ?: existing.tournamentTemplateId,
            ncaaAutoBidSlot = patch.ncaaAutoBidSlot,
        )

    private fun mergeTeam(existing: TeamPack, patch: TeamPack): TeamPack =
        existing.copy(
            conferenceId = patch.conferenceId.ifBlank { existing.conferenceId },
            displayName = patch.displayName.ifBlank { existing.displayName },
            shortName = patch.shortName.ifBlank { existing.shortName },
            homeCity = patch.homeCity.ifBlank { existing.homeCity },
            homeState = patch.homeState.ifBlank { existing.homeState },
            postseasonEligible = patch.postseasonEligible,
            reclassTransitionYear = patch.reclassTransitionYear ?: existing.reclassTransitionYear,
            colors = patch.colors,
            arenaBaseName = patch.arenaBaseName.ifBlank { existing.arenaBaseName },
            logoUri = patch.logoUri ?: existing.logoUri,
        )

    private fun mergeTemplate(
        existing: TournamentTemplatePack,
        patch: TournamentTemplatePack,
    ): TournamentTemplatePack =
        patch.copy(
            bracketSize = if (patch.bracketSize > 0) patch.bracketSize else existing.bracketSize,
            seeds = if (patch.seeds > 0) patch.seeds else existing.seeds,
        )
}
