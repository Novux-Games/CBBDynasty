package dev.novux.dynasty.cbb.core.coach.skill

import dev.novux.dynasty.cbb.core.coach.card.HeadCoachRatings

/** Apply skill tree levels to displayed coach ratings. */
object HeadCoachSkillRatings {
    fun effective(base: HeadCoachRatings, state: CoachSkillPointState): HeadCoachRatings {
        val recruiting = base.recruiting + CoachSkillTreeEngine.ratingBonus(
            CoachSkillTreeEngine.level(state, CoachSkillBranch.Recruiting),
        ) + CoachSkillTreeEngine.ratingBonus(
            CoachSkillTreeEngine.level(state, CoachSkillBranch.NilCharm),
        ) / 2
        val training = base.training + CoachSkillTreeEngine.ratingBonus(
            CoachSkillTreeEngine.level(state, CoachSkillBranch.PlayerDevelopment),
        )
        val tactics = CoachSkillTreeEngine.level(state, CoachSkillBranch.Tactics)
        val offensive = (base.offensive + CoachSkillTreeEngine.ratingBonus(tactics)).coerceIn(0, 99)
        val defensive = (base.defensive + CoachSkillTreeEngine.ratingBonus(tactics)).coerceIn(0, 99)
        return HeadCoachRatings(
            offensive = offensive,
            defensive = defensive,
            training = training.coerceIn(0, 99),
            recruiting = recruiting.coerceIn(0, 99),
        )
    }
}
