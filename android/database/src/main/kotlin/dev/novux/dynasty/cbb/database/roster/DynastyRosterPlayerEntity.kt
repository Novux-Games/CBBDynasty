package dev.novux.dynasty.cbb.database.roster

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "dynasty_roster_player",
    indices = [Index(value = ["dynastyId"])],
)
data class DynastyRosterPlayerEntity(
    @PrimaryKey val playerId: String,
    val dynastyId: String,
    val positionWire: String,
    val classYearWire: String,
    val isStarter: Boolean,
    val depthOrder: Int,
    val inTransferPortal: Boolean = false,
)
