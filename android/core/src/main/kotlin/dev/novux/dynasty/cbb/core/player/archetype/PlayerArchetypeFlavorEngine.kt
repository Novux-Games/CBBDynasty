package dev.novux.dynasty.cbb.core.player.archetype

/** One-line Ratings tab flavor copy ([MEGA-COACH-076], [MEGA-COACH-166], [planning/player-archetypes.md]). */
object PlayerArchetypeFlavorEngine {
    const val MEGA_ID_RATINGS: String = "MEGA-COACH-166"
    const val MEGA_ID_SHEET: String = "MEGA-COACH-076"

    private val flavorByArchetype: Map<PlayerArchetype, String> = mapOf(
        PlayerArchetype.FloorGeneral to "Primary ballhandler; pick-and-roll orchestration.",
        PlayerArchetype.ComboGuard to "Score-first guard who can create and pass.",
        PlayerArchetype.ThreePointSpecialist to "Off-ball shooter with high three-point tendency.",
        PlayerArchetype.LockdownDefender to "Defensive stopper; lower usage, strong perimeter D.",
        PlayerArchetype.SlashingGuard to "Drives and finishes at the rim.",
        PlayerArchetype.Facilitator to "Distributor; assists over scoring.",
        PlayerArchetype.WingScorer to "Primary scoring wing with versatile shot creation.",
        PlayerArchetype.ThreeAndDWing to "Spot-up threes plus strong perimeter defense.",
        PlayerArchetype.PointForward to "Secondary initiator from the forward spot.",
        PlayerArchetype.StretchFour to "Spacing big on the perimeter.",
        PlayerArchetype.AthleticWing to "Transition finisher; cuts and athleticism.",
        PlayerArchetype.GlueGuy to "Role forward; defense and hustle.",
        PlayerArchetype.RimProtector to "Interior defense and lob finishes.",
        PlayerArchetype.PostScorer to "Back-to-basket paint touch.",
        PlayerArchetype.RollBig to "Pick-and-roll finisher; vertical spacing.",
        PlayerArchetype.StretchFive to "Outside shooting from the center spot.",
        PlayerArchetype.PaintBeast to "Boards, putbacks, and physical interior play.",
    )

    fun flavorLine(archetype: PlayerArchetype): String =
        flavorByArchetype[archetype].orEmpty()
}