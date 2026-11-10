package dev.novux.dynasty.cbb.core.discipline

import dev.novux.dynasty.cbb.core.balance.BalanceDisciplineTuning
import dev.novux.dynasty.cbb.core.roster.RosterPlayer

object DisciplineResolver {
    fun apply(
        players: List<RosterPlayer>,
        incident: PendingDisciplineIncident,
        choice: DisciplineResolutionChoice,
        suspendGames: Int? = null,
        tuning: BalanceDisciplineTuning,
        drawSeed: Long = incident.playerId.hashCode().toLong(),
    ): DisciplineResolutionResult {
        val player = players.firstOrNull { it.playerId == incident.playerId }
            ?: return DisciplineResolutionResult(players)
        return when (choice) {
            DisciplineResolutionChoice.Dismiss -> {
                DisciplineResolutionResult(players.filter { it.playerId != player.playerId }, cutPlayerIds = listOf(player.playerId))
            }
            DisciplineResolutionChoice.SuspendGames -> {
                val games = suspendGames?.coerceIn(1, 10) ?: 1
                DisciplineResolutionResult(
                    players.map {
                        if (it.playerId == player.playerId) {
                            it.copy(
                                gamesSuspendedRemaining = games,
                                conductIncidentId = incident.conductTemplateId,
                                gpaCurrent = incident.gpaCurrent ?: it.gpaCurrent,
                            )
                        } else {
                            it
                        }
                    },
                    gamesSuspended = games,
                )
            }
            DisciplineResolutionChoice.ConferenceDraw -> {
                val (outcome, games) = ConferenceDrawEngine.resolve(tuning.conferenceDraw, drawSeed)
                when (outcome) {
                    ConferenceDrawOutcome.Cleared ->
                        DisciplineResolutionResult(players, conferenceOutcome = outcome)
                    ConferenceDrawOutcome.Dismissed ->
                        DisciplineResolutionResult(
                            players.filter { it.playerId != player.playerId },
                            cutPlayerIds = listOf(player.playerId),
                            conferenceOutcome = outcome,
                        )
                    ConferenceDrawOutcome.ShortSuspension,
                    ConferenceDrawOutcome.LongSuspension,
                    -> {
                        val g = games ?: 1
                        DisciplineResolutionResult(
                            players.map {
                                if (it.playerId == player.playerId) {
                                    it.copy(gamesSuspendedRemaining = g, conductIncidentId = incident.conductTemplateId)
                                } else {
                                    it
                                }
                            },
                            conferenceOutcome = outcome,
                            gamesSuspended = g,
                        )
                    }
                }
            }
            DisciplineResolutionChoice.AcademicSweep -> {
                DisciplineResolutionResult(
                    players.map {
                        if (it.playerId == player.playerId) {
                            it.copy(academicCoverUp = true, gpaCurrent = incident.gpaCurrent ?: it.gpaCurrent)
                        } else {
                            it
                        }
                    },
                )
            }
            DisciplineResolutionChoice.AcademicSuspendGames -> {
                val games = suspendGames?.coerceIn(1, 10) ?: 2
                DisciplineResolutionResult(
                    players.map {
                        if (it.playerId == player.playerId) {
                            it.copy(
                                gamesSuspendedRemaining = games,
                                gpaCurrent = incident.gpaCurrent ?: it.gpaCurrent,
                            )
                        } else {
                            it
                        }
                    },
                    gamesSuspended = games,
                )
            }
            DisciplineResolutionChoice.AcademicHoldSupport -> {
                DisciplineResolutionResult(
                    players.map {
                        if (it.playerId == player.playerId) {
                            it.copy(
                                academicHold = true,
                                gpaCurrent = incident.gpaCurrent ?: it.gpaCurrent,
                            )
                        } else {
                            it
                        }
                    },
                )
            }
        }
    }

    fun tickAcademicSupport(
        players: List<RosterPlayer>,
        weeklyGain: Double,
    ): List<RosterPlayer> =
        players.map { player ->
            if (!player.academicHold) return@map player
            val nextGpa = (player.gpaCurrent + weeklyGain).coerceAtMost(4.0)
            if (nextGpa >= 2.0) {
                player.copy(gpaCurrent = nextGpa, academicHold = false)
            } else {
                player.copy(gpaCurrent = nextGpa)
            }
        }

    fun decrementGameSuspensions(players: List<RosterPlayer>, teamPlayedGame: Boolean): List<RosterPlayer> {
        if (!teamPlayedGame) return players
        return players.map { player ->
            if (player.gamesSuspendedRemaining <= 0) return@map player
            player.copy(gamesSuspendedRemaining = player.gamesSuspendedRemaining - 1)
        }
    }

    fun isIneligibleForLineup(player: RosterPlayer): Boolean =
        player.academicHold ||
            player.gamesSuspendedRemaining > 0 ||
            (player.injuryActive && player.injuryWeeksRemaining > 0) ||
            player.medicalRedshirtSeason
}
