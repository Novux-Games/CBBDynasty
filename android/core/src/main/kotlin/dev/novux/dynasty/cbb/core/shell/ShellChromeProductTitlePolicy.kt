package dev.novux.dynasty.cbb.core.shell

import dev.novux.dynasty.cbb.core.GameConstants

/** Dynasty shell chrome uses full product title ([MEGA-SHELL-161]). */
object ShellChromeProductTitlePolicy {
    const val MEGA_ID: String = "MEGA-SHELL-161"

    fun chromeTitle(schoolName: String): String = "${GameConstants.GAME_TITLE}: $schoolName"
}