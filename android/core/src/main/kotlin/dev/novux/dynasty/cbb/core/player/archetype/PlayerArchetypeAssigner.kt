package dev.novux.dynasty.cbb.core.player.archetype

import dev.novux.dynasty.cbb.core.player.PlayerRatings
import dev.novux.dynasty.cbb.core.roster.PlayerPosition

object PlayerArchetypeAssigner {
    fun assign(position: PlayerPosition, ratings: PlayerRatings): PlayerArchetype {
        val shooting = (ratings.shootingInside + ratings.shootingMid + ratings.shootingThree) / 3
        val handlePass = (ratings.handling + ratings.passing) / 2
        val defense = (ratings.perimeterDefense + ratings.interiorDefense + ratings.stealing) / 3
        val athletic = (ratings.blocking + ratings.rebounding) / 2
        return when (position) {
            PlayerPosition.PointGuard, PlayerPosition.ShootingGuard -> assignGuard(
                position,
                shooting,
                handlePass,
                defense,
                ratings.off,
            )
            PlayerPosition.SmallForward, PlayerPosition.PowerForward -> assignForward(
                position,
                shooting,
                handlePass,
                defense,
                athletic,
            )
            PlayerPosition.Center -> assignCenter(shooting, defense, athletic, ratings.rebounding)
        }
    }

    private fun assignGuard(
        position: PlayerPosition,
        shooting: Int,
        handlePass: Int,
        defense: Int,
        off: Int,
    ): PlayerArchetype {
        val options = PlayerArchetype.forPosition(position)
        return when {
            defense >= shooting + 5 && defense >= handlePass -> PlayerArchetype.LockdownDefender
            shooting >= handlePass + 8 -> PlayerArchetype.ThreePointSpecialist
            handlePass >= shooting + 6 && position == PlayerPosition.PointGuard -> PlayerArchetype.FloorGeneral
            handlePass >= off - 2 -> PlayerArchetype.Facilitator
            off >= shooting + 5 -> PlayerArchetype.SlashingGuard
            off >= handlePass -> PlayerArchetype.ComboGuard
            else -> options.firstOrNull { it == PlayerArchetype.Facilitator } ?: PlayerArchetype.ComboGuard
        }
    }

    private fun assignForward(
        position: PlayerPosition,
        shooting: Int,
        handlePass: Int,
        defense: Int,
        athletic: Int,
    ): PlayerArchetype {
        return when {
            shooting >= defense + 6 && position == PlayerPosition.PowerForward -> PlayerArchetype.StretchFour
            shooting >= defense + 4 && defense >= 70 -> PlayerArchetype.ThreeAndDWing
            handlePass >= shooting + 4 -> PlayerArchetype.PointForward
            athletic >= shooting + 3 -> PlayerArchetype.AthleticWing
            shooting >= handlePass + 5 -> PlayerArchetype.WingScorer
            defense >= shooting -> PlayerArchetype.GlueGuy
            else -> PlayerArchetype.WingScorer
        }
    }

    private fun assignCenter(
        shooting: Int,
        defense: Int,
        athletic: Int,
        rebounding: Int,
    ): PlayerArchetype = when {
        shooting >= defense + 8 -> PlayerArchetype.StretchFive
        defense >= shooting + 6 -> PlayerArchetype.RimProtector
        rebounding >= athletic + 5 -> PlayerArchetype.PaintBeast
        athletic >= shooting -> PlayerArchetype.RollBig
        else -> PlayerArchetype.PostScorer
    }
}
