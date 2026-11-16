package dev.novux.dynasty.cbb.core.news

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/** Shell / hub feed lines show headline + date chip ([MEGA-SHELL-025]). */
object ShellNewsFeedDateChipPolicy {
    const val MEGA_ID: String = "MEGA-SHELL-025"

    private val formatter =
        DateTimeFormatter.ofPattern("MMM d", Locale.US).withZone(ZoneId.systemDefault())

    fun dateChip(createdAtMs: Long, rsWeekIndex: Int?): String =
        when {
            createdAtMs > 0L -> formatter.format(Instant.ofEpochMilli(createdAtMs))
            rsWeekIndex != null -> "W$rsWeekIndex"
            else -> ""
        }

    fun showChip(createdAtMs: Long, rsWeekIndex: Int?): Boolean =
        createdAtMs > 0L || rsWeekIndex != null
}