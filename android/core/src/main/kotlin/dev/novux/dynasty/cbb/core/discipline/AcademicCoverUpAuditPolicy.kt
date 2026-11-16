package dev.novux.dynasty.cbb.core.discipline

/** End-of-season cover-up audit roll ([MEGA-SHELL-069]). */
object AcademicCoverUpAuditPolicy {
    const val MEGA_ID: String = "MEGA-SHELL-069"

    fun shouldRollAudit(academicCoverUp: Boolean, auditBaseChance: Double, roll: Double): Boolean =
        academicCoverUp && roll < auditBaseChance
}