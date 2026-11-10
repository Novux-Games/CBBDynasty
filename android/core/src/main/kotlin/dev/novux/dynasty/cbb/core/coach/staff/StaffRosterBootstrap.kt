package dev.novux.dynasty.cbb.core.coach.staff

import dev.novux.dynasty.cbb.core.coach.card.AssistantCoachCard
import dev.novux.dynasty.cbb.core.coach.card.AssistantCoachRatings
import kotlin.random.Random

object StaffRosterBootstrap {
    fun defaultAssistants(dynastyId: String, schoolDisplayName: String, seed: Long): List<AssistantCoachCard> {
        val random = Random(seed)
        val roles = listOf(
            CoachTrackRole.AssociateHeadCoach,
            CoachTrackRole.Assistant,
            CoachTrackRole.RecruitingCoordinator,
        )
        val names = listOf(
            "Dana Rivers",
            "Chris Walton",
            "Jordan Price",
        )
        return roles.mapIndexed { index, role ->
            AssistantCoachCard(
                staffId = "asst-$dynastyId-${index + 1}",
                displayName = names[index],
                age = 32 + index * 3,
                schoolDisplayName = schoolDisplayName,
                seasonRecordLabel = "0-0",
                role = role,
                yearsInRole = 1 + index,
                yearsAtSchool = 2 + index,
                contractYearsLeft = 2,
                contractLengthYears = 3,
                ratings = AssistantCoachRatings(
                    recruiting = 68 + random.nextInt(12),
                    development = 70 + random.nextInt(10),
                    scouting = 66 + random.nextInt(14),
                ),
            )
        }
    }

    fun defaultSupport(dynastyId: String, seed: Long): List<SupportStaffMember> {
        val random = Random(seed xor 9)
        val roles = SupportStaffRole.entries.take(3)
        val names = listOf("Taylor Reed", "Morgan Ellis", "Casey Shaw")
        return roles.mapIndexed { index, role ->
            SupportStaffMember(
                staffId = "support-$dynastyId-${index + 1}",
                displayName = names[index],
                role = role,
                contributionRating = 62 + random.nextInt(20),
                contractYearsLeft = 1 + index,
            )
        }
    }
}
