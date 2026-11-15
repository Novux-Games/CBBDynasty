package dev.novux.dynasty.cbb.core.school

/** School hub surfaces pack geography, arena, and colors ([MEGA-WORLD-110]–[112]). */
object SchoolHubPackIdentityPolicy {
    const val GEOGRAPHY_MEGA_ID: String = "MEGA-WORLD-110"
    const val ARENA_MEGA_ID: String = "MEGA-WORLD-111"
    const val COLOR_MEGA_ID: String = "MEGA-WORLD-112"

    fun geographyLine(city: String, state: String): String? =
        city.takeIf { it.isNotBlank() }?.let { c ->
            state.takeIf { it.isNotBlank() }?.let { s -> "$c, $s" }
        }

    fun arenaLine(arenaBaseName: String): String? =
        arenaBaseName.takeIf { it.isNotBlank() }

    fun parseColorArgb(hex: String): Long? = runCatching {
        val normalized = hex.removePrefix("#")
        require(normalized.length == 6) { "expected #RRGGBB" }
        normalized.toLong(16) or 0xFF000000L
    }.getOrNull()
}