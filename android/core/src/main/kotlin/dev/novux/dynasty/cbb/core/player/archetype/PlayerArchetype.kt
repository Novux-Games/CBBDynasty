package dev.novux.dynasty.cbb.core.player.archetype

import dev.novux.dynasty.cbb.core.roster.PlayerPosition

/** Primary archetype — [planning/player-archetypes.md]. */
enum class PlayerArchetype(
    val wire: String,
    val displayName: String,
    val positions: Set<PlayerPosition>,
) {
    FloorGeneral("floor_general", "Floor general", setOf(PlayerPosition.PointGuard)),
    ComboGuard("combo_guard", "Combo guard", setOf(PlayerPosition.PointGuard, PlayerPosition.ShootingGuard)),
    ThreePointSpecialist("three_specialist", "3-point specialist", setOf(PlayerPosition.PointGuard, PlayerPosition.ShootingGuard)),
    LockdownDefender("lockdown_guard", "Lockdown defender", setOf(PlayerPosition.PointGuard, PlayerPosition.ShootingGuard)),
    SlashingGuard("slashing_guard", "Slashing guard", setOf(PlayerPosition.PointGuard, PlayerPosition.ShootingGuard)),
    Facilitator("facilitator", "Facilitator", setOf(PlayerPosition.PointGuard, PlayerPosition.ShootingGuard)),
    WingScorer("wing_scorer", "Wing scorer", setOf(PlayerPosition.SmallForward, PlayerPosition.PowerForward)),
    ThreeAndDWing("three_and_d", "3-and-D wing", setOf(PlayerPosition.SmallForward, PlayerPosition.PowerForward)),
    PointForward("point_forward", "Point forward", setOf(PlayerPosition.SmallForward, PlayerPosition.PowerForward)),
    StretchFour("stretch_four", "Stretch four", setOf(PlayerPosition.PowerForward)),
    AthleticWing("athletic_wing", "Athletic wing", setOf(PlayerPosition.SmallForward, PlayerPosition.PowerForward)),
    GlueGuy("glue_guy", "Glue guy", setOf(PlayerPosition.SmallForward, PlayerPosition.PowerForward)),
    RimProtector("rim_protector", "Rim protector", setOf(PlayerPosition.Center)),
    PostScorer("post_scorer", "Post scorer", setOf(PlayerPosition.Center, PlayerPosition.PowerForward)),
    RollBig("roll_big", "Roll big", setOf(PlayerPosition.Center)),
    StretchFive("stretch_five", "Stretch five", setOf(PlayerPosition.Center)),
    PaintBeast("paint_beast", "Paint beast", setOf(PlayerPosition.Center, PlayerPosition.PowerForward)),
    ;

    companion object {
        fun fromWire(value: String): PlayerArchetype? = entries.firstOrNull { it.wire == value }

        fun forPosition(position: PlayerPosition): List<PlayerArchetype> =
            entries.filter { position in it.positions }
    }
}
