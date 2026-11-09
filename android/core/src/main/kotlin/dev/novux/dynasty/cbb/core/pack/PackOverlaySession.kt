package dev.novux.dynasty.cbb.core.pack

import dev.novux.dynasty.cbb.core.save.DynastySchema

/**
 * Active pack overlay in memory (display cache). Swap replaces this session;
 * dynasty saves keep stable `teamId` / `confId`.
 */
data class PackOverlaySession(
    val packId: String,
    val membershipYear: String,
    val leagueDisplayName: String,
    val index: IdentityPackIndex,
) {
    fun teamDisplay(teamId: String): TeamDisplayOverlay? = index.team(teamId)

    fun conferenceDisplay(confId: String): ConferenceDisplayOverlay? = index.conference(confId)
}

object PackOverlaySessions {
    fun fromValidatedPack(pack: IdentityPack): PackOverlaySession {
        val packId = pack.packId ?: DynastySchema.DEFAULT_PACK_ID
        return PackOverlaySession(
            packId = packId,
            membershipYear = pack.membershipYear,
            leagueDisplayName = PackDisplayNames.forPack(pack),
            index = IdentityPackIndex.from(pack),
        )
    }

    /**
     * Replace overlay; stable keys unchanged ([game-design.md](../planning/game-design.md) pack swap).
     */
    fun swap(session: PackOverlaySession, newPack: IdentityPack): PackSwapResult {
        val next = fromValidatedPack(newPack)
        val previousId = session.packId
        return PackSwapResult(
            previousPackId = previousId,
            session = next,
            teamCount = next.index.allTeamIds().size,
        )
    }
}

data class PackSwapResult(
    val previousPackId: String,
    val session: PackOverlaySession,
    val teamCount: Int,
)
