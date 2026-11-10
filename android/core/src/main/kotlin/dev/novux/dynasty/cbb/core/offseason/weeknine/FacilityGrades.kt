package dev.novux.dynasty.cbb.core.offseason.weeknine

data class FacilityGrades(
    val grades: Map<FacilityCategory, Int> = FacilityCategory.entries.associateWith { DEFAULT_GRADE },
) {
    fun grade(category: FacilityCategory): Int =
        grades[category]?.coerceIn(
            OffseasonWeekNineConstants.FACILITY_GRADE_MIN,
            OffseasonWeekNineConstants.FACILITY_GRADE_MAX,
        ) ?: DEFAULT_GRADE

    fun withUpgrade(category: FacilityCategory): FacilityGrades {
        val current = grade(category)
        if (current >= OffseasonWeekNineConstants.FACILITY_GRADE_MAX) return this
        return copy(grades = grades + (category to current + 1))
    }

    fun encode(): String =
        FacilityCategory.entries.joinToString("|") { "${it.key}=${grade(it)}" }

    companion object {
        const val DEFAULT_GRADE: Int = 5

        fun decode(raw: String?): FacilityGrades {
            if (raw.isNullOrBlank()) return FacilityGrades()
            val map = mutableMapOf<FacilityCategory, Int>()
            raw.split("|").forEach { part ->
                val split = part.split("=")
                if (split.size == 2) {
                    val cat = FacilityCategory.entries.firstOrNull { it.key == split[0] } ?: return@forEach
                    map[cat] = split[1].toIntOrNull()?.coerceIn(
                        OffseasonWeekNineConstants.FACILITY_GRADE_MIN,
                        OffseasonWeekNineConstants.FACILITY_GRADE_MAX,
                    ) ?: DEFAULT_GRADE
                }
            }
            return FacilityGrades(FacilityCategory.entries.associateWith { map[it] ?: DEFAULT_GRADE })
        }
    }
}
