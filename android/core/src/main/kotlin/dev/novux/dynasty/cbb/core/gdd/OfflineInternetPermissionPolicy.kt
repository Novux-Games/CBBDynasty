package dev.novux.dynasty.cbb.core.gdd

/** INTERNET permission is opt-in URL import only ([MEGA-GDD-092]). */
object OfflineInternetPermissionPolicy {
    const val MEGA_ID = "MEGA-GDD-092"

    const val RATIONALE =
        "Playable offline; INTERNET is used only when the user imports an identity pack from HTTPS."
}