package dev.novux.dynasty.cbb.data.coach

import dev.novux.dynasty.cbb.core.coach.CoachArchetype
import dev.novux.dynasty.cbb.core.coach.card.HeadCoachCard
import dev.novux.dynasty.cbb.core.coach.card.HeadCoachCardFactory
import dev.novux.dynasty.cbb.core.coach.staff.SchoolStaffRoster
import dev.novux.dynasty.cbb.core.coach.staff.StaffRosterBootstrap
import dev.novux.dynasty.cbb.core.strategy.DefensiveSchemePreset
import dev.novux.dynasty.cbb.core.strategy.OffensiveSchemePreset
import dev.novux.dynasty.cbb.data.pack.PackRepository
import dev.novux.dynasty.cbb.database.DynastyDatabase
import dev.novux.dynasty.cbb.database.staff.toAssistant
import dev.novux.dynasty.cbb.database.staff.toEntity
import dev.novux.dynasty.cbb.database.staff.toSupport

class SchoolStaffRepository(
    private val database: DynastyDatabase,
    private val packRepository: PackRepository,
) {
    private val staffDao = database.dynastyStaffDao()
    private val coachDao = database.dynastyCoachDao()

    suspend fun ensureBootstrap(dynastyId: String, schoolDisplayName: String) {
        val existing = staffDao.listForDynasty(dynastyId)
        if (existing.isNotEmpty()) return
        val seed = dynastyId.hashCode().toLong()
        val assistants = StaffRosterBootstrap.defaultAssistants(dynastyId, schoolDisplayName, seed)
        val support = StaffRosterBootstrap.defaultSupport(dynastyId, seed)
        staffDao.upsertAll(
            assistants.map { it.toEntity(dynastyId) } + support.map { it.toEntity(dynastyId) },
        )
    }

    suspend fun loadStaffRoster(dynastyId: String, seasonRecordLabel: String = "0-0"): SchoolStaffRoster? {
        val coach = coachDao.getByDynastyId(dynastyId) ?: return null
        val team = packRepository.activeSession().index.team(coach.userTeamId)
        val schoolName = team?.displayName ?: "Your program"
        ensureBootstrap(dynastyId, schoolName)
        val archetype = CoachArchetype.fromWire(coach.archetypeWire) ?: CoachArchetype.Balanced
        val offensive = OffensiveSchemePreset.fromWire(coach.offensiveSchemeWire) ?: OffensiveSchemePreset.Balanced
        val defensive = DefensiveSchemePreset.fromWire(coach.defensiveSchemeWire) ?: DefensiveSchemePreset.ManToMan
        val alma = coach.almaMaterTeamId?.let { packRepository.activeSession().index.team(it)?.displayName }
        val programPrestige = team?.programPrestige ?: 50
        val head = HeadCoachCardFactory.build(
            dynastyId = dynastyId,
            displayName = HeadCoachCardFactory.displayName(coach.firstName, coach.lastName, coach.nameSuffixWire),
            age = coach.age,
            schoolDisplayName = schoolName,
            archetype = archetype,
            offensiveScheme = offensive,
            defensiveScheme = defensive,
            almaMaterLabel = alma,
            seasonRecordLabel = seasonRecordLabel,
            avatarPresetId = coach.avatarPresetId,
            programPrestige = programPrestige,
        )
        val rows = staffDao.listForDynasty(dynastyId)
        val assistants = rows.mapNotNull { it.toAssistant(schoolName, seasonRecordLabel) }
        val support = rows.mapNotNull { it.toSupport() }
        return SchoolStaffRoster(headCoach = head, assistants = assistants, supportStaff = support)
    }

    suspend fun loadHeadCoachCard(dynastyId: String): HeadCoachCard? =
        loadStaffRoster(dynastyId)?.headCoach

    suspend fun loadAssistantCard(dynastyId: String, staffId: String) =
        loadStaffRoster(dynastyId)?.assistants?.firstOrNull { it.staffId == staffId }
}
