package dev.novux.dynasty.cbb.core.recruiting

/**
 * National HS/JUCO pool draws home states from a broad US set (pack-driven breadth follows)
 * ([MEGA-RECR-012]).
 */
object NationalPoolGeographyPolicy {
    const val MEGA_ID: String = "MEGA-RECR-012"

    val usStates: List<String> = listOf(
        "AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA",
        "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD",
        "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ",
        "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC",
        "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY",
    )
}