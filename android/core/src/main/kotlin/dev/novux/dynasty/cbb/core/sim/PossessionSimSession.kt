package dev.novux.dynasty.cbb.core.sim

import dev.novux.dynasty.cbb.core.injuries.GameSimInjuryEvent
import dev.novux.dynasty.cbb.core.injuries.MidGameInjurySampler
import dev.novux.dynasty.cbb.core.strategy.GameplanEffectsResolver
import dev.novux.dynasty.cbb.core.strategy.LateGameFoulStrategy
import dev.novux.dynasty.cbb.core.strategy.LateGamePossessionPolicy
import dev.novux.dynasty.cbb.core.strategy.GameplanUsageNormalizer
import dev.novux.dynasty.cbb.core.strategy.SchemeMatchupResolver
import kotlin.random.Random

data class PossessionStep(
    val logLine: String,
    val haltedAtHalftime: Boolean,
)

/**
 * Steppable possession loop for watch playback and halftime resume.
 */
class PossessionSimSession(
    input: GameSimInput,
    resume: MidGameState? = null,
    private val stopAtHalftime: Boolean = false,
    private val injuryRoll: GameSimInjuryRollInput? = null,
) {
    private val input: GameSimInput = input
    private var random: Random = resume?.random ?: Random(input.seed)
    private var gameClock: Int = resume?.gameClockSeconds ?: GameSimClock.REGULATION_SECONDS
    private var shotClockSeconds: Int = resume?.shotClockSecondsRemaining
        ?: PossessionShotClock.resetOnChangeOfPossession(input.tuning.shotClock)
    private var offenseHome: Boolean = resume?.offenseHome ?: random.nextBoolean()
    private var homeBox: TeamBoxScore = resume?.homeBox ?: TeamBoxScore(teamId = input.home.teamId)
    private var awayBox: TeamBoxScore = resume?.awayBox ?: TeamBoxScore(teamId = input.away.teamId)
    private val log: MutableList<String> = resume?.possessionLog?.toMutableList() ?: mutableListOf()
    private var totalPossessions: Int = resume?.totalPossessions ?: 0
    private val homeFoulLedger = PersonalFoulLedger(resume?.homePersonalFoulsByPlayer.orEmpty())
    private val awayFoulLedger = PersonalFoulLedger(resume?.awayPersonalFoulsByPlayer.orEmpty())
    private val homeMinutesLedger = SimFloorMinutesLedger(resume?.homeFloorSecondsByPlayer.orEmpty())
    private val awayMinutesLedger = SimFloorMinutesLedger(resume?.awayFloorSecondsByPlayer.orEmpty())
    private val homePlayerLedger = PlayerBoxScoreLedger(
        playerIds = input.homeRoster?.participants?.map { it.playerId }.orEmpty(),
        initial = resume?.homePlayerBox.orEmpty(),
    )
    private val awayPlayerLedger = PlayerBoxScoreLedger(
        playerIds = input.awayRoster?.participants?.map { it.playerId }.orEmpty(),
        initial = resume?.awayPlayerBox.orEmpty(),
    )
    private val homeInjuredOut = (resume?.homeInjuredPlayerIds ?: emptySet()).toMutableSet()
    private val awayInjuredOut = (resume?.awayInjuredPlayerIds ?: emptySet()).toMutableSet()
    private val homeMidInjuries = (resume?.homeMidGameInjuries ?: emptyList()).toMutableList()
    private val awayMidInjuries = (resume?.awayMidGameInjuries ?: emptyList()).toMutableList()
    private var inOvertime: Boolean = resume?.inOvertime ?: false
    private var overtimePeriods: Int = resume?.overtimePeriods ?: 0

    fun hasNext(): Boolean = gameClock > 0

    fun isTiedAtBuzz(): Boolean =
        gameClock <= 0 && homeBox.points == awayBox.points

    fun beginOvertimePeriod() {
        check(isTiedAtBuzz()) { "Cannot start OT unless regulation/OT period ended tied" }
        overtimePeriods += 1
        inOvertime = true
        gameClock = GameSimClock.OVERTIME_SECONDS
        shotClockSeconds = PossessionShotClock.resetOnChangeOfPossession(input.tuning.shotClock)
        offenseHome = random.nextBoolean()
        log += "— OT$overtimePeriods (5:00) — ${homeBox.points}-${awayBox.points}"
    }

    fun periodsPlayedTotal(): Int = 1 + overtimePeriods

    fun latestLogLine(): String = log.last()

    fun step(): PossessionStep {
        check(hasNext()) { "No possessions remaining" }
        val sides = if (offenseHome) {
            PossessionSimEngine.Sides(input.home, input.away, homeBox, "HOME")
        } else {
            PossessionSimEngine.Sides(input.away, input.home, awayBox, "AWAY")
        }
        val schemeEffects = SchemeMatchupResolver.resolve(
            sides.offense.offensiveScheme,
            sides.defense.defensiveScheme,
        )
        val offGameplan = if (offenseHome) {
            input.activeHomeGameplan(gameClock)
        } else {
            input.activeAwayGameplan(gameClock)
        }
        val defGameplan = if (offenseHome) {
            input.activeAwayGameplan(gameClock)
        } else {
            input.activeHomeGameplan(gameClock)
        }
        val defenseScore = if (offenseHome) awayBox.points else homeBox.points
        val offenseScore = if (offenseHome) homeBox.points else awayBox.points
        val usagePeak = GameplanUsageNormalizer.normalize(offGameplan?.usageWeights.orEmpty())
            .values
            .maxOrNull() ?: 0.2
        val effects = GameplanEffectsResolver.mergeInto(
            schemeEffects,
            GameplanEffectsResolver.offenseTweaks(offGameplan, usagePeak),
            GameplanEffectsResolver.defenseTweaks(defGameplan, gameClock, defenseScore, offenseScore),
        )
        val lateDecision = LateGamePossessionPolicy.defenseDecision(
            strategy = defGameplan?.lateGameFoulStrategy ?: LateGameFoulStrategy.Standard,
            gameClockSeconds = gameClock,
            defenseScore = defenseScore,
            offenseScore = offenseScore,
        )
        val tripSample = PossessionSimEngine.sampleTripForLateGame(input.tuning, random, lateDecision)
        val tripSeconds = PossessionSimEngine.pacedTripSecondsFromRaw(
            input.tuning,
            tripSample.rawSeconds,
            effects.tripPaceScale,
        )
        val shotClockBefore = shotClockSeconds
        val used = PossessionShotClock.secondsConsumed(tripSeconds, shotClockSeconds, gameClock)
        gameClock -= used
        val shotClockAfterTrip = PossessionShotClock.remainingAfterTrip(shotClockBefore, used)
        totalPossessions++

        val offRating = teamOffensiveRating(offenseIsHome = offenseHome) + effects.offensiveRatingDelta
        val defRating = teamDefensiveRating(defenseIsHome = !offenseHome) + effects.defensiveRatingDelta
        val resolution = if (
            PossessionShotClock.isShotClockViolation(shotClockBefore, used, tripSeconds)
        ) {
            PossessionSimEngine.resolveShotClockViolation(sides.box)
        } else {
            PossessionSimEngine.resolvePossessionForSession(
                sides.box,
                offRating,
                defRating,
                effects,
                random,
            )
        }
        var offenseBoxAfter = resolution.box
        val offenseRosterPresent = if (offenseHome) input.homeRoster != null else input.awayRoster != null
        if (
            offenseRosterPresent &&
            PlayerPossessionStatRecorder.shouldShootBonusFreeThrows(sides.box, offenseBoxAfter, tripSample.foulStoppage)
        ) {
            val (makes, attempts) = PlayerPossessionStatRecorder.sampleBonusFreeThrows(random)
            offenseBoxAfter = offenseBoxAfter.copy(
                points = offenseBoxAfter.points + makes,
                freeThrowsMade = offenseBoxAfter.freeThrowsMade + makes,
                freeThrowsAttempted = offenseBoxAfter.freeThrowsAttempted + attempts,
            )
        }
        val homeFloorIds = floorPlayerIds(home = true, excludeInjured = false)
        val awayFloorIds = floorPlayerIds(home = false, excludeInjured = false)
        val offenseFloor = fallbackFloor(
            if (offenseHome) homeFloorIds else awayFloorIds,
            if (offenseHome) input.homeRoster else input.awayRoster,
        )
        val defenseFloor = fallbackFloor(
            if (offenseHome) awayFloorIds else homeFloorIds,
            if (offenseHome) input.awayRoster else input.homeRoster,
        )
        val offenseLedger = if (offenseHome) homePlayerLedger else awayPlayerLedger
        val defenseLedger = if (offenseHome) awayPlayerLedger else homePlayerLedger
        val usageWeights = offGameplan?.usageWeights.orEmpty()
        PlayerPossessionStatRecorder.record(
            offenseLedger = offenseLedger,
            defenseLedger = defenseLedger,
            offenseFloor = offenseFloor,
            defenseFloor = defenseFloor,
            usageWeights = usageWeights,
            before = sides.box,
            after = offenseBoxAfter,
            change = resolution.change,
            turnoverTerminal = resolution.turnoverTerminal,
            foulStoppage = tripSample.foulStoppage,
            random = random,
        )
        if (offenseHome) {
            homeBox = offenseBoxAfter
        } else {
            awayBox = offenseBoxAfter
        }
        when (resolution.change) {
            PossessionChange.DefenseTakesBall -> {
                offenseHome = !offenseHome
                shotClockSeconds = PossessionShotClock.resetOnChangeOfPossession(input.tuning.shotClock)
            }
            PossessionChange.OffenseRetainsOffensiveRebound -> {
                shotClockSeconds = PossessionShotClock.resetAfterOffensiveRebound(input.tuning.shotClock)
            }
        }
        accrueFloorMinutes(used, offenseHome)
        val newInjuryLines = sampleMidGameInjuries(homeFloorIds, awayFloorIds)
        val foulNote = recordPersonalFoulIfNeeded(tripSample.foulStoppage, offenseHome)
        val line = PossessionSimEngine.formatPossessionLog(
            used,
            gameClock,
            sides.label,
            sides.box,
            resolution.box,
            shotClockBefore = shotClockBefore,
            shotClockAfter = shotClockAfterTrip,
            change = resolution.change,
            turnoverTerminal = resolution.turnoverTerminal,
            foulNote = foulNote,
            lateGameTag = LateGamePossessionPolicy.logTag(lateDecision),
        )
        log += line
        newInjuryLines.forEach { log += it }
        val halftime = stopAtHalftime && !inOvertime && gameClock <= GameSimClock.HALF_SECONDS
        return PossessionStep(line, haltedAtHalftime = halftime)
    }

    private fun sampleMidGameInjuries(
        homeFloorIds: List<String>,
        awayFloorIds: List<String>,
    ): List<String> {
        val roll = injuryRoll ?: return emptyList()
        val lines = mutableListOf<String>()
        roll.homeRosterPlayers?.let { roster ->
            MidGameInjurySampler.tryTeamPossession(
                phase = roll.phase,
                floorPlayerIds = homeFloorIds.filter { it !in homeInjuredOut },
                minutesLedger = homeMinutesLedger,
                roster = roster,
                alreadyInjuredIds = homeInjuredOut,
                injuriesThisTeam = homeMidInjuries.size,
                bank = roll.bank,
                tuning = roll.occurrence,
                medicalStaffGrade = roll.medicalStaffGrade,
                gameClockSeconds = gameClock,
                possessionIndex = totalPossessions,
                teamSeed = input.seed xor input.home.teamId.hashCode().toLong(),
                random = random,
            )?.let { event ->
                homeMidInjuries += event
                homeInjuredOut += event.playerId
                val label = roster.firstOrNull { it.playerId == event.playerId }?.displayLabel()
                    ?: event.playerId
                lines += MidGameInjurySampler.formatLogLine(label, event.label)
            }
        }
        roll.awayRosterPlayers?.let { roster ->
            MidGameInjurySampler.tryTeamPossession(
                phase = roll.phase,
                floorPlayerIds = awayFloorIds.filter { it !in awayInjuredOut },
                minutesLedger = awayMinutesLedger,
                roster = roster,
                alreadyInjuredIds = awayInjuredOut,
                injuriesThisTeam = awayMidInjuries.size,
                bank = roll.bank,
                tuning = roll.occurrence,
                medicalStaffGrade = roll.medicalStaffGrade,
                gameClockSeconds = gameClock,
                possessionIndex = totalPossessions,
                teamSeed = input.seed xor input.away.teamId.hashCode().toLong(),
                random = random,
            )?.let { event ->
                awayMidInjuries += event
                awayInjuredOut += event.playerId
                val label = roster.firstOrNull { it.playerId == event.playerId }?.displayLabel()
                    ?: event.playerId
                lines += MidGameInjurySampler.formatLogLine(label, event.label)
            }
        }
        return lines
    }

    private fun teamOffensiveRating(offenseIsHome: Boolean): Int {
        val roster = if (offenseIsHome) input.homeRoster else input.awayRoster
        val foulLedger = if (offenseIsHome) homeFoulLedger else awayFoulLedger
        val minutesLedger = if (offenseIsHome) homeMinutesLedger else awayMinutesLedger
        return roster?.weightedOffensiveOnFloor(foulLedger, minutesLedger, gameClock)
            ?: if (offenseIsHome) input.homeOffensive() else input.awayOffensive()
    }

    private fun teamDefensiveRating(defenseIsHome: Boolean): Int {
        val roster = if (defenseIsHome) input.homeRoster else input.awayRoster
        val foulLedger = if (defenseIsHome) homeFoulLedger else awayFoulLedger
        val minutesLedger = if (defenseIsHome) homeMinutesLedger else awayMinutesLedger
        return roster?.weightedDefensiveOnFloor(foulLedger, minutesLedger, gameClock)
            ?: if (defenseIsHome) input.homeDefensive() else input.awayDefensive()
    }

    private fun fallbackFloor(ids: List<String>, roster: GameSimRosterInput?): List<String> =
        ids.ifEmpty {
            roster?.participants?.map { it.playerId }?.take(FoulTroubleRotationPolicy.FLOOR_PLAYERS).orEmpty()
        }

    private fun floorPlayerIds(home: Boolean, excludeInjured: Boolean = true): List<String> {
        val roster = if (home) input.homeRoster else input.awayRoster
        if (roster == null) return emptyList()
        val foulLedger = if (home) homeFoulLedger else awayFoulLedger
        val minutesLedger = if (home) homeMinutesLedger else awayMinutesLedger
        val injured = if (home) homeInjuredOut else awayInjuredOut
        return SimFloorRotationPolicy.floorParticipants(roster, foulLedger, minutesLedger, gameClock)
            .map { it.playerId }
            .filter { !excludeInjured || it !in injured }
    }

    private fun accrueFloorMinutes(usedSeconds: Int, offenseIsHome: Boolean) {
        input.homeRoster?.let { homeRoster ->
            val homeFloor = SimFloorRotationPolicy.floorParticipants(
                homeRoster,
                homeFoulLedger,
                homeMinutesLedger,
                gameClock,
            ).map { it.playerId }.filter { it !in homeInjuredOut }
            homeMinutesLedger.addFloorTime(homeFloor, usedSeconds)
        }
        input.awayRoster?.let { awayRoster ->
            val awayFloor = SimFloorRotationPolicy.floorParticipants(
                awayRoster,
                awayFoulLedger,
                awayMinutesLedger,
                gameClock,
            ).map { it.playerId }.filter { it !in awayInjuredOut }
            awayMinutesLedger.addFloorTime(awayFloor, usedSeconds)
        }
    }

    private fun recordPersonalFoulIfNeeded(foulStoppage: Boolean, offenseIsHome: Boolean): String? {
        if (!foulStoppage) return null
        val defendingRoster = (if (offenseIsHome) input.awayRoster else input.homeRoster) ?: return null
        val ledger = if (offenseIsHome) awayFoulLedger else homeFoulLedger
        val playerId = PersonalFoulAssignment.pickPlayer(defendingRoster, ledger, random) ?: return null
        val record = ledger.record(playerId)
        if (offenseIsHome) {
            awayBox = awayBox.copy(personalFouls = awayBox.personalFouls + 1)
        } else {
            homeBox = homeBox.copy(personalFouls = homeBox.personalFouls + 1)
        }
        return if (record.disqualified) {
            "PF $playerId (${record.foulCount}) DQ"
        } else {
            "PF $playerId (${record.foulCount})"
        }
    }

    fun toMidGame(): MidGameState = MidGameState(
        random = random,
        gameClockSeconds = gameClock,
        offenseHome = offenseHome,
        homeBox = homeBox,
        awayBox = awayBox,
        possessionLog = log.toList(),
        totalPossessions = totalPossessions,
        shotClockSecondsRemaining = shotClockSeconds,
        homePersonalFoulsByPlayer = homeFoulLedger.snapshot(),
        awayPersonalFoulsByPlayer = awayFoulLedger.snapshot(),
        homeFloorSecondsByPlayer = homeMinutesLedger.snapshot(),
        awayFloorSecondsByPlayer = awayMinutesLedger.snapshot(),
        homePlayerBox = homePlayerLedger.snapshotMap(),
        awayPlayerBox = awayPlayerLedger.snapshotMap(),
        homeInjuredPlayerIds = homeInjuredOut.toSet(),
        awayInjuredPlayerIds = awayInjuredOut.toSet(),
        homeMidGameInjuries = homeMidInjuries.toList(),
        awayMidGameInjuries = awayMidInjuries.toList(),
        inOvertime = inOvertime,
        overtimePeriods = overtimePeriods,
    )

    fun toResult(isComplete: Boolean): GameSimResult {
        syncPlayerBoxAuxiliaryStats()
        return GameSimResult(
        homeScore = homeBox.points,
        awayScore = awayBox.points,
        homeBox = homeBox,
        awayBox = awayBox,
        totalPossessions = totalPossessions,
        possessionLog = log.toList(),
        isComplete = isComplete,
        midGame = if (isComplete) null else toMidGame(),
        homePersonalFoulsByPlayer = homeFoulLedger.snapshot(),
        awayPersonalFoulsByPlayer = awayFoulLedger.snapshot(),
        homeFloorSecondsByPlayer = homeMinutesLedger.snapshot(),
        awayFloorSecondsByPlayer = awayMinutesLedger.snapshot(),
        homePlayerBox = homePlayerLedger.snapshot(),
        awayPlayerBox = awayPlayerLedger.snapshot(),
        homeInjuries = homeMidInjuries.toList(),
        awayInjuries = awayMidInjuries.toList(),
        periodsPlayed = periodsPlayedTotal(),
        overtimePeriods = overtimePeriods,
        )
    }

    private fun syncPlayerBoxAuxiliaryStats() {
        homeFoulLedger.snapshot().forEach { (id, pf) -> homePlayerLedger.setPersonalFouls(id, pf) }
        awayFoulLedger.snapshot().forEach { (id, pf) -> awayPlayerLedger.setPersonalFouls(id, pf) }
        homeMinutesLedger.snapshot().forEach { (id, sec) -> homePlayerLedger.setMinutesSeconds(id, sec) }
        awayMinutesLedger.snapshot().forEach { (id, sec) -> awayPlayerLedger.setMinutesSeconds(id, sec) }
        PlayerBoxReconciler.reconcile(homeBox, homePlayerLedger)
        PlayerBoxReconciler.reconcile(awayBox, awayPlayerLedger)
    }
}
