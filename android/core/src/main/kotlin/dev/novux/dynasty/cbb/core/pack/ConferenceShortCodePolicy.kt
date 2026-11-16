package dev.novux.dynasty.cbb.core.pack

/** Conference short codes for compact UI ([MEGA-WORLD-037], [MEGA-WORLD-128]). */
object ConferenceShortCodePolicy {
    const val MEGA_ID = "MEGA-WORLD-037"

    private val BY_CONF_ID = mapOf(
        "conf-nac" to "NAC",
        "conf-aucc" to "AUCC",
        "conf-cat10" to "CAT10",
        "conf-acrown" to "ACROWN",
        "conf-ascu" to "ASC-U",
        "conf-c12" to "C12",
        "conf-epc" to "EPC",
        "conf-nsc" to "NSC",
        "conf-psc" to "PSC",
        "conf-g10" to "G10",
        "conf-gwc" to "GWC",
        "conf-cca" to "CCA",
        "conf-ucc" to "UCC",
        "conf-lhl" to "LHL",
        "conf-fel" to "FEL",
        "conf-metro" to "METRO",
        "conf-mac" to "MAC",
        "conf-meac" to "MEAC",
        "conf-pvc" to "PVC",
        "conf-rmwc" to "RMWC",
        "conf-nec" to "NEC",
        "conf-ova" to "OVA",
        "conf-pr12" to "PR12",
        "conf-psl" to "PSL",
        "conf-sedc" to "SEDC",
        "conf-scf" to "SCF",
        "conf-gcc" to "GCC",
        "conf-scal" to "SCAL",
        "conf-summit" to "PSL",
        "conf-sbc" to "SBC",
        "conf-uac" to "UAC",
        "conf-wcc" to "WCC",
    )

    fun shortCode(conferenceId: String, displayName: String, packShort: String? = null): String =
        packShort?.takeIf { it.isNotBlank() }
            ?: BY_CONF_ID[conferenceId]
            ?: displayName.take(6).uppercase()
}