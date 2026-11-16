package dev.novux.dynasty.cbb.core.save

import dev.novux.dynasty.cbb.core.pack.TeamDisplayOverlay

/**
 * Wizard school summary shows AD demeanor tier derived from program prestige until pack ships the field
 * ([MEGA-RECR-074]).
 */
object NewGameSchoolDemeanorPolicy {
    const val MEGA_ID: String = "MEGA-RECR-074"

    enum class DemeanorTier(val label: String) {
        Patient("Patient"),
        Neutral("Neutral"),
        Reactionary("Reactionary"),
    }

    fun tierForPrestige(prestige: Int): DemeanorTier = when {
        prestige >= 75 -> DemeanorTier.Patient
        prestige <= 35 -> DemeanorTier.Reactionary
        else -> DemeanorTier.Neutral
    }

    fun labelForSchool(school: TeamDisplayOverlay): String = tierForPrestige(school.programPrestige).label
}