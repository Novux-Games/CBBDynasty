package dev.novux.dynasty.cbb.core.coach.staff

import kotlin.math.roundToInt

/** Aggregate staff grade chip for School hub Staff tab ([MEGA-COACH-053], [planning/game-design.md]). */
object SchoolStaffGradeEngine {
    const val MEGA_ID: String = "MEGA-COACH-053"

    fun aggregateGrade(roster: SchoolStaffRoster): Int {
        val hcTraining = roster.headCoach.ratings.training
        val assistantMean: Double = if (roster.assistants.isEmpty()) {
            hcTraining.toDouble()
        } else {
            roster.assistants
                .map { (it.ratings.recruiting + it.ratings.development + it.ratings.scouting) / 3.0 }
                .average()
        }
        val supportMean: Double = if (roster.supportStaff.isEmpty()) {
            assistantMean
        } else {
            roster.supportStaff.map { it.contributionRating.toDouble() }.average()
        }
        val roleFill = (roster.assistants.size + roster.supportStaff.size).coerceAtMost(7)
        val blended = hcTraining.toDouble() * 0.35 +
            assistantMean * 0.40 +
            supportMean * 0.15 +
            roleFill.toDouble() * 1.8
        return blended.roundToInt().coerceIn(0, 99)
    }
}