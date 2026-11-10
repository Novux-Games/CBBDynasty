package dev.novux.dynasty.cbb.core.awards

/** Display names for league honorees until full roster sim lands in 14.3. */
object AwardPlaceholderNames {
    fun leadingScorer(teamId: String, schoolName: String): String {
        val slot = kotlin.math.abs(teamId.hashCode()) % 3
        val role = when (slot) {
            0 -> "guard"
            1 -> "forward"
            else -> "center"
        }
        return "$schoolName star $role"
    }

    fun coachHonorName(schoolName: String): String = "$schoolName — head coach"
}
