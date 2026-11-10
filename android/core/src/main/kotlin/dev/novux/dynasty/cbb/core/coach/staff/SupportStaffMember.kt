package dev.novux.dynasty.cbb.core.coach.staff

data class SupportStaffMember(
    val staffId: String,
    val displayName: String,
    val role: SupportStaffRole,
    val contributionRating: Int,
    val contractYearsLeft: Int,
) {
    init {
        require(contributionRating in 0..99)
    }
}
