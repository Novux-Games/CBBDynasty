package dev.novux.dynasty.cbb.core.coach

/** MEGA queue batch 36 — [MEGA-COACH-190]. */
object CoachPreviewOneLinerPolicy {
    const val MEGA_ID = "MEGA-COACH-190"
    fun oneLiner(school: String, role: String, record: String): String = "$school · $role · $record"
}
