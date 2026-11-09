package dev.novux.dynasty.cbb.core.pack

private val MEMBERSHIP_YEAR_PATTERN = Regex("""^\d{4}-\d{2}$""")
private val HEX_COLOR_PATTERN = Regex("""^#[0-9A-Fa-f]{6}$""")

/**
 * Structural validation after JSON decode — atomic accept/reject per [planning/game-design.md].
 */
object IdentityPackValidator {
    fun validate(pack: IdentityPack): PackValidationError? {
        if (pack.packVersion != PackConstants.SUPPORTED_PACK_VERSION) {
            return PackValidationError.UnsupportedPackVersion(pack.packVersion)
        }
        if (!MEMBERSHIP_YEAR_PATTERN.matches(pack.membershipYear)) {
            return PackValidationError.InvalidMembershipYear(pack.membershipYear)
        }
        if (pack.conferences.isEmpty()) {
            return PackValidationError.EmptyCollection("conferences")
        }
        if (pack.teams.isEmpty()) {
            return PackValidationError.EmptyCollection("teams")
        }
        if (pack.rulesetYear < 2000) {
            return PackValidationError.InvalidConference("ruleset", "rulesetYear out of range")
        }

        val confIds = mutableSetOf<String>()
        for (conf in pack.conferences) {
            if (conf.id.isBlank()) return PackValidationError.BlankId("conference.id")
            if (!confIds.add(conf.id)) {
                return PackValidationError.DuplicateId("conference", conf.id)
            }
            if (conf.leagueGameCount <= 0) {
                return PackValidationError.InvalidConference(conf.id, "leagueGameCount must be positive")
            }
            if (conf.displayName.isBlank()) {
                return PackValidationError.InvalidConference(conf.id, "displayName required")
            }
            conf.tournamentTemplateId?.takeIf { it.isNotBlank() }?.let { templateId ->
                val exists = pack.tournamentTemplates.any { it.id == templateId }
                if (!exists) {
                    return PackValidationError.UnknownTournamentTemplate(conf.id, templateId)
                }
            }
        }

        val templateIds = mutableSetOf<String>()
        for (template in pack.tournamentTemplates) {
            if (template.id.isBlank()) return PackValidationError.BlankId("tournamentTemplate.id")
            if (!templateIds.add(template.id)) {
                return PackValidationError.DuplicateId("tournamentTemplate", template.id)
            }
            validateTemplate(template)?.let { return it }
        }

        val teamIds = mutableSetOf<String>()
        for (team in pack.teams) {
            if (team.id.isBlank()) return PackValidationError.BlankId("team.id")
            if (!teamIds.add(team.id)) {
                return PackValidationError.DuplicateId("team", team.id)
            }
            if (team.conferenceId !in confIds) {
                return PackValidationError.UnknownConference(team.id, team.conferenceId)
            }
            if (team.displayName.isBlank() || team.shortName.isBlank()) {
                return PackValidationError.InvalidTeam(team.id, "display/short name required")
            }
            if (!HEX_COLOR_PATTERN.matches(team.colors.primary) ||
                !HEX_COLOR_PATTERN.matches(team.colors.secondary)
            ) {
                return PackValidationError.InvalidTeam(team.id, "colors must be #RRGGBB")
            }
        }

        return null
    }

    private fun validateTemplate(template: TournamentTemplatePack): PackValidationError? {
        if (template.bracketSize < 2) {
            return PackValidationError.InvalidTemplate(template.id, "bracketSize must be >= 2")
        }
        if (template.seeds < 2 || template.seeds > template.bracketSize) {
            return PackValidationError.InvalidTemplate(template.id, "seeds out of range")
        }
        if (template.rounds.isEmpty()) {
            return PackValidationError.InvalidTemplate(template.id, "rounds required")
        }
        if (template.sitePolicy.isBlank() || template.qualificationPolicy.isBlank()) {
            return PackValidationError.InvalidTemplate(template.id, "site/qualification policy required")
        }
        template.rounds.forEach { round ->
            if (round.name.isBlank() || round.games <= 0) {
                return PackValidationError.InvalidTemplate(template.id, "invalid round entry")
            }
        }
        return null
    }
}
