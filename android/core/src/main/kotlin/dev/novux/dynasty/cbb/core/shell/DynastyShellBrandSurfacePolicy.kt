package dev.novux.dynasty.cbb.core.shell

import dev.novux.dynasty.cbb.core.GameConstants

/** Shell scaffold uses brand background token ([MEGA-SHELL-175]). */
object DynastyShellBrandSurfacePolicy {
    const val MEGA_ID: String = "MEGA-SHELL-175"

    fun shellBackgroundArgb(): Int = GameConstants.BRAND_BACKGROUND_HEX.toInt()
}