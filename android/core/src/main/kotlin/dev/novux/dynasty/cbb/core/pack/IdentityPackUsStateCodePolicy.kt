package dev.novux.dynasty.cbb.core.pack

/** Validates US `homeState` codes on identity pack import ([MEGA-WORLD-041]). */
object IdentityPackUsStateCodePolicy {
    const val MEGA_ID: String = "MEGA-WORLD-041"

    private val usStates = setOf(
        "AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA",
        "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD",
        "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ",
        "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC",
        "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY",
        "DC",
    )

    fun isValid(state: String): Boolean = state.uppercase() in usStates

    fun normalize(state: String): String = state.trim().uppercase()
}