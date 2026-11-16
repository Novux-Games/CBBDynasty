package dev.novux.dynasty.cbb.core.sim

/** Win shares roll at EoS — not on box score ([MEGA-SIM-174]). */
object WinSharesUiDocPolicy {
    const val MEGA_ID = "MEGA-SIM-174"

    const val HINT =
        "Win shares are computed at season end, not on in-game box scores (see simulation-engine season rollup)."
}