package dev.novux.dynasty.cbb.core.brackets

import kotlinx.serialization.Serializable

/** NCBA bracket regions — [planning/selection-engine.md#seeds-5364--regions]. */
@Serializable
enum class NcbaRegionId {
    East,
    Midwest,
    South,
    West,
    ;

    companion object {
        val serpentineOrder: List<NcbaRegionId> = listOf(East, Midwest, South, West)
    }
}
