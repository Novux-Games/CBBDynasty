package dev.novux.dynasty.cbb.core.recruiting

/**
 * Rolled once at player creation; fixed for career (portal keeps same priorities).
 */
enum class RecruitPriority(
    val chipLabel: String,
    val factor: RecruitingInterestFactor,
) {
    StayHome("Stay close to home", RecruitingInterestFactor.Proximity),
    FollowMoney("Follow the money", RecruitingInterestFactor.Nil),
    StartNow("Start right away", RecruitingInterestFactor.PlayingTimeFit),
    WinningProgram("Wants a winning program", RecruitingInterestFactor.PrestigeWinning),
    CoachBond("Trust the coach", RecruitingInterestFactor.CoachRelationship),
    CampusFit("Campus / academics first", RecruitingInterestFactor.CampusLife),
}

object RecruitPriorityRoll {
    fun roll(prospectId: String): List<RecruitPriority> {
        val random = kotlin.random.Random(prospectId.hashCode().toLong())
        val count = if (random.nextBoolean()) 2 else 1
        return RecruitPriority.entries.shuffled(random).take(count)
    }
}
