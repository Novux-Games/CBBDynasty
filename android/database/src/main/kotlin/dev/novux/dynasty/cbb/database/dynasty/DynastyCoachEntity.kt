package dev.novux.dynasty.cbb.database.dynasty

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Head coach row created from the new-game wizard — [planning/coach-card.md]. */
@Entity(tableName = "dynasty_coach")
data class DynastyCoachEntity(
    @PrimaryKey val dynastyId: String,
    val userTeamId: String,
    val firstName: String,
    val lastName: String,
    val nameSuffixWire: String?,
    val age: Int,
    val almaMaterTeamId: String?,
    val skinToneId: String,
    val hairColorId: String,
    val hairStyleId: String,
    val beardStyleId: String,
    val avatarPresetId: String,
    val archetypeWire: String,
    val offensiveSchemeWire: String,
    val defensiveSchemeWire: String,
    val autoUpgrade: Boolean,
    val unlimitedCoachAge: Boolean,
)
