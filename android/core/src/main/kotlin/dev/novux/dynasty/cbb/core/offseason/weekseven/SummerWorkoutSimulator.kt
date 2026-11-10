package dev.novux.dynasty.cbb.core.offseason.weekseven

import dev.novux.dynasty.cbb.core.roster.RosterPlayer
import kotlin.math.roundToInt
import kotlin.random.Random

data class SummerWorkoutReportLine(
    val playerId: String,
    val displayLabel: String,
    val gainPoints: Int,
    val summary: String,
)

data class SummerWorkoutReport(
    val coachTrainingGrade: Int,
    val lines: List<SummerWorkoutReportLine>,
    val teamAverageGain: Int,
)

/**
 * Auto-run summer workouts on returning roster ([planning/offseason.md#week-7--summer-workouts]).
 */
object SummerWorkoutSimulator {
    fun simulate(
        players: List<RosterPlayer>,
        dynastySeed: Long,
        coachTrainingGrade: Int = 72,
    ): SummerWorkoutReport {
        val returning = players.filter { !it.inTransferPortal }
        val random = Random(dynastySeed)
        val facilityBoost = 1.0 + (coachTrainingGrade - 50).coerceIn(0, 40) / 200.0
        val lines = returning.map { player ->
            val youthBoost = when (player.classYear.label) {
                "Fr" -> 1.15
                "So" -> 1.1
                "Jr" -> 1.0
                else -> 0.85
            }
            val base = 4 + random.nextInt(5)
            val gain = (base * facilityBoost * youthBoost).roundToInt().coerceIn(1, 12)
            val summary = when {
                gain >= 9 -> "Breakout summer"
                gain >= 6 -> "Solid development"
                else -> "Maintenance week"
            }
            SummerWorkoutReportLine(
                playerId = player.playerId,
                displayLabel = player.displayLabel(),
                gainPoints = gain,
                summary = summary,
            )
        }.sortedByDescending { it.gainPoints }
        val avg = if (lines.isEmpty()) 0 else lines.sumOf { it.gainPoints } / lines.size
        return SummerWorkoutReport(
            coachTrainingGrade = coachTrainingGrade,
            lines = lines,
            teamAverageGain = avg,
        )
    }
}
