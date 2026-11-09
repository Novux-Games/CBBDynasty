package dev.novux.dynasty.cbb.core.save

/** Dynasty save slot policy — [docs/ADR-001-dynasty-save-backend.md]. */
object SaveSlots {
    const val COUNT = 10

    val INDICES: IntRange = 0 until COUNT
}
